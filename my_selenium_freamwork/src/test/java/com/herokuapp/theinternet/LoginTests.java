package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LoginTests {

	private static WebDriver driver;

	@Parameters({ "browser" })
	@BeforeClass(alwaysRun = true)
	static void setupAll(@Optional String browser) {
		switch (browser) {
		case "chrome": 
			//Create Chrome driver
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			break;
			
		case"firefox":
			//Create Firefox driver
			WebDriverManager.firefoxdriver().setup(); 
			driver = new FirefoxDriver();
			break;
			
		default:
			System.out.println("Since browser was not selected properly, for the next test instance Edge browser was selected by default");
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;
		}
	}

	@AfterTest
	private static void teardown() {
		driver.quit();
	}

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLoginTest() {
		System.out.println("Test started");
		System.out.println("Browser is opened");
		sleep(1);

		// open test page
		String url = "https://the-internet.herokuapp.com/login";
		driver.get(url);
		sleep(1);

		driver.manage().window().maximize();

		sleep(1);

		System.out.println("Page is opened");
		sleep(2);

		// enter username
		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");

		// enter password
		WebElement password = driver.findElement(By.name("password"));
		password.sendKeys("SuperSecretPassword!");

		sleep(4);
		// click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

		sleep(4);
		// verifications:
		// -new url
		String expectedURL = "https://the-internet.herokuapp.com/secure";
		String actualURL = driver.getCurrentUrl();
		Assert.assertEquals(actualURL, expectedURL, "Actual page url is not the same as expected");

		// -logout button is visible
		WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
//		WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius broken']"));
		Assert.assertTrue(logOutButton.isDisplayed(), "Log out button is not displayed");

		// -succesful login msg
//		WebElement successMessage = driver.findElement(By.cssSelector("#flash"));
		WebElement successMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String expectedMessage = "You logged into a secure area!";
		String actualMessage = successMessage.getText();

//		Assert.assertEquals(actualMessage, expectedMessage,"Actual message is not the same as expected");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not the contain expected.\nActual Message: " + actualMessage
						+ "\n Expected Message: " + expectedMessage);

		System.out.println("Test finished");
		driver.quit();
	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLoginTest(String username, String password, String expectedMessage) {
		System.out.println("Test started");

		System.out.println("Browser is opened");
		sleep(1);

		// open test page
		String url = "https://the-internet.herokuapp.com/login";
		driver.get(url);
		sleep(1);

		driver.manage().window().maximize();

		sleep(1);

		System.out.println("Page is opened");
		sleep(2);

		// enter wrong username
		WebElement usernameElement = driver.findElement(By.id("username"));
		usernameElement.sendKeys(username);

		// enter password
		WebElement passwordElement = driver.findElement(By.name("password"));
		passwordElement.sendKeys(password);

		sleep(4);
		// click login button
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

		sleep(4);
		// verifications:
		// -same url
		String expectedURL = "https://the-internet.herokuapp.com/login";
		String actualURL = driver.getCurrentUrl();
		Assert.assertEquals(actualURL, expectedURL, "Actual page url is not the same as expected");

		// -invalid login msg
		WebElement errorMessage = driver.findElement(By.xpath("//div[@id='flash']"));
//		String expectedMessage = "Your username is invalid!";
		String actualMessage = errorMessage.getText();

		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message does not the contain expected.\nActual Message: " + actualMessage
						+ "\n Expected Error Message: " + expectedMessage);

		System.out.println("Invalid username message was displayed");

		System.out.println("Test finished");
		driver.quit();
	}

	/**
	 * Stop executions for given amount of time
	 * 
	 * @param seconds
	 */
	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
