package integration.springstructure;

import io.github.orcunbalcilar.selenium.support.pagefactory.PageScoped;
import io.github.orcunbalcilar.selenium.support.pagefactory.SpringWebComponent;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value = "prototype")
@Component
public class Content extends SpringWebComponent {

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
