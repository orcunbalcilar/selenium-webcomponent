package orcun.balcilar.selenium.support.pagefactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class Content extends WebComponentImpl {

  @FindBy(css = ".input-button__input")
  private WebElement input;

  public void click() {
    input.click();
  }

  public void clear() {
    input.clear();
  }

  public void sendKeys(String keys) {
    input.sendKeys(keys);
  }
}
