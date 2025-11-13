package Test;



import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
 
public class SignUpTest {
public WebDriver driver;
  @Test
  public void Signup() {
	  // For Open Website
driver.get("http://localhost/covid-tms/new-user-testing.php");
     // Sending full name 
driver.findElement(By.id("fullname")).sendKeys("Vishwajeet Londhe");
     // Send phone to text field
driver.findElement(By.id("mobilenumber")).sendKeys("8459652020");
    //  dob
driver.findElement(By.id("dob")).sendKeys("01-01-2004");
 //  Govt Issued ID
driver.findElement(By.id("govtissuedid")).sendKeys("Adhar");
//  ID Number
driver.findElement(By.id("govtidnumber")).sendKeys("123456789012");
// address
driver.findElement(By.id("address")).sendKeys("pune");
// State
driver.findElement(By.id("state")).sendKeys("Maharashtra");

driver.findElement(By.id("testtype")).sendKeys("RT-PCR");

driver.findElement(By.id("birthdaytime")).sendKeys("22-12-2022 13:20");

driver.findElement(By.xpath("/html/body/div/div/div/div/form/div/div[2]/div/div[2]/div[3]/input")).click();
//String s=driver.getCurrentUrl();
  }
 
  @BeforeClass
  public void beforeClass() {
  
  System.setProperty("webdriver.gecko.driver", "\"C:\\Users\\vishw\\OneDrive\\Desktop\\chromedriver.exe\"");
  driver = new ChromeDriver();
  
  }
 
  @AfterClass
  public void afterClass() {
  driver.quit();
  }
 
}

