package orcun.balcilar.selenium.support.pagefactory.test.pojo;

import orcun.balcilar.selenium.support.pagefactory.PageScoped;
import orcun.balcilar.selenium.support.pagefactory.WebComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Content extends WebComponent {

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
