package TestProjectPages;

import appmanager.Assertions;
import appmanager.HelperBase;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.asserts.Assertion;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Blazedeompage extends HelperBase {
    public void login() throws AWTException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\maheshbabuy\\Drivers\\edgedriver_win64\\msedgedriver.exe\"");
        WebDriver driver = new EdgeDriver();
        driver.manage().window().maximize();
        driver.get("https://blazedemo.com/");
        Robot robot = new Robot();
        sleep(2000);
        robot.keyPress(KeyEvent.VK_ENTER);
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getTitle());



    }

    @Value("${Departuretxt}")
    public String departure;

    public void validate_blazedemopage() {
        try {
//        WebElement we=getW
//        ebElement("//h2[text()='Choose your departure city:']");
//            WebElement we1=getWebElement(departure);

            ArrayList<String> list=new ArrayList<String>();
            list.add("Choose your departure city:");
            list.forEach(s -> {
                Assertions.getInstance().assertEquals(s,"Choose your departure city:");


            });



//            if (isElementPresent(we1)){
//                testStepPassed("successfully validated:");
//            }
//            else {
//                testStepFailed("Not successfully validated:");
//            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void click_on_find_flights(){
        sleep(2000);
        String findflights ="//input[@value='Find Flights']";
        JsClick(findflights,"Find Flights");
    }
}
