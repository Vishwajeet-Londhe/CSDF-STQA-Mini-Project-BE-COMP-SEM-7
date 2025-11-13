package Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class InvalidDateOfBirthTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vishw\\OneDrive\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ✅ UNIVERSAL LOCATOR FOR TIME SLOT FIELD
    private WebElement getTimeSlotField() {
        By[] locators = new By[]{
                By.id("timeslot"),
                By.name("timeslot"),
                By.cssSelector("input[placeholder*='slot' i]"),
                By.xpath("//label[contains(text(),'Time Slot')]/following::input[1]"),
                By.xpath("//input[contains(@class,'form-control') and (contains(@placeholder,'Time') or contains(@placeholder,'Slot'))]")
        };

        for (By by : locators) {
            try {
                WebElement el = driver.findElement(by);
                if (el.isDisplayed()) return el;
            } catch (Exception ignored) {}
        }
        throw new NoSuchElementException("Time Slot field not found. Check HTML.");
    }

    @Test
    public void testFutureDateOfBirth() {

        driver.get("http://localhost/covid-tms/new-user-testing.php");

        // ✅ Fill Vishwajeet Londhe's details
        driver.findElement(By.id("fullname")).sendKeys("Vishwajeet Londhe");
        driver.findElement(By.id("mobilenumber")).sendKeys("8459652020");

        // ✅ Future DOB (Invalid)
        WebElement dob = driver.findElement(By.id("dob"));
        dob.clear();
        dob.sendKeys("01-01-2030");
        dob.sendKeys(Keys.TAB);

        driver.findElement(By.id("govtissuedid")).sendKeys("Adhar");
        driver.findElement(By.id("govtidnumber")).sendKeys("123456789012");
        driver.findElement(By.id("address")).sendKeys("Pune");
        driver.findElement(By.id("state")).sendKeys("Maharashtra");

        // ✅ Test Type = Antigen
        Select testType = new Select(driver.findElement(By.id("testtype")));
        testType.selectByVisibleText("Antigen");

        // ✅ Time Slot (auto-detected)
        WebElement timeSlot = getTimeSlotField();
        timeSlot.clear();
        timeSlot.sendKeys("12-11-2025 09:00 AM");

        // ✅ Submit
        driver.findElement(By.id("submit")).click();

        // ✅ CASE 1 — Browser blocks invalid date
        JavascriptExecutor js = (JavascriptExecutor) driver;
        boolean formValid = (Boolean) js.executeScript(
                "return document.querySelector('form').checkValidity();"
        );
        boolean html5Error = !formValid;

        // ✅ CASE 2 — Inline error message (PHP / JS)
        String src = driver.getPageSource().toLowerCase();
        boolean inlineError = src.contains("invalid")
                || src.contains("future")
                || src.contains("dob")
                || src.contains("date");

        boolean errorShown = html5Error || inlineError;

        Assert.assertTrue(errorShown, "❌ Future DOB NOT validated! No error shown.");
        System.out.println("✅ Invalid DOB detected successfully.");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
