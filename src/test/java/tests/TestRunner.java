package tests;

import appmanager.*;
import com.google.common.collect.ImmutableMap;
import cucumber.api.CucumberOptions;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

@CucumberOptions(features = {"classpath:features"},
        glue = "tests",
        plugin = {"io.qameta.allure.cucumberjvm.AllureCucumberJvm", "appmanager.ExtentCucumberFormatter"},
//          dryRun = true,
        tags = {"@Smoke123"})


public class TestRunner extends AbstractTestNGCucumberTests {
    String sourceDir = "./src/main/resources/";

    public TestRunner() {
    }

    private static ExtentCucumberFormatter formatter;
    private PropertyFileReader localreader = new PropertyFileReader("local.properties");
    EmailSender email = new EmailSender();
    static ArrayList<String> listOfScenarios = new ArrayList<>();
    static ArrayList<String> results = new ArrayList<>();
    String emailSenderSwitch = localreader.get("send.email").replaceAll("\\s+", "");
    String[] emailAddresses = localreader.get("send.emailAddress").replaceAll("\\s+", "").split(",");
    HelperBase base = new HelperBase();


    @BeforeSuite
    public void setUp() {
        try{
            formatter = new ExtentCucumberFormatter();
            ExtentCucumberFormatter.initiateExtentCucumberFormatter();
            ExtentCucumberFormatter.loadConfig(new File(sourceDir + "extent-config.xml"));
            allureEnvironmentWriter(
                    ImmutableMap.<String, String>builder()
                            .put("Browser", "Chrome")
                            .put("Stand", "Production")
                            .put("URL", "https://prod-sk:8080/home")
                            .put("Database.Server", "agl-entdbp2")
                            .put("Application.Server", "lx-skap-p1.fhlbny.net")
                            .build(), System.getProperty("user.dir")
                            + "/build/allure-results/");

        }catch (NullPointerException n){
            System.out.println("hit the null pointer exception");
        }


    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws IOException {
        formatter.releaseMediaPlayer();
        ApplicationManager.stop();
        Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");

        if (emailSenderSwitch.equalsIgnoreCase("On") && base.getCurrentLocalHour() <= 9 && listOfScenarios.toString().contains("PASSED")) {
            // sending morning run results regardless of test results
            sendEmail(emailAddresses);

        } else if (emailSenderSwitch.equalsIgnoreCase("On") && listOfScenarios.toString().contains("FAILED")) {
            // sending results to dist list if script is failing this includes phone text messages
            String[] distList = {"6464838873@vtext.com"};
            sendEmail(emailAddresses);
            sendMessage(distList);
        }



    }


    @Before
    public void startScenario(Scenario scenario) {

        String fileName = scenario.getName().split(" ")[0];
        String[] tagsToBeRun = localreader.get("tagsForVideoCapture").replaceAll("\\s+", "").split(",");
        for (String tag : scenario.getSourceTagNames()) {
            for (String tagToBeRun : tagsToBeRun) {
                if (tag.equalsIgnoreCase(tagToBeRun)) {
                    System.out.println("================================= " + tag + " ==========================================");
                    HelperBase.screenShotSwitch = formatter.startVideoRecording(fileName);
                } else {
                    HelperBase.screenShotSwitch = false;
                }
            }
        }

    }


    @After
    public void endScenario(Scenario scenario) {
        ApplicationManager.stop();
        listOfScenarios.add(scenario.getStatus().toUpperCase() + " - " + scenario.getName());
        formatter.stopVideoRecording();
    }

    public void sendEmail(String[] to) {
        Comparator<String> comparator = Comparator.<String, Boolean>comparing(s -> s.contains("FAILED")).reversed().thenComparing(Comparator.naturalOrder());
        Collections.sort(listOfScenarios, comparator);
        String message = email.buildPostMessage(listOfScenarios);
        String subject = listOfScenarios.toString().contains("FAILED") ? "Application Status Check: " + localreader.get("application.name") + " FAILED. "
                : "Application Status Check: " + localreader.get("application.name") + " PASSED.";
        email.sendHTMLmessage(to, subject, message);
    }


    public void sendMessage(String[] to) {
        Comparator<String> comparator = Comparator.<String, Boolean>comparing(s -> s.contains("FAILED")).reversed().thenComparing(Comparator.naturalOrder());
        Collections.sort(listOfScenarios, comparator);
        String message = email.buildPostTextMessage(listOfScenarios);
        String subject = listOfScenarios.toString().contains("FAILED") ? localreader.get("application.name") + " FAILED. "
                : "Application Status Check: " + localreader.get("application.name") + " PASSED.";
        email.sendHTMLmessage(to, subject, message);
    }
}

