package tqs.reservation.demo;

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
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

public class SeleniumTestCase {
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

  @AfterEach
    void teardown() {
        driver.quit();
    }

  @Test
  public void makeReservation() throws Exception {
    driver.get("http://127.0.0.1:5500/Frontend/index.html");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Cantina Universitária de Santiago'])[1]/following::button[1]")).click();
    TimeUnit.SECONDS.sleep(1);
    driver.findElement(By.id("day")).click();
    driver.findElement(By.id("day")).sendKeys("11-04-2025");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Select Day:'])[1]/following::button[1]")).click();
    TimeUnit.SECONDS.sleep(1);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Number of Meals:'])[1]/following::button[1]")).click();
    TimeUnit.SECONDS.sleep(1);
    driver.findElement(By.id("customerName")).click();
    driver.findElement(By.id("customerName")).clear();
    driver.findElement(By.id("customerName")).sendKeys("a");
    driver.findElement(By.id("customerEmail")).click();
    driver.findElement(By.id("customerEmail")).clear();
    driver.findElement(By.id("customerEmail")).sendKeys("a@gmail");
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Cancel'])[1]/following::button[1]")).click();
    TimeUnit.SECONDS.sleep(1);
    String code = driver.findElement(By.xpath("/html/body/div[2]/div/div[4]/p/strong")).getText();
    assertThat(code!=null).isTrue();
    System.out.println("Reservation code: " + code);
    System.setProperty("reservationCode", code);
  }

  @Test
  public void seeReservation() throws Exception{
    driver.get("http://127.0.0.1:5500/Frontend/index.html");
    driver.findElement(By.linkText("Go to Reservation Status")).click();
    TimeUnit.SECONDS.sleep(1);
    driver.get("http://127.0.0.1:5500/Frontend/reservationStatus.html");
    driver.findElement(By.id("code")).click();
    driver.findElement(By.id("code")).clear();
    driver.findElement(By.id("code")).sendKeys(System.getProperty("reservationCode"));
    TimeUnit.SECONDS.sleep(1);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Reservation Code:'])[1]/following::button[1]")).click();
    TimeUnit.SECONDS.sleep(1);
    //assertThat(driver.findElement(By.xpath("/html/body/div[2]/div/p[1]/span")).getText()).contains("PENDING");
  }

  @Test
  public void confirmReservation() throws Exception {
    driver.get("http://127.0.0.1:5500/Frontend/index.html");
    driver.findElement(By.linkText("Go to Coordinator Page")).click();
    TimeUnit.SECONDS.sleep(1);
    driver.get("http://127.0.0.1:5500/Frontend/coordinatorPage.html");
    new Select(driver.findElement(By.id("restaurantId"))).selectByVisibleText("Cantina Universitária de Santiago");
    driver.findElement(By.id("reservationDate")).click();
    driver.findElement(By.id("reservationDate")).clear();
    driver.findElement(By.id("reservationDate")).sendKeys("11-04-2025");
    TimeUnit.SECONDS.sleep(1);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Select Date:'])[1]/following::button[1]")).click();
    TimeUnit.SECONDS.sleep(2);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Cantina Universitária de Santiago'])[3]/following::button[1]")).click();
    TimeUnit.SECONDS.sleep(1);
    driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Confirm'])[2]/following::button[1]")).click();
  }

  
}
