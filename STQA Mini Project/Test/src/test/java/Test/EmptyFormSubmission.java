package Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class EmptyFormSubmission {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vishw\\OneDrive\\Desktop\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
    }

    @Test
    public void testEmptyFormSubmission() {

        driver.get("http://localhost/covid-tms/new-user-testing.php");

        WebElement submitBtn = driver.findElement(By.id("submit"));

        // ✅ HTML5 "required" validation is client-side (browser popup balloon)
        // Trick: use JavaScript to check the validity of fields
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isFormValid = (Boolean) js.executeScript(
                "return document.querySelector('form').checkValidity();"
        );

        // Click the submit button
        submitBtn.click();

        // ✅ Now check 3 possible validation outcomes:

        boolean html5Error = !isFormValid; // form invalid → required fields empty

        boolean inlineError = driver.getPageSource().contains("required")
                || driver.getPageSource().contains("Please fill out")
                || driver.getPageSource().contains("All fields are required")
                || driver.getPageSource().contains("Please enter");

        // ✅ Any one type of validation must occur
        boolean isErrorDisplayed = html5Error || inlineError;

        Assert.assertTrue(isErrorDisplayed,
                "❌ No error shown when submitting an empty form!");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }
}
