package integration.test.pojo;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;

import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponentLocatorFactory;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmptyTest {
  @Test
  public void test() {
    WebDriver driver = WebDriverManager.firefoxdriver().create();
    driver.manage().window().maximize();
    driver.get("https://www.ryanair.com/tr/en");
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(
                    "#cookie-popup-with-overlay > div > div.cookie-popup-with-overlay__buttons > button.cookie-popup-with-overlay__button")))
        .click();
    WebComponentLocatorFactory factory = new WebComponentLocatorFactory(driver, 30);
    HomePage homePage = new HomePage(driver, factory);
    homePage.selectToPort("JFK");
  }
}
