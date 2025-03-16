package com.example.UntitledTestSuite;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.apache.commons.io.FileUtils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

public class UntitledTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  JavascriptExecutor js;

  @BeforeEach
  void setup() {
    ChromeOptions options = new ChromeOptions().setBinary("/usr/bin/brave-browser");
    driver = new ChromeDriver(options);
  }

  @Test
  public void testUntitledTestCase() throws Exception {
    driver.get("https://blazedemo.com/");
    new Select(driver.findElement(By.name("fromPort"))).selectByVisibleText("Portland");
    new Select(driver.findElement(By.name("toPort"))).selectByVisibleText("Berlin");
    driver.findElement(By.xpath("//input[@value='Find Flights']")).click();
    driver.findElement(By.xpath("//tr[3]/td/input")).click();
    driver.findElement(By.id("inputName")).click();
    driver.findElement(By.id("inputName")).clear();
    driver.findElement(By.id("inputName")).sendKeys("la");
    driver.findElement(By.id("address")).clear();
    driver.findElement(By.id("address")).sendKeys("la");
    driver.findElement(By.id("city")).clear();
    driver.findElement(By.id("city")).sendKeys("la");
    driver.findElement(By.id("state")).clear();
    driver.findElement(By.id("state")).sendKeys("al");
    driver.findElement(By.id("zipCode")).clear();
    driver.findElement(By.id("zipCode")).sendKeys("12");
    driver.findElement(By.id("creditCardNumber")).clear();
    driver.findElement(By.id("creditCardNumber")).sendKeys("1");
    driver.findElement(By.id("nameOnCard")).clear();
    driver.findElement(By.id("nameOnCard")).sendKeys("j");
    driver.findElement(By.xpath("//input[@value='Purchase Flight']")).click();
    assertThat(driver.getTitle()).contains("BlazeDemo Confirmation");

  }

  @AfterEach
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      throw new Exception(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
