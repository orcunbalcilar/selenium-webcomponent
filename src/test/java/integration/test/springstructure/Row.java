package integration.test.springstructure;

import io.github.orcunbalcilar.selenium.support.pagefactory.SpringWebComponent;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class Row extends SpringWebComponent {

  @FindBy(css = ".fake-list")
  private List<WebElement> fakeList;

  @Autowired
  @FindBy(css = ".input-button__content")
  private Content content;

  public void enterPort(String port) {
    content.click();
    content.clear();
    content.sendKeys(port);
  }
}
