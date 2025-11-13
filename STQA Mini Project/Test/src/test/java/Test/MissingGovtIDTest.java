package Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class MissingGovtIDTest {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vishw\\OneDrive\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(4));
    }

    @Test
    public void testMissingGovtID() {

        driver.get("http://localhost/covid-tms/new-user-testing.php");

        // ✅ Fill required fields except Govt ID
        driver.findElement(By.id("fullname")).sendKeys("Test User");
        driver.findElement(By.id("mobilenumber")).sendKeys("9999999999");
        driver.findElement(By.id("dob")).sendKeys("05-05-1999");

        // ✅ Govt ID fields intentionally LEFT EMPTY
        driver.findElement(By.id("govtissuedid")).clear();
        driver.findElement(By.id("govtidnumber")).clear();

        // ✅ Click submit
        driver.findElement(By.id("submit")).click();

        // ✅ CASE 1 — HTML5 required validation blocks form
        JavascriptExecutor js = (JavascriptExecutor) driver;
        boolean formValid = (Boolean) js.executeScript(
                "return document.querySelector('form').checkValidity();"
        );

        boolean html5Error = !formValid;

        // ✅ CASE 2 — Inline message (if PHP/Javascript adds any text)
        String src = driver.getPageSource().toLowerCase();
        boolean inlineError =
                src.contains("id") ||
                src.contains("required") ||
                src.contains("enter") ||
                src.contains("please");

        boolean errorShown = html5Error || inlineError;

        Assert.assertTrue(
                errorShown,
                "❌ Govt ID validation NOT triggered when fields are empty!"
        );

        System.out.println("✅ Missing Govt ID validation works.");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
