package orcun.balcilar.selenium.support.pagefactory;

import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

@ScenarioScope
@Component
public class Content extends WebComponentImpl {
  @FindBy(css = ".input-button__input")
  private WebElement input;

  public void clear() {
    input.clear();
  }

  public void sendKeys(String keys) {
    input.sendKeys(keys);
  }
}
