package integration.test.pojo;

import io.github.orcunbalcilar.selenium.support.pagefactory.PageScoped;
import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponent;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Content extends WebComponent {

  @FindBy(css = ".input-button__input")
  private WebElement input;

  @FindBy(css = ".input-button__label.body-m-lg.body-m-sm")
  private List<WebElement> fakeList;

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
