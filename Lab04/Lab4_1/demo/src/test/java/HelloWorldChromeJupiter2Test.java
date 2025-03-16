
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.remote.DesiredCapabilities;

@ExtendWith(SeleniumJupiter.class)
class HelloWorldChromeJupiter2Test {
    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Test
    void test(ChromeDriver driver) {
        driver.get("https://www.betclic.pt/");
        assertThat(driver.getTitle()).contains("Apostas");

        WebElement cookiesButton = driver.findElement(By.id("popin_tc_privacy_button_2"));
        cookiesButton.click();
    }

}