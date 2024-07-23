package appmanager;

import io.qameta.allure.Allure;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.awaitility.Duration;
import org.json.simple.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static appmanager.ApplicationManager.getWebDriver;
import static appmanager.ApplicationManager.stop;
import static org.awaitility.Awaitility.await;


public class HelperBase extends ExtentCucumberFormatter {


    public static String loggedInUser = "";

    public static Properties obj = new Properties();

    @Value("${btnAdd}")
    public String btnAdd;

    @Value("${btnSave}")
    public String btnSave;

    @Value("${btnSearch}")
    public String btnSearch;

    @Value("${btnBack}")
    public String btnBack;

    @Value("${btnCommonUpdate}")
    public String btnCommonUpdate;

    @Value("${btnCommonCancel}")
    public String btnCommonCancel;

    @Value("${btnUpdate}")
    public String btnUpdate;

    @Value("${btnDelete}")
    public String btnDelete;

    @Value("${btnCancel}")
    public String btnCancel;

    @Value("${btnReset}")
    public String btnReset;

    @Value("${weUserName}")
    public String weUserName;

    @Value("${btnCSV}")
    public String btnCSV;

    @Value("${btnXLS}")
    public String btnXLS;

    @Value("${btnPDF}")
    public String btnPDF;

    @Value("${btnRefresh}")
    public String btnRefresh;

    @Value("${casFirstPage}")
    public String casFirstPage;

    @Value("${casPreviousPage}")
    public String casPreviousPage;

    @Value("${casNextPage}")
    public String casNextPage;

    @Value("${casLastPage}")
    public String casLastPage;

    @Value("${drpShowEntry}")
    public String drpShowEntry;

    @Value("//a[@class='breadcrumbLink']")
    public String wesBreadCumb;

    @Value("${weRecordCount}")
    public String weRecordCount;

    @Value("${weToastMessage}")
    public String weToastMessage;

    @Value("${btnDeleteConfirm}")
    public String btnDeleteConfirm;

    @Value("${btnDeleteCancel}")
    public String btnDeleteCancel;

    @Value("${btnSaveChanges}")
    public String btnSaveChanges;

    @Value("${btnDeleteSelected}")
    public String btnDeleteSelected;

    @Value("${boHome}")
    public String boHome;

    @Value("${tableRowData}")
    public String tableRowData;

    @Value("${tableRowsData}")
    public String tableRowsData;

    @Value("${table}")
    public String table;

    @Value("${tableOneRowData}")
    public String tableOneRowData;

    @Value("${weErrorMsg}")
    public String weErrorMsg;


    @Value("${txtUsername}")
    public String txtUsername;

    @Value("${txtPassword}")
    public String txtPassword;

    @Value("${btnLogin}")
    public String btnLogin;


    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC_STRING = "1234567890";

    public static boolean screenShotSwitch = false;
    public static JSONObject objData;
    static String target = System.getProperty("target", "local");
    protected static PropertyFileReader reader = new PropertyFileReader(String.format("local.properties", target));


    public HelperBase() {
    }

    public boolean checkLogInUser(String user) {
        ApplicationManager app = null;
        if (reader.get("Browser").toLowerCase().contains("chrome")) {
            app = new ApplicationManager((System.getProperty("Browser", "chrome")));
        } else if (reader.get("Browser").toLowerCase().contains("edge")) {
            app = new ApplicationManager((System.getProperty("Browser", "edge")));
        } else {
            app = new ApplicationManager((System.getProperty("browser", "ie")));
        }
        boolean blnLogIn = false;
        WebDriver wd = ApplicationManager.driver;
        if (wd != null) {
            if (isElementPresent(By.xpath("//div[@id='dropdownBasic2']/span[2]"))) {
                WebElement we = wd.findElement(By.xpath("//div[@id='dropdownBasic2']/span[2]"));
                String userLoggged = getText(we);
                if (user.equalsIgnoreCase(userLoggged)) {
                    testStepPassed("User is already logged into the SonarQube application, Username is -->" + userLoggged);
                    blnLogIn = true;
                }
            }
            if (!blnLogIn) {
                stop();
                try {
                    app.initUrl(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                blnLogIn = true;
                app.initUrl(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return blnLogIn;

    }

    public void login(String username) {
        String pwdDecode = new String(Base64.decodeBase64(reader.get("" + username + "")));
        sendText("//input[@id='username']",username);
        sendText("//input[@id='password']",pwdDecode);
        JsClick("//input[@value='Login']", "Login");
    }

    public void checkLogInUser() {
        ApplicationManager app = null;
        if (reader.get("browser").toLowerCase().equals("chrome")) {
            app = new ApplicationManager((System.getProperty("browser", "chrome")));
        } else if ((reader.get("browser").toLowerCase().equals("edge"))) {
            app = new ApplicationManager((System.getProperty("browser", "edge")));
        }
        try {
            getWebDriver().get(reader.get("web.ExternalUrl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigateToHomePage() {
        JsClick(ApplicationManager.getWebDriver().findElement(By.xpath("//img[@class='img-fluid']")), "Home link");
        waitAbit(5);
        if (getPageUrl().endsWith("home")) {
            testStepPassed("Navigated to home page successfully");
        }
    }

    public boolean checkLogInInvalidUser(String user) {
        ApplicationManager driverManager = new ApplicationManager((System.getProperty("browser", "ie")));
        boolean blnLogIn = false;
        try {
            driverManager.initUrl(user);
            blnLogIn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blnLogIn;
    }

    public String text(By locator) {
        String text = "";
        try {
            text = getWebElement(locator).getText();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return text;
    }

    protected void robotType(Robot robot, String characters) {
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

    public void robotAcceptPopUp() {
        try {
//            waitTillLoadingCompleted();
            Robot robot = new Robot();
            robot.delay(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(1000);
            testStepPassed("Clicked on \"OK\" button from pop-up");
        } catch (Exception e) {
            testStepFailed("Couldn't click on \"OK\" button from pop-up");
        }

    }

    public void robotCancelPopUp() {
        try {
//            waitTillLoadingCompleted();
            Robot robot = new Robot();
            robot.delay(1000);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(1000);
            testStepPassed("Clicked on \"Cancel\" button from pop-up");
        } catch (Exception e) {
            testStepFailed("Couldn't click on \"Cancel\" button from pop-up");
        }

    }


    public void saveExportedFile(String fileName, String format) {
        try {
            // waitTillLoadingCompleted();
            waitAbit(2);
            Robot robot = new Robot();
            robot.delay(4000);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(4000);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(3000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(3000);
            System.out.println("&^&^%  " + System.getProperty("user.dir"));
            System.out.println("&^&^%  " + outputDirectory);
            System.out.println("&^&^%  " + outputDirectory.replace(".", ""));
            boolean file = new File(System.getProperty("user.dir") + outputDirectory.replace(".", "") + "\\TestDocuments").mkdir();
            String fileCompletePath = System.getProperty("user.dir") + outputDirectory.replace(".", "") + "\\TestDocuments\\" + fileName;

            System.out.println("&^&^%  " + fileCompletePath);

            robotType(robot, fileCompletePath);
            String lnk_path = ".\\TestDocuments\\" + fileName;

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.keyPress(KeyEvent.VK_ALT);
            robot.keyPress(KeyEvent.VK_N);
            robot.delay(5000);
            robot.keyRelease(KeyEvent.VK_ALT);
            robot.keyRelease(KeyEvent.VK_N);
            robot.delay(2000);
            robot.keyPress(KeyEvent.VK_ESCAPE);
            if (fileExists(fileCompletePath)) {
                testReporter("blue", "<a href=" + lnk_path + ">View Exported " + fileName + "</a>");
                Path content = Paths.get(fileCompletePath);
                try (InputStream is = Files.newInputStream(content)) {
                    Allure.step("Download Exported Files Here " + fileName, () -> Allure.addAttachment(fileName, "document/CSVORXLS", is, format));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                testStepFailed("File is not downloaded successfully->" + fileName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            testStepException(new Exception().getStackTrace()[0].getMethodName(), e);
        }

    }

    public void checkFileExist(String filePath, String fileName, String format) {
        sleep(5000);
        if (fileExists(filePath + fileName)) {
            testReporter("blue", "<a href=" + filePath + ">View Exported " + fileName + "</a>");
            String fileCompletePath = filePath + fileName;
            Path content = Paths.get(fileCompletePath);
            try (InputStream is = Files.newInputStream(content)) {
                Allure.step("Download Exported Files Here " + fileName, () -> Allure.addAttachment(fileName, "document/CSVORXLS", is, format));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            testStepFailed("File is not downloaded successfully->" + fileName);
        }
    }


    public boolean fileExists(String fileName) {
        try {
            for (int i = 0; i < 30; i++) {
                File file = new File(fileName);
                if (file.exists() && file.isFile()) {
                    return true;
                } else {
                    waitAbit(5);
                }
            }
        } catch (Exception e) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), e);
            return false;
        }
        return false;
    }

    public void deleteFile(String filePath, String fileName) {
        try {
            String fileLoc = filePath + fileName;
            File file = new File(fileLoc);

            if (file.exists()) {
                file.delete();

            }
        } catch (Exception e) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), e);
        }
    }

    public void deleteAllFilesWithName(String filePath, String fileName) {
        try {
            File folder = new File(filePath);
            for (File file : folder.listFiles()) {
                if (file.getName().contains(fileName)) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), e);
        }
    }

    public static String getFileName() {
        String timeFile = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss a").format(new Date());
        return timeFile.replace(" ", "");
    }

    // returns WebElement object please use instead of wd.findElement(By.locator) method  (created by Akbar 02.01.19)
    public WebElement getWebElement(String object) {
        return getWebElement(By.xpath(object));
    }

    // returns WebElement object please use instead of wd.findElement(By.locator) method  (created by Akbar 02.01.19)
    public List<WebElement> getWebElements(String object) {
        return getWebElements(By.xpath(object));
    }

    // returns WebElement object please use this method when you have multiple similar web objects with different values
    // Use when you going to get the web object by xpath containing text (created by Akbar 02.01.19)
    public WebElement getWebElementByPartialText(String objectValue) {
        return getWebElement(By.xpath("//*[contains(text(), '" + objectValue + "')]"));
    }


    public WebElement getWebElementByPartialText(String objectValue, String tagName) {
        return getWebElement(By.xpath("//" + tagName + "[contains(text(), '" + objectValue + "')]"));
    }

    // returns WebElement object please use this method when you have multiple similar web objects with different values
    // Use when you going to get the web object by xpath containing text (created by Akbar 02.01.19)
    public WebElement getWebElementByText(String objectValue) {
        return getWebElement(By.xpath("//*[text()='" + objectValue + "']"));
    }

    public WebElement getWebElementByText(String objectValue, String tagName) {
        return getWebElement(By.xpath("//" + tagName + "[text()='" + objectValue + "']"));
    }

    public WebElement getWebElement(By locator) {

        FluentWait<WebDriver> wait = new FluentWait<>(ApplicationManager.getWebDriver())
                .withTimeout(java.time.Duration.ofSeconds(20))
                .pollingEvery(java.time.Duration.ofSeconds(2))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
//                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NullPointerException.class);
        WebElement element = wait.until(new com.google.common.base.Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        });
        highlightAndTakeScreenShot(element);
        return element;
    }

    // returns list of WebElement objects please use instead of wd.findElements(By.locator) method (created by Akbar)
    public List<WebElement> getWebElements(By locator) {

        FluentWait<WebDriver> wait = new FluentWait<>(ApplicationManager.getWebDriver())
                .withTimeout(java.time.Duration.ofSeconds(120))
                .pollingEvery(java.time.Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
//                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class);

        List<WebElement> element = wait.until(new com.google.common.base.Function<WebDriver, List<WebElement>>() {
            public List<WebElement> apply(WebDriver driver) {
                return driver.findElements(locator);
            }
        });
        return element;
    }


    public boolean checkElementIsPresent(String element, String elementName) {
        try {
            WebElement webElement = getWebElement(element);
            if (webElement.isDisplayed()) {
                testStepPassed("==== The Object is displayed on the screen!!!!!! ==== " + elementName);
                return true;
            } else {
                testStepFailed("==== The Object is not displayed on the screen!!!!!! ====" + elementName);
                return false;
            }
        } catch (Exception e) {
            testStepFailed("==== The Object is not displayed on the screen!!!!!! ====" + elementName);
            e.printStackTrace();
            return false;
        }
    }


    public boolean isElementPresent(WebElement we) {
        try {
            return we.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isElementPresent(By by) {
        try {
            return getWebElement(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }

    }


    public boolean waitTillLoadingCompleted() {
        if (isElementPresent(By.xpath("//*[contains(@class,'spinner')]"))) {
            return elementToBeInvisible(By.xpath("//*[contains(@class,'spinner')]"));
        } else {
            return true;
        }
    }

    public boolean waitUntilElementLoads(By by, String eleName) {
        try {
            WebDriverWait wait = new WebDriverWait(ApplicationManager.getWebDriver(), java.time.Duration.ofSeconds(20));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            testStepPassed("==== The Object is displayed on the screen!!!!!! ==== " + eleName);
            return true;
        } catch (Exception ex) {
            testStepFailed("==== The Object is not displayed on the screen!!!!!! ==== " + eleName);
            return false;
        }
    }


    public static boolean elementToBeInvisible(By by) {
        try {
            WebDriverWait wait = new WebDriverWait(ApplicationManager.getWebDriver(), java.time.Duration.ofSeconds(40));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean waitForAlert() {
        try {
            WebDriverWait wait = new WebDriverWait(ApplicationManager.getWebDriver(), java.time.Duration.ofSeconds(20));
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isElementPresent(String obj) {
        try {
            return getWebElement(obj).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectFromDropDownMenu(String dropDownMenu, String visableText) {
        getWebElement(dropDownMenu).click();
        getWebElementByPartialText(visableText).click();
        tabOut();
    }

    public void tabOut() {
        try {
            Robot robot = new Robot();
            robot.delay(3000);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_TAB);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public boolean verifyDropDownMenuOptions(String dropDownMenu, String options) {
        boolean returnValue = false;
        getWebElement(dropDownMenu).click();
        String[] arrString = options.split(",");
        for (String option : arrString) {
            verifyLabelDisplayed(option);
            returnValue = true;
        }
        tabOut();
        return returnValue;
    }

    public void sendText(String object, String text) {
        WebElement element = getWebElement(object);
        element.clear();
        element.sendKeys(text);
        element.sendKeys(Keys.TAB);
    }

    public void clearText(String object) {
        WebElement element = getWebElement(object);
        element.clear();
        sleep(1000);
        element.sendKeys(Keys.TAB);
    }

    public boolean verifyElementDisplayed(String elem, String elemName) {
        try {
            waitAbit(5);
            WebElement webElement = getWebElement(elem);
            if (isElementPresent(webElement)) {
                testStepPassed("WebElement :" + elemName + " is displayed in the page as expected");
                return true;
            } else {
                testStepFailed("WebElement :" + elemName + " is not displayed in the page ");
                return false;
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while verifying the element " + elemName + ", Exception message is->" + ex.getMessage());
            return false;
        }
    }


    public boolean verifyPartialLabelDisplayed(String elementText) {
        try {
            WebElement webElement = getWebElementByPartialText(elementText, "span");
            if (isElementPresent(webElement)) {
                testStepPassed("WebElement with label :" + elementText + " is displayed in the page as expected");
                return true;
            } else {
                testStepFailed("WebElement with label :" + elementText + " is not displayed in the page ");
                return false;
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while verifying the element " + elementText + ", Exception message is-" + ex.getMessage());
            return false;
        }
    }

    public boolean verifyPartialLabelDisplayed(String elementText, String tagName) {
        try {
            long loopEnd = System.currentTimeMillis() + 20000;
            while (System.currentTimeMillis() != loopEnd) {
                WebElement webElement = getWebElementByPartialText(elementText, tagName);
                if (isElementPresent(webElement)) {
                    testStepPassed("WebElement with label :" + elementText + " is displayed in the page as expected");
                    return true;
                }
            }
            testStepFailed("WebElement with label :" + elementText + " is not displayed in the page ");
            return false;
        } catch (Exception ex) {
            testStepFailed("Exception caught while verifying the element " + elementText + ", Exception message is-" + ex.getMessage());
            return false;
        }
    }

    public boolean verifyLabelDisplayed(String labelText) {
        try {
            WebElement webElement = getWebElementByText(labelText);
            if (isElementPresent(webElement)) {
                testStepPassed("WebElement with label :" + labelText + " is displayed in the page as expected");
                return true;
            } else {
                testStepFailed("WebElement with label :" + labelText + " is not displayed in the page ");
                return false;
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while verifying the element " + labelText + ", Exception message is->" + ex.getMessage());
            return false;
        }
    }

    public boolean verifyLabelDisplayed(String labelText, String tagName) {
        try {
            //WebElement webElement = ApplicationManager.getWebDriver().findElement(By.xpath("//"+tagName+"[contains(text(),'"+labelText+"')]"));
            WebElement webElement = getWebElementByPartialText(labelText, "h5");
            if (isElementPresent(webElement)) {
                testStepPassed("WebElement with label :" + labelText + " is displayed in the page as expected");
                return true;
            } else {
                testStepFailed("WebElement with label :" + labelText + " is not displayed in the page ");
                return false;
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while verifying the element " + labelText + ", Exception message is->" + ex.getMessage());
            return false;
        }
    }

    public void verifyElementNotExist(String element, String elementName) {
        try {
            WebElement webElement = getWebElement(element);
            testStepFailed("WebElement :" + elementName + " is exist in the page");
        } catch (Exception ex) {
            testStepPassed("WebElement :" + elementName + " is not exist in the page as expected");
        }
    }


    public boolean verifyElementNotDsiaplyed(String element, String elementName) {
        try {
            WebElement webElement = getWebElement(element);
            if (isElementPresent(webElement)) {
                testStepFailed("WebElement :" + elementName + " is displayed in the page");
                return false;
            } else {
                testStepPassed("WebElement :" + elementName + " is not displayed in the page as expected");
                return true;
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while verifying the element :" + elementName + ", Exception message is->" + ex.getMessage());
            return false;
        }
    }


    public synchronized void highlightAndTakeScreenShot(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        js.executeScript("arguments[0].setAttribute('style', 'background: default; border: 5px solid red;');", element);
        if (HelperBase.screenShotSwitch == false) {
            takeDesktopScreenshot();
        } else {
            try {
                WebDriverWait wait = new WebDriverWait(getWebDriver(), java.time.Duration.ofSeconds(20));
                wait(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        js.executeScript("arguments[0].setAttribute('style', 'background: default; border: default;');", element);
    }

    public void switchToActiveWindow() {
        String parentWindowHandler = ApplicationManager.getWebDriver().getWindowHandle(); // Store your parent window
        String subWindowHandler = null;

        Set<String> handles = ApplicationManager.getWebDriver().getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }
        ApplicationManager.getWebDriver().switchTo().window(subWindowHandler); // switch to popup window
    }

    public void switchToIframe(String frameNameOrId) {

        ApplicationManager.getWebDriver().switchTo().frame(frameNameOrId); // switch to iframe
    }


    // Js click by displayed text
    public void clickTheLinkByDisplayedText(String linkText) {
        WebElement button = getWebElementByPartialText(linkText);
        if (button.isDisplayed()) {
            JavascriptExecutor js = (JavascriptExecutor) ApplicationManager.getWebDriver();
            js.executeScript("arguments[0].click();", button);
            testStepPassed("Clicked on the button ");
        } else {
            testStepFailed("Couldn't click the button !!!!!");
        }
    }

    public void acceptPopUp() {
        try {
            Alert alert = getWebDriver().switchTo().alert();
            alert.accept();
            testStepPassed("Accepted Pop-Up ");
        } catch (Exception e) {
            String method = new Exception().getStackTrace()[0].getMethodName();
            testStepFailed("Couldn't accept the pop-up, Exception Caught in method " + method +
                    ", and Error Message is->" + e.getMessage());
        }
    }


    //    public static String getToastMessage() {
//        try {
//            waitFor("toastMessage");
//            return wd.findElement(xpath("//div[@class='ng-tns-c2-0']")).getText();
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
//
//    public static void uncheckPrimaryFields() {
//        try {
//            List<WebElement> list = wd.findElements(xpath("//input[@formcontrolname='primyBorwFlag']"));
//            int size = list.size();
//            for (int i = 0; i < size; i++) {
//                boolean status = list.get(i).isSelected();
//                list = wd.findElements(xpath("//input[@formcontrolname='primyBorwFlag']"));
//                if (status) {
//                    int p = i + 1;
//                    JSClick1(By.xpath("(//input[@formcontrolname='primyBorwFlag'])[" + p + "]"));
//                }
//            }
//        } catch (Exception e) {
//            String method = new Exception().getStackTrace()[0].getMethodName();
//            testStepFailed("Exception Caught in method " + method +
//                    ", and Error Message is->" + e.getMessage());
//        }
//    }
//
//    public static void addBorrowers(String Salutation, String Firstname, String Lastname) {
//        try {
//            List<WebElement> list = wd.findElements(xpath("//div[@formarrayname='borrowers']/div"));
//            int size = list.size() - 1;
//            type(By.xpath("(//input[@formcontrolname='hsehldborwsaltnname'])[" + size + "]"), Salutation);
//            type(By.xpath("(//input[@formcontrolname='hsehldBorwFirstName'])[" + size + "]"), Firstname);
//            type(By.xpath("(//input[@formcontrolname='hsehldBorwLastName'])[" + size + "]"), Lastname);
//            JSClick(By.xpath("(//input[@formcontrolname='primyBorwFlag'])[" + size + "]"), "Primary");
//        } catch (Exception e) {
//            String method = new Exception().getStackTrace()[0].getMethodName();
//            testStepFailed("Exception Caught in method " + method + ", and Error Message is->" + e.getMessage());
//        }
//    }
//
//    public static void deleteBorrowers() {
//        try {
//            List<WebElement> list = wd.findElements(xpath("//button[@id='btn_delete']"));
//            int size = list.size();
//            if (size > 0) {
//                JSClick(By.xpath("(//button[@id='btn_delete'])[" + size + "]"), "Delete");
//                //validatePopUpMessage("post_ClosingPopUp", "Are you sure to delete?", "Accept");
//                JSClick ("okInPopUp","OK in delete popup");
//            }else{
//                testStepInfo("Borrower not found ,so can not perform delete operation ");
//            }
//        } catch (Exception e) {
//            String method = new Exception().getStackTrace()[0].getMethodName();
//            testStepFailed("Exception Caught in method " + method +
//                    ", and Error Message is->" + e.getMessage());
//        }
//    }
//
//    public static void UnsuccessfullDeleteBorrowers() {
//        try {
//            List<WebElement> list = wd.findElements(xpath("//button[@id='btn_delete']"));
//            int size = list.size();
//            if (size > 0) {
//                JSClick(By.xpath("(//button[@id='btn_delete'])[" + size + "]"), "Delete");
//                sleep(2000);
//                validatePopUpMessage("wePopUp", "You are about to delete row" + size +
//                        "  press ok to delete Cancel to abort delete", "Reject");
//            } else {
//                testStepInfo("Borrower not found, so can not perform delete operation ");
//            }
//        } catch (Exception e) {
//            String method = new Exception().getStackTrace()[0].getMethodName();
//            testStepFailed("Exception Caught in method " + method +
//                    ", and Error Message is->" + e.getMessage());
//        }
//    }
//
//    public static void validatePopUpMessage(String object, String expectedMessage, String Accept_Reject) {
//        try {
//            String appMessage = wd.findElement(xpath(obj.getProperty(object))).getText().trim();
//            if (appMessage.contains(expectedMessage.trim())) {
//                testStepPassed("Pop up message is as expected " + appMessage);
//                takeScreenshot();
//                if (Accept_Reject.equalsIgnoreCase("Accept")) {
//                    JSClick("btn_ok", "OK");
//                } else {
//                    JSClick("btn_Cancel", "Cancel");
//                }
//            } else {
//                testStepFailed("Pop up message is not as expected " + appMessage);
//                takeScreenshot();
//                if (Accept_Reject.equalsIgnoreCase("accept")) {
//                    JSClick("btn_ok", "OK");
//                } else {
//                    JSClick("btn_Cancel", "Cancel");
//                }
//            }
//        } catch (Exception e) {
//            String method = new Exception().getStackTrace()[0].getMethodName();
//            testStepFailed("Exception Caught in method " + method +
//                    ", and Error Message is->" + e.getMessage());
//        }
//    }
//
//    public static void validateMultiplePopUpMessage(String object, String expectedMessage, String Accept_Reject) {
//        try {
//            String appMessage = wd.findElement(xpath(obj.getProperty(object))).getText().trim();
//            String message[] = expectedMessage.split(",");
//            for (String aMessage : message) {
//                if (appMessage.contains(aMessage.trim())) {
//                    testStepPassed("Pop up message is as expected " + appMessage);
//                    takeScreenshot();
//                } else {
//                    testStepFailed("Pop up message is same as expected " + appMessage);
//                    takeScreenshot();
//                    /*if (Accept_Reject.equalsIgnoreCase("accept")) {
//                        JSClick("btn_ok", "OK");
//                    } else {
//                        JSClick("btn_Cancel", "Cancel");
//                    }*/
//                }
//            }
//            if (Accept_Reject.equalsIgnoreCase("Accept")) {
//                JSClick("btn_ok", "OK");
//            } else {
//                JSClick("btn_Cancel", "Cancel");
//            }
//        } catch (Exception e) {
//            String method = new Exception().getStackTrace()[0].getMethodName();
//            testStepFailed("Exception Caught in method " + method +
//                    ", and Error Message is->" + e.getMessage());
//        }
//    }
//
//    public static Boolean validateInfoMessage(String expMessage) {
//        try {
//            String infoMessage = getToastMessage();
//            if (infoMessage.toLowerCase().contains(expMessage.toLowerCase())) {
//                testStepInfo("Successfully validated the message-> " + expMessage);
//                takeScreenshot();
//                return true;
//            } else {
//                testStepFailed("Expected Message->" + expMessage +
//                        " is not displayed,instead " + infoMessage + " is displayed");
//                return false;
//            }
//        } catch (Exception e) {
//            String method = new Exception().getStackTrace()[0].getMethodName();
//            testStepFailed("Exception Caught in method " + method +
//                    ", and Error Message is->" + e.getMessage());
//            return false;
//        }
//    }
    public void moveToElement(String object) {
        WebDriver wd = ApplicationManager.driver;
        Actions action = new Actions(wd);
        action.moveToElement(getWebElement(object));
    }

    public void uploadFile(String fileName, String locator) {
//        try {
        String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\InputFiles\\TestData\\" + fileName;
        moveToElement(locator);
        WebElement we = getWebElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) ApplicationManager.getWebDriver();
//            js.executeScript("arguments[0].scrollIntoView(true);",we);
        js.executeScript("arguments[0].type='file';", we);
//            js.executeScript("arguments[0].style.display='block';", we);
        we.sendKeys(filePath);
        testStepInfo("File Uploaded ->" + fileName);
//        } catch (Exception e) {
//            testStepException(new Exception().getStackTrace()[0].getMethodName(), e);
//        }
    }

    public void uploadFileUnderOption(String fileName, String locator) {
        try {

            String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\InputFiles\\TestData\\" + fileName;

            moveToElement(locator);
            WebElement we = getWebElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) ApplicationManager.getWebDriver();
            String defaultStyle = we.getAttribute("style");
            js.executeScript("arguments[0].setAttribute('style', 'opacity: 110;pointer-events: none;display: block;visibility: visible;');", we);
            Thread.sleep(1500);
            we.sendKeys(filePath);
            Thread.sleep(1000);
            js.executeScript("arguments[0].setAttribute('style', '" + defaultStyle + "');", we);
            Thread.sleep(1500);
            testStepInfo("File Uploaded ->" + fileName);
        } catch (Exception e) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), e);
        } finally {
            testStepInfo("File Uploaded Process Completed ->" + fileName);
        }
    }

    public boolean checkElementIsEnabled(String elementText) {
        WebElement webElement = getWebElement(elementText);

        if (webElement.isEnabled()) {
            testStepPassed("==== The Object is enabled on the screen!!!!!! ==== " + elementText);
            return true;
        } else {
            testStepFailed("==== The Object is not enabled on the screen!!!!!! ====" + elementText);
            return false;
        }
    }

    public boolean checkElementIsNotEnabled(String elementText) {
        WebElement webElement = getWebElement(elementText);

        if (webElement.isEnabled()) {
            testStepFailed("==== The Object is enabled on the screen!!!!!! ==== " + elementText);
            return true;
        } else {
            testStepPassed("==== The Object is not enabled on the screen!!!!!! ====" + elementText);
            return false;
        }
    }

    public boolean checkElementIsSelected(String elementText) {
        WebElement webElement = getWebElement(elementText);
        if (webElement.isSelected()) {
            testStepPassed("The Object is selected on the screen. " + elementText);
            return true;
        } else {
            testStepFailed("The Object is not selected on the screen. " + elementText);
            return false;
        }
    }

    public boolean checkElementIsNotSelected(String elementText) {
        WebElement webElement = getWebElement(elementText);

        if (webElement.isSelected()) {
            testStepFailed("The Object is selected on the screen => " + elementText);
            return false;
        } else {
            testStepPassed("The Object is not selected on the screen => " + elementText);
            return true;
        }
    }

    public String getPageTitle() {
        try {
            return ApplicationManager.getWebDriver().getTitle();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public String getPageUrl() {
        try {
            return ApplicationManager.getWebDriver().getCurrentUrl();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public void JsSendText(String elem, String value) {
        try {
            WebElement weElem = getWebElement(elem);
            JavascriptExecutor js = (JavascriptExecutor) ApplicationManager.getWebDriver();
            js.executeScript("arguments[0].value='" + value + "';", weElem);
            testStepInfo("Successfully entered value ->" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void JsClick(String elem, String elementName) {
        try {
            WebElement weElem = getWebElement(elem);
            JavascriptExecutor js = (JavascriptExecutor) ApplicationManager.getWebDriver();
            js.executeScript("arguments[0].click();", weElem);
            testStepInfo("Successfully clicked on element->" + elementName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void JsClick(WebElement we, String elementName) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) ApplicationManager.getWebDriver();
            js.executeScript("arguments[0].click();", we);
            testStepInfo("Successfully clicked on element->" + elementName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void JsClickByVisibleText(String elementName) {
        try {
            waitAbit(5);
            WebElement elem = getWebElementByPartialText(elementName);
            JavascriptExecutor js = (JavascriptExecutor) ApplicationManager.getWebDriver();
            js.executeScript("arguments[0].click();", elem);
            testStepInfo("Successfully clicked on element -> " + elementName);
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public WebElement findValueFromTable(By locator, String expected) {
        WebElement element = null;
        for (WebElement ele : getWebElements(locator)) {
            String value = ele.getAttribute("value");
            if (value.contains(expected)) {
                testStepPassed("Found an expected value  " + expected);
                element = ele;
                System.out.println("Found the element +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ " + value);
                return element;
            }
        }
        return element;
    }

    public String findTextFromTable(By locator, String expected) {
        String elementText = null;
        for (WebElement ele : getWebElements(locator)) {
            String value = ele.getAttribute("value");
            if (value.contains(expected)) {
                testStepPassed("Found an expected value  " + expected);
                elementText = value;
                return elementText;
            }
        }
        return elementText;
    }

    public List<WebElement> headerElementsFromTable() {
        return getWebElements(".//th");
    }

    public List<String> columnNamesFromTable() {
        List<String> columnNames = new ArrayList<>();
        List<WebElement> headerElements = headerElementsFromTable();
        for (WebElement headerElement : headerElements) {
            columnNames.add(getText(headerElement));
        }
        return columnNames;
    }

    public List<WebElement> getRowsFromTable(String obj) {
        return getWebElements(obj);
    }

    public List<WebElement> getRowsFromTable() {
        return getWebElements(".//tr");
    }

    public List<WebElement> getAllDataFromRow(String obj, int rowNmb) {
        return getWebElements(getRowXPath(obj, rowNmb));
    }

    public void clickOn(WebElement we, String elemName) {
        try {
            we.click();
            testStepInfo("Clicked on Element-" + elemName);
        } catch (Exception ex) {
            testStepFailed("Unable to Click on Element- " + elemName + ", Exception is ->" + ex.getMessage());
        }
    }

    public void clickOn(String obj, String elemName) {
        try {
            WebElement we = getWebElement(obj);
            String s = we.getText();
            we.click();
            testStepInfo("Clicked on Element-" + elemName);
        } catch (Exception ex) {
            ex.printStackTrace();
            testStepFailed("Unable to Click on Element- " + elemName);
        }
    }

    public List<WebElement> getAllWebelementsFromDropdown(String select, String dropdownName) {
        try {
            Select weSel = new Select(getWebElement(select));
            List<WebElement> webElements = weSel.getOptions();
            testStepInfo("Getting all webelements for options from the dropdown " + dropdownName);
            return webElements;
        } catch (Exception ex) {
            testStepFailed("Unable to get all webelements for options from the dropdown " + dropdownName + " Exception message is->" + ex.getMessage());
            return null;
        }
    }

    public List<String> getTextsFromElements(String obj, String elmntName) {
        List<String> list = new ArrayList<>();
        try {
            for (WebElement element : getWebElements(obj)) {
                list.add(element.getText());
            }
            testStepInfo("Getting all texts from the elements of " + elmntName);
            return list;
        } catch (Exception ex) {
            testStepFailed("Unable to get all texts from the elements of " + elmntName + " Exception message is->" + ex.getMessage());
            return list;
        }
    }

    public List<String> getAllTextsFromDropdown(String select, String dropdownName) {
        List<String> list = new ArrayList<>();
        try {
            for (WebElement element : getAllWebelementsFromDropdown(select, dropdownName)) {
                list.add(element.getText());
            }
            testStepInfo("Getting all texts for options from the dropdown " + dropdownName);
            return list;
        } catch (Exception ex) {
            testStepFailed("Unable to get all texts for options from the dropdown " + dropdownName + " Exception message is->" + ex.getMessage());
            return list;
        }
    }

    public int getSizeFromDropdown(String select, String dropdownName) {
        try {
            int size = getAllWebelementsFromDropdown(select, dropdownName).size();
            testStepInfo("Get the size options from the dropdown " + dropdownName);
            return size;
        } catch (Exception ex) {
            testStepFailed("Unable to get the size options from the dropdown " + dropdownName + " Exception message is->" + ex.getMessage());
            return -1;
        }
    }

    public void selectOptionRandomlyFromDropdown(String select, String dropdownName) {
        try {
            selectFromDropdown(select, getRandomInt(1, getSizeFromDropdown(select, dropdownName) - 1), dropdownName);
            testStepInfo("Selected a value from the dropdown randomly, Dropdown Name:  " + dropdownName);
        } catch (Exception ex) {
            testStepFailed("Unable to selecta value from the dropdown randomly, Dropdown Name: " + dropdownName + " xPath: " + select + " Exception message is->" + ex.getMessage());
        }
    }

    public void selectFromDropdown(String select, String visibleText, String dropdownName) {
        try {
            sleep(5000);
            waitUntilElementLoads(By.xpath(select), dropdownName);
            Actions action = new Actions(getWebDriver());
            action.moveToElement(getWebElement(select)).perform();
            sleep(5000);
       /* JavascriptExecutor je = (JavascriptExecutor)ApplicationManager.getWebDriver() ;
        je.executeScript("arguments[0].scrollIntoView(true);",getWebElement(select));*/
            Select weSel = new Select(getWebElement(select));
            weSel.selectByVisibleText(visibleText);
            testStepInfo("Selected the value " + visibleText + " from the dropdown " + dropdownName);
        } catch (Exception ex) {
            testStepFailed("Unable to select the value " + visibleText + " from dropdown " + select + " Exception message is->" + ex.getMessage());
        }
    }

    public void selectFromDropdown(String select, int index, String dropdownName) {
        try {
            Select weSel = new Select(getWebElement(select));
            weSel.selectByIndex(index);
            testStepInfo("Selected the value with index " + index + " from the dropdown " + dropdownName);
        } catch (Exception ex) {
            testStepFailed("Unable to select the index " + index + " from dropdown " + select + " Exception message is->" + ex.getMessage());
        }
    }

    public void selectFromDropdownByValue(String select, String value, String dropdownName) {
        try {
            Select weSel = new Select(getWebElement(select));
            weSel.selectByValue(value);
            testStepInfo("Selected the value " + value + " from the dropdown " + dropdownName);
        } catch (Exception ex) {
            testStepFailed("Unable to select the value " + value + " from dropdown " + select + " Exception message is->" + ex.getMessage());
        }
    }

    public void verifyDefaultValueFromDropdown(String select, String expected, String dropdownName) {
        try {
            String slctValue = getSelectedValueFromDropdown(select);
            if (slctValue.equals(expected)) {
                testStepInfo("The default value is" + slctValue + " from the dropdown, and expected value is " + expected + " Dropdown Name " + dropdownName);
            } else {
                testStepFailed("The default value is not " + expected + " from the dropdown, and actual value is " + slctValue + "Dropdown Name " + dropdownName);
            }

        } catch (Exception ex) {
            testStepFailed("Unable to get the default value from dropdown " + dropdownName + ", Exception message is->" + ex.getMessage());
        }
    }

    public String getSelectedValueFromDropdown(String select) {
        try {
            Select weSel = new Select(getWebElement(select));
            return weSel.getFirstSelectedOption().getText();
        } catch (Exception ex) {
            testStepFailed("Unable to get the selected value from dropdown " + select + ", Exception message is->" + ex.getMessage());
            return "";
        }
    }

    public String getSelectedValueFromDropdown(WebElement element) {
        try {
            Select weSel = new Select(element);
            return weSel.getFirstSelectedOption().getText();

        } catch (Exception ex) {
            testStepFailed("Unable to get the selected value from dropdown " + element + ", Exception message is->" + ex.getMessage());
            return "";
        }
    }

    public String getText(WebElement we) {
        try {
            if (isElementPresent(we)) {
                return we.getText();
            } else {
                return "";
            }
        } catch (Exception ex) {
            testStepFailed("Unable to get the text element, message is->" + ex.getMessage());
            return "";
        }
    }


    public void clickOnRadioButton(String obj, String value, String elemName) {
        try {
            if (value.equals("C")) {
                WebElement we = getWebElement(obj);
                we.click();
                testStepInfo("Clicked on Element-" + elemName);
            } else if (value.equals("D")) {
                WebElement we = getWebElement(obj);
                we.click();
                testStepInfo("Clicked on Element-" + elemName);
            }
        } catch (Exception ex) {
            testStepFailed("Unable to Click on Element- " + elemName);
        }
    }

    public void clickOnRadioButton(String obj, String elemName) {
        try {
            WebElement we = getWebElement(obj);
            we.click();
            testStepInfo("Clicked on Element-" + elemName);

        } catch (Exception ex) {
            testStepFailed("Unable to Click on Element- " + elemName);
        }
    }

    public void Backspace(String object) {
        try {
            WebElement we = getWebElement(object);
            we.click();
            waitAbit(2);
            String value = we.getAttribute("value");
            for (int i = 0; i <= value.length() + 2; i++) {
                we.sendKeys(Keys.BACK_SPACE);
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught message is->" + ex.getMessage());
        }
    }

    public void enterText(String object, String text, String elemName) {
        try {
            WebElement we = getWebElement(object);
            //waitAbit(2);
            sleep(2000);
            //we.clear();
            if (!(we.getAttribute("value").length() == 0)) {
                Backspace(object);
            }
            we.sendKeys(text);
            testStepInfo("Entered the value in the  :" + elemName + " field :" + text);
        } catch (Exception ex) {
            testStepFailed("Exception caught while entering the value in :" + elemName + " field, message is->" + ex.getMessage());
        }
    }

    public String getTextValue(WebElement we) {
        try {
            if (isElementPresent(we)) {
                return we.getAttribute("value");
            } else {
                return "";
            }
        } catch (Exception ex) {
            testStepFailed("Unable to get the text element, message is->" + ex.getMessage());
            return "";
        }
    }

    public String getTextLength(WebElement we) {
        try {
            if (isElementPresent(we)) {
                return we.getAttribute("maxlength");
            } else {
                return "";
            }
        } catch (Exception ex) {
            testStepFailed("Unable to get the text element, message is->" + ex.getMessage());
            return "";
        }
    }

    public String getTextValue(String obj) {
        try {
            WebElement we = getWebElement(obj);
            if (isElementPresent(we)) {
                return we.getAttribute("value");
            } else {
                return "";
            }
        } catch (Exception ex) {
            testStepFailed("Unable to get the text element, message is->" + ex.getMessage());
            return "";
        }
    }


    public void validateGeneralComponentsInPage() {
        try {
            verifyElementDisplayed(btnCSV, "CSV Button");
            verifyElementDisplayed(btnXLS, "XLS Button");
//           // verifyElementDisplayed(btnPDF, "PDF Button");
            verifyElementDisplayed(btnRefresh, "Refresh Button");
            JsClick(casNextPage, "Next Page Link");
            verifyElementDisplayed(casFirstPage, "First Page Link");
            verifyElementDisplayed(casNextPage, "Next Page Link");
            verifyElementDisplayed(casPreviousPage, "Previous Page Link");
            verifyElementDisplayed(casLastPage, "Last Page Link");
            verifyElementDisplayed(btnSearch, "Search Button");
            verifyElementDisplayed(btnReset, "Reset Button");
            //verifyElementDisplayed(btnSaveChanges, "Save Chages Button");
            // verifyElementDisplayed(btnDeleteSelected, "Delete Slecrted Button");
//            if (isElementPresent(drpShowEntry)) {
//                String strValue = getSelectedValueFromDropdown(drpShowEntry);
//                if (strValue.contains("10")) {
//                    testStepInfo("Show dropdown is displayed and default value is set to 10 as expected");
//                } else {
//                    testStepFailed("Show dropdown is displayed and default value is not set to 10");
//                }
//            } else {
//                testStepFailed("Show dropdown is not displayed");
//            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while validating the generic elements in page, Message is->" + ex.getMessage());
        }
    }

    public void validateColumnNamesinTable(String obj, List<String> coloumNames) {
        try {
            boolean blnPass = true;
            List<WebElement> weColumnNames = getWebElements(obj);
            if (weColumnNames.size() == coloumNames.size()) {
                //int i=0;
                for (int i = 0; i < coloumNames.size(); i++) {
                    //for(WebElement we: weColumnNames){
                    //  weColumnNames = getWebElements(obj);
                    WebElement we = weColumnNames.get(i);
                    if (getText(we).contains(coloumNames.get(i))) {
                        testStepInfo("Column Name : " + coloumNames.get(i) + " is displayed as expected in Column Number " + (i + 1));
                    } else {
                        blnPass = false;
                        testStepFailed("Column Name : " + coloumNames.get(i) + " is not displayed in Column Number " + (i + 1));
                    }
                }
                if (blnPass) {
                    testStepPassed("All the required columns are displayed in table as expected");
                }
            } else {
                testStepFailed("All the required columns are not displayed in table, Expected Columns is " + coloumNames.size() + ", But actual is ->" + weColumnNames.size());
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while validating the generic elements in page, Message is->" + ex.getMessage());
        }
    }

    public void validateColumnNamesinTable(WebElement table, List<String> coloumNames) {
        try {
            boolean blnPass = true;
            List<WebElement> weColumnNames = table.findElements(By.xpath(".//thead/tr/th/span[1]"));
            if (weColumnNames.size() - 1 == coloumNames.size()) {
                //int i=0;
                for (int i = 0; i < coloumNames.size(); i++) {
                    //for(WebElement we: weColumnNames){
                    weColumnNames = table.findElements(By.xpath(".//thead/tr/th/span[1]"));
                    WebElement we = weColumnNames.get(i + 1);
                    if (getText(we).contains(coloumNames.get(i))) {
                        testStepInfo("Column Name : " + coloumNames.get(i) + " is displayed as expected in Column Number " + (i + 1));
                    } else {
                        blnPass = false;
                        testStepFailed("Column Name : " + coloumNames.get(i) + " is not displayed in Column Number " + (i + 1));
                    }
                }
                if (blnPass) {
                    testStepPassed("All the required columns are displayed in table as expected");
                }
            } else {
                testStepFailed("All the required columns are not displayed in table, Expected Columns is " + coloumNames.size() + ", But actual is ->" + weColumnNames.size());
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while validating the generic elements in page, Message is->" + ex.getMessage());
        }
    }

    public void sleep(int milisec) {
        try {
            Thread.sleep(milisec);
        } catch (Exception ex) {
            // do nothing
        }
    }

    public void sleepInSeconds(int secs) {
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException e) {
            System.out.println("Error happended in Thread sleepIn Seconds");
            e.printStackTrace();
        }
    }

    public int checkAndWait(String expStr, String locator) {
        try {
            long loopEnd = System.currentTimeMillis() + 20000;
            while (System.currentTimeMillis() != loopEnd) {
//                getWebDriver().manage().timeouts().setScriptTimeout(5000,TimeUnit.MILLISECONDS);
                /*try {
                    getWebElement(btnRefresh).click();
                } catch (Exception e) {
//                    e.printStackTrace();
                }*/
                WebElement ele = getWebDriver().findElement(By.xpath(locator));
                String[] numbers = ele.getText().split(" ");
                for (String num : numbers) {
                    if (expStr.equals(num)) {
                        return Integer.parseInt(num);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public int checkAndWaitFor(String expStr, String locator) {
        try {
            long loopEnd = System.currentTimeMillis() + 20000;
            while (System.currentTimeMillis() != loopEnd) {
//                getWebDriver().manage().timeouts().setScriptTimeout(5000,TimeUnit.MILLISECONDS);
                try {
                    getWebElement(btnRefresh).click();
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                WebElement ele = getWebDriver().findElement(By.xpath(locator));
                String[] numbers = ele.getText().split(" ");
                for (String num : numbers) {
                    if (expStr.equals(num)) {
                        return Integer.parseInt(num);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void waitAbit(int secs) {
        switch (secs) {
            case 2:
                await().atLeast(Duration.TWO_SECONDS);
                break;
            case 5:
                await().atLeast(Duration.FIVE_SECONDS);
                break;
            case 10:
                await().atLeast(Duration.TEN_SECONDS);
                break;
            default:
                await().atLeast(Duration.FIVE_SECONDS);
                break;
        }
    }

    public void validateBreadcrumb(String breadcrumb) {
        boolean blnTrue = true;
        String[] strBreadCrumbs = breadcrumb.split(" > ");
        List<WebElement> weBreadcrumb = getWebElements(wesBreadCumb);
        int i = 0;
        if (strBreadCrumbs.length - 1 == weBreadcrumb.size()) {
            for (WebElement we : weBreadcrumb) {
                if (getText(we).contains(strBreadCrumbs[i + 1])) {
                    testStepInfo(strBreadCrumbs[i + 1] + " breadcrumb is displayed");
                } else {
                    testStepFailed(strBreadCrumbs[i] + " breadcrumb is not displayed, instead " + we.getText() + " is displayed ");
                    blnTrue = false;
                }
                i++;
            }
            if (blnTrue) {
                testStepPassed("All the breadcumbs are displayed as expected ");
            }
//            WebElement weBreadcrumbLink1 =  getWebElement(By.xpath("//nav[@aria-label='breadcrumb']//a[contains(text(),'"+strBreadCrumbs[2]+"')]"));
//            clickOn(weBreadcrumbLink1,strBreadCrumbs[1]+" Breadcrumb");
//            waitTillLoadingCompleted();
//            verifyPartialLabelDisplayed(strBreadCrumbs[1],"td");
//            if(strBreadCrumbs.length==4) {
//                ApplicationManager.getWebDriver().navigate().back();
//                sleep(3000);
//            }
//            WebElement weBreadcrumbLink2 =  getWebElement(By.xpath("//nav[@aria-label='breadcrumb']//a[contains(text(),'"+strBreadCrumbs[0]+"')]"));
//            JsClick(weBreadcrumbLink2,strBreadCrumbs[0]+" Breadcrumb");
//            waitTillLoadingCompleted();
//            sleep(3000);
//            if(getPageUrl().endsWith("home")){
//                testStepPassed("Navigated to home page successfully");
//            }else{
//                testStepFailed("Unable to navigate to home page");
//            }
        } else {
            testStepFailed("Expected breadcrumb is not displayed in page, Expected is->" + breadcrumb);
        }
    }


    public void selectRecordFromTable(String objTable, int rowNum) {
        WebElement tbl = getWebElement(objTable);
        List<WebElement> weRows = tbl.findElements(By.xpath("./tbody/tr"));
        int rowCount = weRows.size();
        if (rowCount >= rowNum) {
            clickOn(weRows.get(rowNum - 1), rowNum + " row in the table");
        } else {
            testStepFailed("Total row count is only " + rowCount + " , hence row " + rowNum + " is not available");
        }
    }

    public void selectPolyPathRecordFromTable(String objTable, int rowNum) {
        waitAbit(5);
        WebElement tbl = getWebElement(objTable);
        List<WebElement> weRows = tbl.findElements(By.xpath("./tbody/tr"));
        int rowCount = weRows.size();
        if (rowCount >= rowNum) {
            JSClick(weRows.get(rowNum - 1).findElement(By.xpath("./td[1]/a")), rowNum + " row in the table");
        } else {
            testStepFailed("Total row count is only " + rowCount + " , hence row " + rowNum + " is not available");
        }
    }

    public void refreshBrowser() {
        driver.navigate().refresh();
    }


    public void selectRandomRecordFromTable(String objTable, int maxRowCount) {
        WebElement tbl = getWebElement(objTable);
        List<WebElement> weRows = tbl.findElements(By.xpath("./tbody/tr"));
        int rowCount = weRows.size();
        int rowNum = getRandomInt(1, maxRowCount);
        clickOn(weRows.get(rowNum - 1), rowNum + " row in the table");
    }

    public int getRandomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public String getRowXPath(String obj, int rowNum) {
        return obj.replace("rowNmbr", String.valueOf(rowNum));
    }

    public String getDynamicXPath(String obj, int num) {
        return obj.replace("indexNumber", String.valueOf(num));
    }

    public String getRowXPath(String table) {
        return table + "//tr";
    }

    public String getHeaderXPath(String table) {
        return table + "//th";
    }

    public String getCellXPath(String table) {
        return table + "//td";
    }

    public String getRowAndCellXPath(String obj, int rowNum, int columnNum) {
        obj = obj.replace("rowNmbr", String.valueOf(rowNum));
        obj = obj.replace("cellNmbr", String.valueOf(columnNum));
        return obj;
    }

    public String getColumnXPath(String obj, int columnNum) {
        return obj.replace("cellNmbr", String.valueOf(columnNum));
    }

    public String getTableCellText(String obj, int rowNum, int columnNum) {
        String str = "";
        try {
            String locator = getRowAndCellXPath(obj, rowNum, columnNum);
            return getText(getWebElement(locator));
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public List<WebElement> getAllCellWebElementsDatafromTable(String obj) {
        List<WebElement> cellData = new ArrayList<>();
        try {
            cellData = getWebElements(obj);
            return cellData;
        } catch (Exception e) {
            e.printStackTrace();
            return cellData;
        }
    }

    public List<String> getAllCellsDatafromTable(String obj) {
        List<String> cellData = new ArrayList<>();
        try {
            for (WebElement element : getWebElements(obj))
                cellData.add(getText(element));
            return cellData;
        } catch (Exception e) {
            e.printStackTrace();
            return cellData;
        }
    }

    public List<Map<String, String>> getTableDataAsListOfMap(String table) {

        List<Map<String, String>> userTable = new ArrayList<>();
        if (isElementPresent(table)) {
            List<String> columnNames = columnNamesFromTable();
            columnNames = columnNames.subList(0, columnNames.size());
            for (int i = 0; i < getRowsFromTable(table + "/tbody/tr").size(); i++) {
                Map<String, String> row = new HashMap<>();
                List<WebElement> rowList = getAllDataFromRow(table + "/tbody/tr[rowNmbr]/td", i + 1);
                for (int j = 0; j < columnNames.size(); j++) {
                    row.put(columnNames.get(j), rowList.get(j).getText());
                }
                userTable.add(row);
            }
            testStepPassed("Find the elements for table, locator => " + table);
        } else {
            testStepFailed("Couldn't find the element for table, locator => " + table);
        }
        return userTable;
    }

    public List<Map<String, String>> getTableDataAsListOfMap() {
        List<Map<String, String>> userTable = new ArrayList<>();
        try {
            if (isElementPresent(table)) {
                List<String> columnNames = columnNamesFromTable();
                columnNames = columnNames.subList(0, columnNames.size() - 1);
                for (int i = 0; i < getRowsFromTable(table + "//tr").size() - 1; i++) {
                    Map<String, String> row = new HashMap<>();
                    List<WebElement> rowList = getAllDataFromRow(table + "//tr[rowNmbr]//td", i + 1);
                    for (int j = 0; j < columnNames.size(); j++) {
                        if (j % 3 == 0 && j != 0) {
                            row.put(columnNames.get(j), getSelectedValueFromDropdown(getRowAndCellXPath(tableOneRowData, i + 1, j + 1) + "/select"));
                        } else {
                            row.put(columnNames.get(j), rowList.get(j).getText());
                        }
                    }
                    userTable.add(row);
                }
                testStepPassed("Find the elements for table, locator => " + table);
            } else {
                testStepFailed("Couldn't find the element for table, locator => " + table);
            }
        } catch (Exception e) {
            e.printStackTrace();
            testStepFailed("Couldn't find the element for table, locator => " + table);
        }
        return userTable;
    }

    public void VerifyPaginationFunctionality(String tableName) {
        waitTillLoadingCompleted();
        int startLast = getLastRecordCount();
        int startTotal = getTotalRecordCount();
        if (getFirstRecordCount() != 0) {
            if (isElementPresent(weRecordCount)) {
                if (isElementPresent(casNextPage)) {
                    JsClick(casNextPage, "Next");
                    sleep(4000);
                    int init = getFirstRecordCount();
//                    int init = checkAndWait("11", weRecordCount);
                    if (init == startLast + 1) {
                        testStepPassed("Sucessfully Validated NextPage button for " + tableName);
                    } else {
                        testStepFailed("Failed To Validate NextPage button for " + tableName);
                    }
                } else {
                    testStepInfo("Pagination Icons are not valid for " + tableName);
                }
            }

            if (isElementPresent(casPreviousPage)) {

                JsClick(casPreviousPage, "Previous Page Link");
                sleep(4000);
                int init = checkAndWait("1", weRecordCount);
                if (init == 1) {
                    testStepPassed("Sucessfully Validated Previous Page button for" + tableName);
                } else {
                    testStepFailed("Failed To Validate Previous Page button for " + tableName);
                }
            } else {
                testStepInfo("Pagination Icons are not present for " + tableName);
            }

            if (isElementPresent(casLastPage)) {

                JsClick(casLastPage, "Last Page Link");
                sleep(2000);
                int total = checkAndWait(String.valueOf(startTotal), weRecordCount);
                int last = checkAndWait(String.valueOf(startTotal), weRecordCount);
                if (total == last) {
                    testStepPassed("Sucessfully Validated LastPage button for " + tableName);
                } else {
                    testStepFailed("Failed To Validate LastPage button for " + tableName);
                }
            } else {
                testStepInfo("Pagination Icons are not present for " + tableName);
            }

            if (isElementPresent(casFirstPage)) {

                JsClick(casFirstPage, "First Page Link");
                sleep(2000);
                int init = checkAndWait("1", weRecordCount);
                if (init == 1) {
                    testStepPassed("Sucessfully Validated First Page button for " + tableName);
                } else {
                    testStepFailed("Failed To Validate First Page button for " + tableName);
                }
            } else {
                testStepInfo("Pagination Icons are not present for " + tableName);
            }
        } else {
            testStepInfo("unable to verify pagination Functionality as no records found for " + tableName);
            takeScreenshot();
        }
    }

    public void verifyRefreshButton(String webelement, String TableName) {
        int init = checkAndWait("1", weRecordCount);
        if ((init) > 0) {
            sleepInSeconds(5);
            if (isElementPresent(btnRefresh)) {
                JsClick(btnRefresh, "Refresh Button");
                if ((isElementPresent(By.xpath(webelement))) && (isElementPresent(btnRefresh))) {
                    testStepPassed("Successfully validated Refresh button for table->" + TableName);
                } else {
                    testStepFailed("Couldn't validate Refresh button for table->" + TableName);
                }
            } else {
                testStepFailed(" Refresh button does not exist for table->" + TableName);

            }
        } else {
            testStepInfo(" No records found,Hence Refresh Button is disabled for the table->" + TableName);
        }
    }

    public void verifyRefreshButton(String TableName) {
        int init = getFirstRecordCount();
        if ((init) > 0) {
            if (isElementPresent(btnRefresh)) {
                JsClick(btnRefresh, "Refresh Button");
                sleepInSeconds(5);
                if ((isElementPresent(By.xpath("//span[@id='dataListForm:dataTable']//table[@class='lcb']"))) && (isElementPresent(btnRefresh))) {
                    testStepPassed("Successfully validated Refresh button for table->" + TableName);
                } else {
                    testStepFailed("Couldn't validate Refresh button for table->" + TableName);
                }
            } else {
                testStepFailed(" Refresh button does not exist for table->" + TableName);
            }
        } else {
            testStepInfo(" No records found,Hence Refresh Button is disabled for the table->" + TableName);
        }
    }


    public String buildQuery(String tableName, HashMap<String, String> record) {
        String query = "Select * from " + tableName + " where ";
        for (Map.Entry<String, String> entry : record.entrySet()) {
            if (!entry.getValue().trim().equals("")) {
                //query = query + entry.getKey() + "= '" + entry.getValue() + "' and ";
                query = query + entry.getKey() + " LIKE '%" + entry.getValue() + "%' and ";
            }
        }
        if (query.endsWith("and ")) {
            query = query.substring(0, query.length() - 4);
        }
        if (query.endsWith("where ")) {
            query = query.substring(0, query.length() - 6);
        }
        return query;
    }

    public String getClass(WebElement we) {
        try {
            return we.getAttribute("class");
        } catch (Exception ex) {
            return "";
        }
    }

    public String getPaginationText() {
        try {
            WebElement ele = getWebElement(weRecordCount);
            return ele.getText();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }


    public int getTotalRecordCount() {
        String strText = getPaginationText();
        if (strText != null) {
            String[] strTexts = strText.split(" ");
            System.out.println(strTexts);
            return Integer.parseInt(strTexts[5]);
        } else {
            testStepFailed("Unable to get the record count from Pagination text for Total Record Count");
            return -1;
        }
    }

    public int getFirstRecordCount() {
        String strText = getPaginationText();
        if (strText != null) {
            String[] strTexts = strText.split("\\s");
            return Integer.parseInt(strTexts[1]);
        } else {
            testStepFailed("Unable to get the record count from Pagination text for First Record Count");
            return -1;
        }
    }

    public int getLastRecordCount() {
        String strText = getPaginationText();
        if (strText != null) {
            String[] strTexts = strText.split(" ");
            return Integer.parseInt(strTexts[3]);
        } else {
            testStepFailed("Unable to get the record count from Pagination text for Last Record Count");
            return -1;
        }
    }

    public void validateDefaultSorting(String tableRowData, int columnNumber, String ColumName, String Type, String
            TableName, String sortOrder) {
        try {
            WebDriver wd = ApplicationManager.getWebDriver();
            List<WebElement> row = getWebElements(By.xpath(tableRowData));
            int i = row.size();
            if (i > 0) {
                if (sortOrder.toLowerCase().contains("a")) {
//                    validateAscendingOrder(tableRowData, columnNumber, ColumName, Type);
                } else {
                    validateDescendingOrder(columnNumber, ColumName, Type);
                }
            } else {
                testStepInfo("No records found,Hence Sorting functionality cannot be validated");

            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while validating the default sorting functionality, Message is ->" + ex.getMessage());
        }
    }

    public void validateDefaultSorting(int columnNumber, String ColumName, String Type, String TableName, String sortOrder) {
        try {
            WebDriver wd = ApplicationManager.getWebDriver();
            List<WebElement> row = getWebElements(By.xpath(tableRowData));
            int i = row.size();
            if (i > 0) {
                if (sortOrder.toLowerCase().contains("a")) {
                    validateAscendingOrder(columnNumber, ColumName, Type);
                } else {
                    validateDescendingOrder(columnNumber, ColumName, Type);
                }
            } else {
                testStepInfo("No records found,Hence Sorting functionality cannot be validated");

            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while validating the default sorting functionality, Message is ->" + ex.getMessage());
        }
    }


    public static void setObjData(JSONObject objDataAct) {
        objData = objDataAct;
    }
//    public void ValidateSortingFunctionalityFoPartiallyInteger(String tableRowData, int columnNumber, String ColumName, String Type, String AppName) {
//        try {
//            WebDriver wd = ApplicationManager.getWebDriver();
//            List<WebElement> row = wd.findElements(By.xpath(tableRowData));
//            int i = row.size();
//            if (i > 0) {
//                sleep(3000);
//                By bySort = By.xpath("//thead[@class=\"dr-table-thead\"]/tr/th[" + columnNumber + "]/a");
//                if (!getClass(getWebElement(bySort)).contains("asc")) {
//                    JsClick(getWebElement(bySort), "Ascending");
//                }
//                sleep(5000);
//                validateAscendingOrderForPatiallyInteger(tableRowData, columnNumber, ColumName, Type);
//                if (!getClass(getWebElement(bySort)).contains("desc")) {
//                    JsClick(getWebElement(bySort), "Descending");
//                }
//                sleep(5000);
//                validateDescendingOrderForPartiallyIntiger(tableRowData, columnNumber, ColumName, Type);
//                sleep(3000);
//            } else {
//                testStepInfo("No records found,Hence Sorting functionality cannot be validated");
//
//            }
//        } catch (Exception ex) {
//            testStepFailed("Exception caught while validating the sorting functionality, Message is ->" + ex.getMessage());
//        }
//    }
//
//    public void ValidateSortingFunctionalityFoPartiallyInteger(int columnNumber, String ColumName, String
//            Type, String AppName) {
//        try {
//            WebDriver wd = ApplicationManager.getWebDriver();
//            List<WebElement> row = wd.findElements(By.xpath(tableRowData));
//            int i = row.size();
//            if (i > 0) {
//                sleep(3000);
//                By bySort = By.xpath("//thead[@class=\"dr-table-thead\"]/tr/th[" + columnNumber + "]/a");
//                if (!getClass(getWebElement(bySort)).contains("asc")) {
//                    JsClick(getWebElement(bySort), "Ascending");
//                }
//                sleep(5000);
//                validateAscendingOrderForPatiallyInteger(tableRowData, columnNumber, ColumName, Type);
//                if (!getClass(getWebElement(bySort)).contains("desc")) {
//                    JsClick(getWebElement(bySort), "Descending");
//                }
//                sleep(5000);
//                validateDescendingOrderForPartiallyIntiger(tableRowData, columnNumber, ColumName, Type);
//                sleep(3000);
//            } else {
//                testStepInfo("No records found,Hence Sorting functionality cannot be validated");
//
//            }
//        } catch (Exception ex) {
//            testStepFailed("Exception caught while validating the sorting functionality, Message is ->" + ex.getMessage());
//        }
//    }
//

    public void ValidateSortFunctionality(int columnNumber, String ColumName, String Type, String AppName) {
        try {
            WebDriver wd = getWebDriver();
            List<WebElement> row = wd.findElements(By.xpath("//div//table[@id='tbl_appApp']//tr[contains(@class,'tableData')]"));
            int i = row.size();
            if (i > 0) {
                sleep(3000);
                By bySort = By.xpath("//div[contains(@class,'table')]/table//span[text()='" + ColumName + "']/following::span[contains(@class,'sort')][1]");
                if (!getClass(getWebElement(bySort)).contains("asc")) {
                    JsClick(getWebElement(bySort), "Ascending");
                }
//                sleep(5000);
                waitTillLoadingCompleted();
                validateAscendingOrder(columnNumber, ColumName, Type);
                if (!getClass(getWebElement(bySort)).contains("desc")) {
                    JsClick(getWebElement(bySort), "Descending");
                }
//                sleep(5000);
                waitTillLoadingCompleted();
                validateDescendingOrder(columnNumber, ColumName, Type);
                sleep(3000);
            } else {
                testStepInfo("No records found,Hence Sorting functionality cannot be validated");

            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while validating the sorting functionality, Message is ->" + ex.getMessage());
        }
    }

    public String getSelectedOptionFromDropdown(WebElement dropdown) {
        Select sel = new Select(dropdown);
        return sel.getFirstSelectedOption().getText();
    }

    public void validateDescendingOrder(int columnNumber, String ColumName, String Type) {
        try {
            WebDriver wd = getWebDriver();
            int value = columnNumber + 1;
            int value1 = columnNumber + 1;
            WebElement icon = wd.findElement(By.xpath("//div[contains(@class,'table')]/table/thead/tr/th[" + value1 + "]/span[2]"));
            if (!icon.getAttribute("class").contains("desc")) {
                testStepFailed(ColumName + " column's icon is not changed to Descending");
            }
            List<WebElement> defaultRow = wd.findElements(By.xpath("//div[contains(@class,'table')]/table//tr[not(contains(@style,'background'))]"));
            int itCount = getTotalRecordCount() - 1;
            List<WebElement> elementName = new LinkedList<>();
            if (Type == "STRING") {
                ArrayList<String> obtainedEleList = new ArrayList<>();
                ArrayList<String> resultEleNameList = new ArrayList<>();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName = elementName.get(i).getText().trim();
                    if (!newelementName.equals("")) {
                        obtainedEleList.add(newelementName);
                        resultEleNameList.add(newelementName);
                        /*obtainedEleList.add(newelementName.toUpperCase());
                        resultEleNameList.add(newelementName.toUpperCase());*/
                    }
                }
                Collections.sort(obtainedEleList, Collections.reverseOrder());

                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Descending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Descending order");

                }
            }
            if (Type == "STRINGCUST") {
                ArrayList<String> obtainedEleList = new ArrayList<>();
                ArrayList<String> resultEleNameList = new ArrayList<>();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName = elementName.get(i).getText().trim();
                    if (!newelementName.equals("")) {
                        obtainedEleList.add(newelementName);
                        resultEleNameList.add(newelementName);
                    }
                }
                Collections.sort(obtainedEleList, Collections.reverseOrder());

                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Descending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Descending order");

                }
            } else if (Type == "INTEGER") {
                ArrayList<Integer> obtainedEleList = new ArrayList<>();
                ArrayList<Integer> resultEleNameList = new ArrayList<>();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName = elementName.get(i).getText().trim();
                    if (newelementName.contains("-")) {
                        newelementName = newelementName.split("-")[0];
                    }
                    if (!newelementName.equals("")) {
                        obtainedEleList.add(Integer.parseInt(newelementName));
                        resultEleNameList.add(Integer.parseInt(newelementName));
                    }
                }
                Collections.sort(obtainedEleList, Collections.reverseOrder());
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Descending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Descending order");

                }
            } else if (Type == "DOUBLE") {
                ArrayList<Double> obtainedEleList = new ArrayList<>();
                ArrayList<Double> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                int count = elementName.size();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName2 = elementName.get(i).getText().trim();
                    String newelementName1 = newelementName2.replaceAll(",", "");
                    String newelementName = newelementName1.replace("$", "");
                    if (!newelementName.equals("")) {
                        obtainedEleList.add(Double.valueOf(newelementName));
                        resultEleNameList.add(Double.valueOf(newelementName));
                    }
                }
                Collections.sort(obtainedEleList, Collections.reverseOrder());
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Descending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Descending order");
                }
            } else if (Type == "DATE") {
                ArrayList<Date> obtainedEleList = new ArrayList<>();
                ArrayList<Date> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                int count = elementName.size();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName = elementName.get(i).getText().trim();
                    SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                    if (!newelementName.equals("")) {
                        try {
                            obtainedEleList.add(date.parse(newelementName));
                            resultEleNameList.add(date.parse(newelementName));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                Collections.sort(obtainedEleList, Collections.reverseOrder());
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in descending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in descending  order");

                }
            } else if (Type == "DATETIME") {
                ArrayList<Date> obtainedEleList = new ArrayList<>();
                ArrayList<Date> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                int count = elementName.size();

                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName = elementName.get(i).getText().trim();
                    boolean flag = true;
                    SimpleDateFormat dateTime = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    if (!newelementName.equals("")) {
                        try {
                            resultEleNameList.add((dateTime.parse(newelementName)));
                            obtainedEleList.add((dateTime.parse(newelementName)));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                Collections.sort(resultEleNameList, Collections.reverseOrder());
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Descending   order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Descending   order");

                }
            } else if (Type == "DROPDOWN") {
                ArrayList<String> obtainedEleList = new ArrayList<>();
                ArrayList<String> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]/select"));
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]/select"));
                    String newelementName = getSelectedOptionFromDropdown(elementName.get(i));
                    if (!newelementName.trim().equals("")) {
                        obtainedEleList.add(newelementName.toUpperCase());
                        resultEleNameList.add(newelementName.toUpperCase());
                    }
                }
                Collections.sort(resultEleNameList, Collections.reverseOrder());
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Descending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Descending order");

                }
            }

            if (Type == "CHECKBOX") {
                ArrayList<String> obtainedEleList = new ArrayList<>();
                ArrayList<String> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]/input"));
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]/input"));
                    String newelementName;
                    if (elementName.get(i).isSelected()) {
                        newelementName = "B";
                    } else {
                        newelementName = "A";
                    }
                    if (!newelementName.equals("")) {
                        obtainedEleList.add(newelementName.toUpperCase());
                        resultEleNameList.add(newelementName.toUpperCase());
                    }
                }
                Collections.sort(obtainedEleList, Collections.reverseOrder());

                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Descending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Descending order");

                }
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while validating the Descending sorting functionality for column " + ColumName + ", Message is ->" + ex.getMessage());
        }

    }


    public void validateAscendingOrder(int columnNumber, String ColumName, String Type) {
        try {
            WebDriver wd = getWebDriver();
            int value = columnNumber + 1;
            int value1 = columnNumber + 1;
            WebElement icon = wd.findElement(By.xpath("//div[contains(@class,'table')]/table/thead/tr/th[" + value1 + "]/span[2]"));
            if (!icon.getAttribute("class").contains("asc")) {
                testStepFailed(ColumName + " column's icon is not changed to Ascending");
            }
//            List<WebElement> defaultRow = wd.findElements(By.xpath("//div[contains(@class,'table')]/table//tr[not(contains(@style,'background'))]"));
            int itCount = getTotalRecordCount() - 1;
            List<WebElement> elementName = new LinkedList<>();
            if (Type == "STRING") {
                ArrayList<String> obtainedEleList = new ArrayList<>();
                ArrayList<String> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    //String newelementName = (String) ((JavascriptExecutor) wd).executeScript("return arguments[0].text;", elementName.get(i));
                    String newelementName = elementName.get(i).getText().trim();
                    if (!newelementName.trim().equals("")) {
                        obtainedEleList.add(newelementName);
                        resultEleNameList.add(newelementName);
                        /*obtainedEleList.add(newelementName.toUpperCase());
                        resultEleNameList.add(newelementName.toUpperCase());*/
                    }
                }
                Collections.sort(obtainedEleList);
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Ascending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Ascending order");

                }
            }
            if (Type == "STRINGCUST") {
                ArrayList<String> obtainedEleList = new ArrayList<>();
                ArrayList<String> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    //String newelementName = (String) ((JavascriptExecutor) wd).executeScript("return arguments[0].text;", elementName.get(i));
                    String newelementName = elementName.get(i).getText().trim();
                    if (!newelementName.trim().equals("")) {
                        obtainedEleList.add(newelementName);
                        resultEleNameList.add(newelementName);
                    }
                }
                Collections.sort(obtainedEleList);
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Ascending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Ascending order");

                }
            } else if (Type == "INTEGER") {
                ArrayList<Integer> obtainedEleList = new ArrayList<>();
                ArrayList<Integer> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                int count = elementName.size();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName1 = elementName.get(i).getText().trim();
                    if (newelementName1.contains("-")) {
                        newelementName1 = newelementName1.split("-")[0];
                    }
                    String newelementName = newelementName1.replaceAll(",", "");
                    if (!newelementName.equals("")) {
                        obtainedEleList.add(Integer.parseInt(newelementName));
                        resultEleNameList.add(Integer.parseInt(newelementName));
                    }
                }
                Collections.sort(obtainedEleList);
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Ascending  order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Ascending order");

                }
            } else if (Type == "DOUBLE") {
                ArrayList<Double> obtainedEleList = new ArrayList<>();
                ArrayList<Double> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                int count = elementName.size();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName2 = elementName.get(i).getText().trim();
                    String newelementName1 = newelementName2.replaceAll(",", "");
                    String newelementName = newelementName1.replace("$", "").replace(")", "").replace("(", "-");
                    if (!newelementName.equals("")) {
                        obtainedEleList.add(Double.valueOf(newelementName));
                        resultEleNameList.add(Double.valueOf(newelementName));
                    }
                }
                Collections.sort(obtainedEleList);
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Ascending  order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Ascending order");

                }
            } else if (Type == "DATE") {
                ArrayList<Date> obtainedEleList = new ArrayList<>();
                ArrayList<Date> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                int count = elementName.size();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName = elementName.get(i).getText().trim();
                    SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
                    if (!newelementName.equals("")) {
                        try {
                            obtainedEleList.add(date.parse(newelementName));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            resultEleNameList.add(date.parse(newelementName));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }

                Collections.sort(obtainedEleList);
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Ascending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Ascending order");

                }
            } else if (Type == "DATETIME") {
                ArrayList<Date> obtainedEleList = new ArrayList<>();
                ArrayList<Date> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                int count = elementName.size();
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]"));
                    String newelementName = elementName.get(i).getText().trim();
                    boolean flag = true;
                    SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    if (!newelementName.equals("")) {
                        try {
                            resultEleNameList.add((date.parse(newelementName)));
                            obtainedEleList.add((date.parse(newelementName)));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                Collections.sort(resultEleNameList);
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Ascending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Ascending  order");

                }
            } else if (Type == "DROPDOWN") {
                ArrayList<String> obtainedEleList = new ArrayList<>();
                ArrayList<String> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]/select"));
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]/select"));
                    String newelementName = getSelectedOptionFromDropdown(elementName.get(i));
                    if (!newelementName.trim().equals("")) {
                        obtainedEleList.add(newelementName.toUpperCase());
                        resultEleNameList.add(newelementName.toUpperCase());
                    }
                }
                Collections.sort(obtainedEleList);
                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Ascending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Ascending order");

                }
            }

            if (Type == "CHECKBOX") {
                ArrayList<String> obtainedEleList = new ArrayList<>();
                ArrayList<String> resultEleNameList = new ArrayList<>();
                elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]/input"));
                for (int i = 0; i < itCount; i++) {
                    elementName = wd.findElements(By.xpath("//table[contains(@class,'table table-bordered')]//tr[contains(@class,'tableData')]/td[" + value + "]/input"));
                    String newelementName;
                    if (elementName.get(i).isSelected()) {
                        newelementName = "B";
                    } else {
                        newelementName = "A";
                    }
                    if (!newelementName.equals("")) {
                        obtainedEleList.add(newelementName.toUpperCase());
                        resultEleNameList.add(newelementName.toUpperCase());
                    }
                }
                Collections.sort(obtainedEleList);

                if (resultEleNameList.equals(obtainedEleList)) {
                    testStepPassed(ColumName + " column is sorted in Descending order");
                } else {
                    testStepFailed(ColumName + " column is not sorted in Descending order");

                }
            }
        } catch (Exception ex) {
            testStepFailed("Exception caught while validating the Ascending sorting functionality for column " + ColumName + ", Message is ->" + ex.getMessage());
        }
    }

    public void validateExportFunctionality(String format) {
        String saveFile = getFileName();
        System.out.println("filename : " + saveFile);
        if (format.equalsIgnoreCase("XLS")) {
            String fileName = saveFile + ".xls";
            sleep(2000);
            waitUntilElementLoads(By.xpath(btnXLS), "export xls");
            JsClick(btnXLS, "XLS button");
            sleep(3000);
            saveExportedFile(fileName, format);
        }
        if (format.equalsIgnoreCase("CSV")) {
            String fileName = saveFile + ".csv";
            sleep(2000);
            waitUntilElementLoads(By.xpath(btnCSV), "export csv");
            JsClick(btnCSV, "CSV button");
            sleep(3000);
            saveExportedFile(fileName, format);
        }
    }


    public void validateExportFunctionality(String format, String fileName) {

        if (format.equalsIgnoreCase("XLS")) {

            sleep(8000);
            waitUntilElementLoads(By.xpath(btnXLS), "export xls");
            String filePath = System.getProperty("user.home") + "\\Downloads\\";
            deleteAllFilesWithName(filePath, fileName);
            fileName = fileName + ".xlsx";
            clickOn(btnXLS, "XLS button");
            waitTillLoadingCompleted();
            checkFileExist(filePath, fileName, format);

        }
        if (format.equalsIgnoreCase("CSV")) {

            sleep(2000);
            waitUntilElementLoads(By.xpath(btnCSV), "export csv");
            String filePath = System.getProperty("user.home") + "\\Downloads\\";
            deleteAllFilesWithName(filePath, fileName);
            fileName = fileName + ".csv";
            JsClick(btnCSV, "CSV button");
            waitTillLoadingCompleted();
            checkFileExist(filePath, fileName, format);
        }
        if (format.equalsIgnoreCase("PDF")) {

            sleep(2000);
            waitUntilElementLoads(By.xpath(btnPDF), "export pdf");
            String filePath = System.getProperty("user.home") + "\\Downloads\\";
            deleteAllFilesWithName(filePath, fileName);
            fileName = fileName + ".pdf";
            JsClick(btnPDF, "PDF button");
            waitTillLoadingCompleted();
            checkFileExist(filePath, fileName, format);
        }
    }

    public void refreshPage() {
        getWebDriver().navigate().refresh();
        testStepInfo("Refreshed the Page Successfully");

    }

    public void clickOnCheckBox(String obj, String elemName) {
        try {
            WebElement we = getWebElement(obj);
            we.click();
            testStepInfo("Clicked on Element-" + elemName);
        } catch (Exception ex) {
            ex.printStackTrace();
            testStepFailed("Unable to Click on Element- " + elemName);
        }
    }


    public void clickOnCheckBox(String obj, String value, String elemName) {
        try {
            if (value.equals("Y")) {
                WebElement we = getWebElement(obj);
                we.click();
                testStepInfo("Clicked on Element-" + elemName);
            } else if (value.equals("N")) {
                WebElement we = getWebElement(obj);
                we.click();
                testStepInfo("Clicked on Element-" + elemName);
            }
        } catch (Exception ex) {
            testStepFailed("Unable to Click on Element- " + elemName);
        }
    }


    public void validateErrorMessageForField(WebElement elem, String elemName, String errorMessage) {
        try {
            WebElement we = elem.findElement(By.xpath("./following::div[2]"));
            if (we.getText().trim().toLowerCase().contains(errorMessage.trim().toLowerCase())) {
                testStepPassed("Error message is displayed in field " + elemName + " as ->" + errorMessage);
            } else {
                testStepFailed("Error message displayed in field " + elemName + " is not valid, Expceted ->" + errorMessage + ", But actual is->" + we.getText());
            }
        } catch (Exception ex) {
            testStepFailed("Error message is not displayed in field " + elemName);
        }
    }

    public void validateErrorMessageForField(String obj, String elemName, String errorMessage) {
        try {
            String errMsg = getOneString(obj, elemName);
            if (errMsg.trim().toLowerCase().contains(errorMessage.trim().toLowerCase())) {
                testStepPassed("Error message is displayed in field " + elemName + " as ->" + errorMessage);
            } else {
                testStepFailed("Error message displayed in field " + elemName + " is not valid, Expceted -> " + errorMessage + ", But actual is-> " + errMsg);
            }
        } catch (Exception ex) {
            testStepFailed("Error message is not displayed in field " + elemName);
        }
    }

    public String getOneString(String obj, String elmntName) {
        String str = "";
        try {
            for (WebElement element : getWebElements(obj)) {
                str += element.getText();
            }
            testStepInfo("Getting all texts as one string from the elements of " + elmntName);
            return str;
        } catch (Exception ex) {
            testStepFailed("Unable to get all texts as one string from the elements of " + elmntName + " Exception message is->" + ex.getMessage());
            return str;
        }
    }

    public void validateNoErrorMessageForField(WebElement elem, String elemName) {
        try {
            List<WebElement> wes = elem.findElements(By.xpath("./following-sibling::div/div[1]"));
            if (wes.size() == 0) {
                testStepPassed("Error message is not displayed in field " + elemName + " as expected");
            } else {
                testStepFailed("Error message displayed in field " + elemName + " ,Message is ->" + wes.get(0).getText());
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
        }
    }

    public void validatePaginationFunctionality(String ScreenName) {
        String strValue = getSelectedValueFromDropdown(drpShowEntry);
        int total = getTotalRecordCount();
        if (total >= (Integer.parseInt(strValue))) {
            try {
                VerifyPaginationFunctionality(ScreenName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            testStepInfo(ScreenName + " table has ->" + total + " which is less tha the initial count-> " + strValue + ",Hence pagination functionalities are disabled ");
        }
    }

    public String getToastMessage() {
        try {
            return getText(getWebElement(weToastMessage));
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public String getToastMessage(String obj) {
        try {
            return getText(getWebElement(obj));
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public String getToastMessages() {
        try {
            String message = "";
            for (WebElement element : getWebElements(weToastMessage)) {
                message += element.getText();
            }
            return message;
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public Boolean validateToastMessages(String expMessage) {
        try {
            String infoMessage = getToastMessages();
            if (!infoMessage.equals("")) {
                if (infoMessage.toLowerCase().contains(expMessage.toLowerCase())) {
                    testStepPassed("Toaster message is displayed as expected -> " + expMessage);
                    return true;
                } else {
                    testStepFailed("Toaster message displayed is not valid , Expected is->" + expMessage + "  but actual is-> " + infoMessage);
                    return false;
                }
            } else {
                testStepFailed("Toaster message is not displayed");
                return false;
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
            return false;
        }
    }


    public Boolean validateToastMessage(String expMessage) {
        try {
            String infoMessage = getToastMessage();
            if (!infoMessage.equals("")) {
                if (infoMessage.toLowerCase().trim().contains(expMessage.toLowerCase().trim())) {
                    testStepPassed("Toaster message is displayed as expected -> " + expMessage);
                    return true;
                } else {
                    testStepFailed("Toaster message displayed is not valid , Expected is-> " + expMessage + "  but actual is-> " + infoMessage);
                    return false;
                }
            } else {
                testStepFailed("Toaster message is not displayed");
                return false;
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
            return false;
        }
    }

    public Boolean validateToastMessage(String obj, String expMessage) {
        try {
            String infoMessage = getToastMessage(obj);
            if (!infoMessage.equals("")) {
                if (infoMessage.toLowerCase().trim().contains(expMessage.toLowerCase().trim())) {
                    testStepPassed("Toaster message is displayed as expected -> " + expMessage);
                    return true;
                } else {
                    testStepFailed("Toaster message displayed is not valid , Expected is-> " + expMessage + "  but actual is-> " + infoMessage);
                    return false;
                }
            } else {
                testStepFailed("Toaster message is not displayed");
                return false;
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
            return false;
        }
    }

    public Boolean validateToastMessage(WebElement elemtent, String expMessage) {
        try {
            String infoMessage = elemtent.getText();
            if (!infoMessage.equals("")) {
                if (infoMessage.toLowerCase().trim().contains(expMessage.toLowerCase().trim())) {
                    testStepPassed("Toaster message is displayed as expected -> " + expMessage);
                    return true;
                } else {
                    testStepFailed("Toaster message displayed is not valid , Expected is-> " + expMessage + "  but actual is-> " + infoMessage);
                    return false;
                }
            } else {
                testStepFailed("Toaster message is not displayed");
                return false;
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
            return false;
        }
    }


    public void validateHomePageLoaded(String pageName) {
        if (isElementPresent(drpShowEntry)) {
            testStepPassed(pageName + " home page is displayed as expected");
        } else {
            testStepFailed(pageName + " home page is not displayed as expected");
        }
    }

    public void validateAddOrEditPageLoaded() {
        if (isElementPresent(btnCancel)) {
            testStepPassed(" Add/Edit page is displayed as expected");
        } else {
            testStepFailed("Add/Edit page is not displayed as expected");
        }
    }


    public void validateDatabaseValues(String dbValue, String AppValue, String columnName) {
        if (dbValue.equals(AppValue)) {
            testStepInfo("Database column " + columnName + " has the expected value -> " + dbValue);
        } else {
            testStepFailed("Database column " + columnName + " does not have expected value, Expected is-> " + AppValue + " But actual is->" + dbValue);
        }
    }

    public String getRandomAlphabetString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public String getRandomAlphaNumbericString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public String getRandomNumbericString(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public String getFirstWord(WebElement elmt) {
        String strText = getText(elmt);
        if (strText != null) {
            String[] strTexts = strText.split("\\s");
            return strTexts[0];
        } else {
            testStepFailed("Unable to get the first word from the string => " + getText(elmt));
            return null;
        }
    }

    public String getFirstWord(String strText) {

        if (strText != null) {
            String[] strTexts = strText.split("\\s");
            return strTexts[0];
        } else {
            testStepFailed("Unable to get the first word from the string => " + strText);
            return null;
        }
    }

    public int getFirstWordAsIntger(WebElement elmt) {
        String strText = getFirstWord(elmt);
        if (strText != null) {
            return Integer.parseInt(strText);
        } else {
            testStepFailed("Unable to get the first word as an intiger from the string => " + getText(elmt));
            return -1;
        }
    }

    public int getFirstWordAsIntger(String str) {
        String strText = getFirstWord(str);
        if (strText != null) {
            return Integer.parseInt(strText);
        } else {
            testStepFailed("Unable to get the first word as an intiger from the string => " + str);
            return -1;
        }
    }


    public String selectcurrentDate() {
        String CurrentDate = "";
        try {
            SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            CurrentDate = currentDate.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CurrentDate;

    }


    public static void JSClick(By by, String elementName) {
        WebDriver wd = ApplicationManager.driver;
        WebElement elem = wd.findElement(by);
        Actions action = new Actions(getWebDriver());
        action.moveToElement(elem).perform();

        JavascriptExecutor js = (JavascriptExecutor) wd;
        js.executeScript("arguments[0].click();", elem);
        testStepInfo("Successfully clicked on element->" + elementName);
    }

    public static void JSClick(WebElement element, String elementName) {
        WebDriver wd = ApplicationManager.driver;
        JavascriptExecutor js = (JavascriptExecutor) wd;
        js.executeScript("arguments[0].click();", element);
        testStepInfo("Successfully clicked on element->" + elementName);
    }


    public static void enterKEY() {
        WebDriver wd = ApplicationManager.driver;
        Actions action = new Actions(wd);
        action.sendKeys(Keys.ENTER).perform();
    }

    public void mouseOverToElement(String object) {
        Actions action = new Actions(driver);
        WebElement element = getWebElement(object);
        action.moveToElement(element).build().perform();
        waitAbit(2);
    }

    public String Prevdate(String dateString) {

        String result = "";
        try {

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");


            Date myDate = dateFormat.parse(dateString);


            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            calendar.add(Calendar.DAY_OF_YEAR, -1);


            Date previousDate = calendar.getTime();
            result = dateFormat.format(previousDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public void selectall(String object) {
        try {
            WebElement we = getWebElement(object);
            we.sendKeys(Keys.END);
            we.sendKeys(Keys.CONTROL + "a");
            we.sendKeys(Keys.DELETE);

        } catch (Exception ex) {
            testStepFailed("Exception caught message is->" + ex.getMessage());
        }
    }

    public void saveCrystalReportExportedFile(String fileName) throws AWTException, IOException {
        //waitTillLoadingCompleted();
        sleep(5000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_N);
        robot.delay(1000);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_N);

        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(5000);

        boolean file = new File(System.getProperty("user.dir") + outputDirectory.replace(".", "") + "\\TestDocuments").mkdir();
        String fileCompletePath = System.getProperty("user.dir") + outputDirectory.replace(".", "") + "\\TestDocuments\\" + fileName;
        robotType(robot, fileCompletePath);
        String lnk_path = ".\\TestDocuments\\" + fileName;
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_N);
        robot.delay(1000);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_N);

        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_ESCAPE);
        if (fileExists(fileCompletePath)) {
            testReporter("blue", "<a href=" + lnk_path + ">View Exported " + fileName + "</a>");
            Path content = Paths.get(fileCompletePath);
            try (InputStream is = Files.newInputStream(content)) {
                Allure.step("Exported Report Is Here", new Allure.ThrowableContextRunnableVoid<Allure.StepContext>() {
                    @Override
                    public void run(Allure.StepContext context) throws Throwable {
                        Allure.addAttachment("Download Exported Reports For " + fileName, "document/pdf", is, "pdf");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            testStepFailed("File is not downloaded successfully->" + fileName);
        }

    }

    public void generateReport() throws Throwable {
        try {
            String fileName = getFileName() + ".pdf";
            saveCrystalReportExportedFile(fileName);
            takeScreenshot();
        } catch (Exception e) {
            String method = new Exception().getStackTrace()[0].getMethodName();
            testStepFailed("Exception Caught in method " + method + ", and Error Message is->" + e.getMessage());
        }
    }

    public boolean waitForCrystalReport(String object) {
        for (int i = 0; i < 4; i++) {
            WebElement we = getWebElement(object);
            if (isElementPresent(we)) {
                return true;
            }
        }
        return false;
    }


    public boolean isAlertPresent() {
        try {
            getWebDriver().switchTo().alert();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void handleReportAuth(String UserName) {
        waitForAlert();
        try {
            if (isAlertPresent()) {
                String target = System.getProperty("target", "local");
                obj.load(new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/local.properties"));
                String pwdDecode = new String(Base64.decodeBase64(obj.getProperty("" + UserName + "")));
                Robot robot = null;
                robot = new Robot();
                robot.delay(2000);
                robotType(robot, UserName);
                robot.delay(3000);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.delay(1000);
                robotType(robot, pwdDecode);
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.delay(3000);
                getWebDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void navigateTOSystemAdministration() {

        System.out.println("$$$$ ");
        waitAbit(5);

        JsClick("//div[text()='Enterprise Services']", "Enterprise Services");
        JsClick("//a[contains(text(),'System Administration')]", "System Administration");
        if (getWebElement("//a[contains(text(),'FHLB Departments')]") != null) {
            testStepPassed("Sucessfully Navigated to System Administration Page");
        } else {
            testStepFailed("Failed to Navigate to System Administration Page");
        }

    }

    public void navigateToFHLBDepartment() {

        if (isElementPresent("//td[text()='FHLB Departments']")) {
            testStepPassed("Sucessfully Navigated to FHLB Departments Page");
        } else {
            testStepFailed("Failed to Navigate to FHLB Departments Page");
        }
    }


    public static String RandomString(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();

    }

    public static String RandomNumericString(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * NUMERIC_STRING.length());
            builder.append(NUMERIC_STRING.charAt(character));
        }
        return builder.toString();

    }

    public void clickCreate() {
        try {
            //waitTillLoadingCompleted();

            if (isElementPresent(By.xpath(btnAdd))) {
                waitUntilElementLoads(By.xpath(btnAdd), "Create");
                JsClick(btnAdd, "Create Button");
                testStepInfo("Successfully clicked on Create button");
            } else {
                testStepInfo("Create button is not present on the screen");
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
        }
        waitAbit(5);
    }

    public void clickCommonCancel() {
        try {
            waitUntilElementLoads(By.xpath(btnCommonCancel), "Cancel");
            if (isElementPresent(By.xpath(btnCommonCancel))) {
                JsClick(btnCommonCancel, "Cancel Button");
                testStepInfo("Successfully clicked on Create button");
            } else {
                testStepFailed("Cancel button is not present on the screen");
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
        }
        waitAbit(5);
    }

    public void clickCommonUpdate() {
        try {
            waitUntilElementLoads(By.xpath(btnCommonUpdate), "Update");
            if (isElementPresent(By.xpath(btnCommonUpdate))) {
                JsClick(btnCommonUpdate, "Update Button");
                testStepInfo("Successfully clicked on cancel button");
            } else {
                testStepFailed("Update button is not present on the screen");
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
        }

        waitAbit(5);
    }


    public void clickSearch() {
        try {
            waitUntilElementLoads(By.xpath(btnSearch), "Search");
            if (isElementPresent(By.xpath(btnSearch))) {
                JsClick(btnSearch, "Search Button");
                testStepPassed("Successfully clicked on Search button");
            } else {
                testStepInfo("Search button is not present on the screen");
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
        }
        waitAbit(5);

    }

    public void clickReset() {
        try {
            if (isElementPresent(By.xpath(btnReset))) {
                JsClick(btnReset, "Reset Button");
                testStepPassed("Successfully clicked on Reset button");
            } else {
                testStepInfo("Reset button is not present on the screen");
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
        }
        waitAbit(5);
    }


    public void clickSave() {
        try {
            waitUntilElementLoads(By.xpath(btnSave), "Save Button");
            if (isElementPresent(By.xpath(btnSave))) {
                JsClick(btnSave, "Save Button");
                testStepInfo("Successfully clicked on Save button");
            } else {
                testStepInfo("Save button is not present on the screen");
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
        }
        waitAbit(5);
    }

    public void clickBack() {
        try {
            waitUntilElementLoads(By.xpath(btnBack), "Back Button");
            if (isElementPresent(By.xpath(btnBack))) {
                JsClick(btnBack, "Back Button");
                testStepPassed("Successfully clicked on Back button");
            } else {
                testStepInfo("Back button is not present on the screen");
            }
        } catch (Exception ex) {
            testStepException(new Exception().getStackTrace()[0].getMethodName(), ex);
        }
        waitTillLoadingCompleted();
    }

    public void validateErrorMessage(String errorMessage) {
        sleep(2000);
        waitUntilElementLoads(By.xpath(weErrorMsg), "Error Message");
        sleep(1000);
        String actualMessage = getText(getWebElement(weErrorMsg));
        if (actualMessage.equals(errorMessage)) {
            testStepPassed("Expected error message :" + errorMessage + " has displayed in UI");
        } else {
            testStepFailed("Expected error message :" + errorMessage + " has not displayed in UI");
        }
    }

    public boolean waitUntilElementVisible(By by, String eleName) {
        try {
            WebDriverWait wait = new WebDriverWait(ApplicationManager.getWebDriver(), java.time.Duration.ofSeconds(80));
            wait.until(ExpectedConditions.visibilityOf(getWebElement(by)));
            testStepPassed("==== The Object is displayed on the screen!!!!!! ==== " + eleName);
            return true;

        } catch (Exception ex) {
            testStepFailed("==== The Object is not displayed on the screen!!!!!! ==== " + eleName);
            return false;

        }
    }


    public int getCurrentLocalHour() {
        int time = LocalTime.now().getHour();
        System.out.println("Current time in New York: " + time);
        return time;
    }


}
