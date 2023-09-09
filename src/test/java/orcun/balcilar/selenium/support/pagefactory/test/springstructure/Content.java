package orcun.balcilar.selenium.support.pagefactory.test.springstructure;

import orcun.balcilar.selenium.support.pagefactory.PageScoped;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value = "prototype")
@Component
public class Content extends orcun.balcilar.selenium.support.pagefactory.SpringWebComponent {

  @FindBy(css = ".input-button__input")
  private WebElement input;

  @PageScoped
  @FindBy(css = ".home")
  private Home home;

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