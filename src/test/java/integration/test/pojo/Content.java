package integration.test.pojo;

import io.github.orcunbalcilar.selenium.support.pagefactory.PageScoped;
import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponent;
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
