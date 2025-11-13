package Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class DuplicateUserRegistrationTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @Test
    public void testDuplicateRegistration() {

        driver.get("http://localhost/covid-tms/new-user-testing.php");

        // ---- Fill Personal Information ----
        driver.findElement(By.id("fullname")).clear();
        driver.findElement(By.id("fullname")).sendKeys("Vishwajeet Londhe");

        driver.findElement(By.id("mobilenumber")).clear();
        driver.findElement(By.id("mobilenumber")).sendKeys("8459652020");
        driver.findElement(By.id("mobilenumber")).sendKeys(Keys.TAB); // triggers instant validation

        // ---- Wait FAST for duplicate message ----
        try {
            WebElement dupMsg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(),'Mobile already exists') or contains(text(),'available for Registration')]")
                    )
            );

            String msg = dupMsg.getText().toLowerCase();
            boolean isDuplicate = msg.contains("already exists") || msg.contains("duplicate");

            Assert.assertTrue(isDuplicate, "❌ Duplicate mobile was not detected!");
            System.out.println("✅ Duplicate user correctly blocked.");
            return; // stop test here
        } catch (Exception ignored) {
            // continue filling if not duplicate yet
        }

        // ---- Continue filling only if not duplicate ----
        driver.findElement(By.id("dob")).clear();
        driver.findElement(By.id("dob")).sendKeys("01-01-2004");

        driver.findElement(By.id("govtissuedid")).clear();
        driver.findElement(By.id("govtissuedid")).sendKeys("Adhar");

        driver.findElement(By.id("govtidnumber")).clear();
        driver.findElement(By.id("govtidnumber")).sendKeys("123456789012");

        driver.findElement(By.id("address")).clear();
        driver.findElement(By.id("address")).sendKeys("Pune");

        driver.findElement(By.id("state")).clear();
        driver.findElement(By.id("state")).sendKeys("Maharashtra");

        // Select Test Type = Antigen
        Select testType = new Select(driver.findElement(By.id("testtype")));
        testType.selectByVisibleText("Antigen");

        // Time Slot
        driver.findElement(By.id("timeslot")).clear();
        driver.findElement(By.id("timeslot")).sendKeys("12-11-2025 09:00 AM");

        // Submit
        driver.findElement(By.id("submit")).click();

        // Final duplicate check after submit
        WebElement dupFinal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'already exists') or contains(text(),'duplicate')]")
        ));

        Assert.assertTrue(dupFinal.isDisplayed(), "❌ Duplicate user registration not blocked!");
        System.out.println("✅ Duplicate detected after submit.");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
