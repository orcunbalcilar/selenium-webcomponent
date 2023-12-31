package integration.pojo;

import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponent;
import org.openqa.selenium.support.FindBy;

public class Row extends WebComponent {

  @FindBy(css = ".input-button__content")
  private Content content;

  public void enterPort(String port) {
    content.click();
    content.clear();
    content.sendKeys(port);
  }
}
