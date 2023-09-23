package unit;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TestPage {
  private WebElement webElement;

  @FindBy(css = ".test-web-component")
  private TestWebComponent webComponent;
}
