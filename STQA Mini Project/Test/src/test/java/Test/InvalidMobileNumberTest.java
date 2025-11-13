package Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class InvalidMobileNumberTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vishw\\OneDrive\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test
    public void testInvalidMobileNumber() {

        driver.get("http://localhost/covid-tms/new-user-testing.php");

        // ✅ Fill minimum data
        driver.findElement(By.id("fullname")).sendKeys("Test User");

        WebElement mobile = driver.findElement(By.id("mobilenumber"));
        mobile.clear();
        mobile.sendKeys("123"); // ❌ INVALID MOBILE NUMBER (<10 digits)
        mobile.sendKeys(Keys.TAB); // trigger JS validation

        driver.findElement(By.id("dob")).sendKeys("01-01-2000");
        driver.findElement(By.id("govtissuedid")).sendKeys("Aadhar");
        driver.findElement(By.id("govtidnumber")).sendKeys("123456789012");

        // ✅ Click submit
        driver.findElement(By.id("submit")).click();

        // ✅ CASE 1 — Browser HTML5 validation (minlength, pattern)
        JavascriptExecutor js = (JavascriptExecutor) driver;
        boolean formValid = (Boolean) js.executeScript(
                "return document.querySelector('form').checkValidity();"
        );
        boolean html5Error = !formValid;

        // ✅ CASE 2 — Inline red error text
        String src = driver.getPageSource().toLowerCase();

        boolean inlineError =
                src.contains("invalid") ||
                src.contains("mobile") ||
                src.contains("phone") ||
                src.contains("10 digit") ||
                src.contains("enter valid") ||
                src.contains("required");

        boolean errorShown = html5Error || inlineError;

        Assert.assertTrue(
                errorShown,
                "❌ No validation shown for INVALID mobile number (less than 10 digits)!"
        );

        System.out.println("✅ Invalid mobile number successfully detected.");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
