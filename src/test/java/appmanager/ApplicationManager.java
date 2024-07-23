package appmanager;


import TestProjectDAO.MaintenanceDAO;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import appmanager.HelperBase;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class ApplicationManager {
    public static WebDriver driver;
    private static String browser;

    static String target = System.getProperty("target", "local");
    static PropertyFileReader reader = new PropertyFileReader(String.format("local.properties", target));

//    @Autowired
//    HelperBase helperBase;

    public ApplicationManager(String browser) {
        this.browser = browser;
    }

    public ApplicationManager() {
    }

//    public static WebDriver getWebDriver() {
//        if (driver == null) {
//            if ("".equals(reader.get("selenium.server"))) {
//                if (browser.equals(BrowserType.IE)) {
//                    System.setProperty("webdriver.ie.driver", "./drivers/IEDriverServer.exe");
//                    DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
//                    caps.setCapability("ignoreProtectedModeSettings", true);
//                    caps.setCapability("ie.ensureCleanSession", true);
//                    caps.setCapability("enableElementCacheCleanup", true);
//                    caps.setCapability("ignoreZoomSetting", true);
//                    caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
//                    driver = new InternetExplorerDriver(caps);
//                    driver.manage().deleteAllCookies();
//                } else if (browser.equalsIgnoreCase("edge")) {
//                    System.setProperty("webdriver.edge.driver", "./drivers/msedgedriver.exe");
//                    driver = new EdgeDriver();
//
//                } else if (browser.equals(BrowserType.CHROME)) {
////                    System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
//                    File file = new File(ExtentCucumberFormatter.outputDirectory + File.separator + "TestDocuments");
//
//                    file.mkdir();
//                    Map<String, Object> chromePrefs = new HashMap<String, Object>();
//                    chromePrefs.put("profile.default_content_settings.popups", 0);
//                    chromePrefs.put("profile.default_content_setting_values.notifications", 2);
//                    chromePrefs.put("download.default_directory", file);
//                    chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
//                    chromePrefs.put("download.prompt_for_download", false);
//
//                    ChromeOptions options = new ChromeOptions();
////                    options.addArguments("--headless");
//                    options.addArguments("--no-sandbox");
//                    options.addArguments("--remote-debugging-port=9222");
//                    options.addArguments("--disable-dev-shm-usage");
//                    options.setExperimentalOption("prefs", chromePrefs);
//                    DesiredCapabilities cap = DesiredCapabilities.chrome();
//                    cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
//                    cap.setCapability(ChromeOptions.CAPABILITY, options);
//                    driver = new ChromeDriver(options);
//                    driver.manage().deleteAllCookies();
//                }
//            } else {
//                DesiredCapabilities capabilities = new DesiredCapabilities();
//                capabilities.setBrowserName(browser);
//                try {
//                    driver = new RemoteWebDriver(new URL(reader.get("selenium.server")), capabilities);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//            }
//            driver.manage().window().maximize();
//            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
//            driver.get(reader.get("web.Url"));
//
//            return driver;
//        } else {
//            return driver;
//        }
//    }


    public static WebDriver getWebDriver() {
        try {
            if (driver == null) {
                if ("".equals(reader.get("selenium.server"))) {
                    if (browser.equals("ie")) {
                        driver = new InternetExplorerDriver();
                        driver.manage().deleteAllCookies();
                    } else if (browser.equals("edge")) {
                        File file = new File(ExtentCucumberFormatter.outputDirectory + File.separator + "TestDocuments");
                        file.mkdir();
                        String file1 = file.getAbsolutePath();
                        Map<String, Object> edgePrefs = new HashMap<String, Object>();
                        edgePrefs.put("profile.default_content_settings.popups", 0);
                        edgePrefs.put("profile.default_content_setting_values.notifications", 2);
                        edgePrefs.put("download.default_directory", file1);
                        edgePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
                        edgePrefs.put("download.prompt_for_download", false);
                        EdgeOptions op = new EdgeOptions();
//                        op.setExperimentalOption("prefs", edgePrefs);
//                        driver = new EdgeDriver(op);
                        driver = new EdgeDriver();
                        driver.manage().deleteAllCookies();
                    } else if (browser.equals("chrome")) {

//                        File file = new File(ExtentCucumberFormatter.outputDirectory + File.separator + "TestDocuments");
//
//                        file.mkdir();
//                        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
//                        chromePrefs.put("profile.default_content_settings.popups", 0);
//                        chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
//                        chromePrefs.put("download.prompt_for_download", false);
//                        ChromeOptions options = new ChromeOptions();
//                        options.setExperimentalOption("prefs", chromePrefs);
//                        options.addArguments("--test-type");
//                        options.addArguments("--disable-extensions");
//                        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
//                        driver = new ChromeDriver(options);
//                        driver.manage().deleteAllCookies();

                        File file = new File(ExtentCucumberFormatter.outputDirectory + File.separator + "TestDocuments");
                        file.mkdir();
                        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                        chromePrefs.put("profile.default_content_settings.popups", 0);
                        chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
                        chromePrefs.put("download.prompt_for_download", false);
                        ChromeOptions options = new ChromeOptions();
                        options.setExperimentalOption("prefs", chromePrefs);
                        options.addArguments("--test-type");
                        options.addArguments("--disable-extensions");
                        options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
//                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver(options);
                        driver.manage().deleteAllCookies();

                    }
                } else {
                    DesiredCapabilities capabilities = new DesiredCapabilities();
                    capabilities.setBrowserName(browser);
                    try {
                        driver = new RemoteWebDriver(new URL(reader.get("selenium.server")), capabilities);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

                return driver;
            } else {
                return driver;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void stop() {
        try {
            if (driver != null) {
                driver.close();
                driver = null;
            }
        } catch (Exception e) {

        }
    }

    public void initUrl(String userName) {
        try {
            String userid = "fhlbny\\" + userName;
            getWebDriver().get(reader.get("web.Url"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void robotType(Robot robot, String characters) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(characters);
        clipboard.setContents(stringSelection, null);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(1000);
    }


}
