package orcun.balcilar.selenium.support.pagefactory;

import io.cucumber.spring.ScenarioScope;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ScenarioScope
@Component
public class HomePage {

  @Autowired private Browser browser;

  @FindBy(css = "#input-button__departure")
  private WebElement fromPortArea;

  @FindBy(css = ".flight-widget-controls__control--destination-airport-container.ng-tns-c52-4")
  private Row row;

  @PostConstruct
  private void init() {
    WebDriver driver = browser.getDriver();
    PageFactory.initElements(
        new NestedElementFieldDecorator(driver, new NestedElementLocatorFactory(driver, 30)), this);
  }

  public void selectFromPort(String port) throws InterruptedException {
    System.out.println(port);
    row.selectFromPort(port);
  }

  public void selectToPort(String port) throws InterruptedException {
    System.out.println(port);
    fromPortArea.click();
    fromPortArea.clear();
    fromPortArea.sendKeys(port);
  }
}
