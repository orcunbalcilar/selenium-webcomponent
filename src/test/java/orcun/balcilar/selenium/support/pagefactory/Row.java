package orcun.balcilar.selenium.support.pagefactory;

import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

@ScenarioScope
@Component
public class Row extends WebComponentImpl {
  @FindBy(css = "fsw-input-button")
  private WebElement fromPortArea;

  @FindBy(css = ".input-button__content")
  private Content content;

  public void selectFromPort(String port) throws InterruptedException {
    fromPortArea.click();
    content.clear();
    content.sendKeys(port);
    Thread.sleep(12000);
  }
}
