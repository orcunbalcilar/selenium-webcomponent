package integration.springstructure;

import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Home extends WebComponent {
  @FindBy(css = ".searchbox")
  private WebElement room;
}
