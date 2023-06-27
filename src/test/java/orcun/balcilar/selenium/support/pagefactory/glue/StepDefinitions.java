package orcun.balcilar.selenium.support.pagefactory.glue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import java.time.Duration;
import orcun.balcilar.selenium.support.pagefactory.Browser;
import orcun.balcilar.selenium.support.pagefactory.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

public class StepDefinitions {

  @Autowired private Browser browser;
  @Autowired private HomePage homePage;

  @Given("I am on the page {string}")
  public void iAmOnThePage(String url) {
    browser.get(url);
    WebDriverWait wait = new WebDriverWait(browser.getDriver(), Duration.ofSeconds(30));
    wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(
                    "#cookie-popup-with-overlay > div > div.cookie-popup-with-overlay__buttons > button.cookie-popup-with-overlay__button")))
        .click();
  }

  @When("I select the origin {string}")
  public void iSelectTheDestination(String origin) {
    homePage.selectFromPort(origin);
  }

  @When("I select the destination {string}")
  public void iSelectTheOrigin(String destination) {
    homePage.selectToPort(destination);
  }
}
