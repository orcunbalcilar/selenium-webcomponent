package integration.test.springstructure;

import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class PortOriginContainer
    extends orcun.balcilar.selenium.support.pagefactory.SpringWebComponent {
  private WebElement selectedPort;

  @Autowired
  @FindBy(css = ".input-button__content")
  private Content content;

  public WebElement getSelectedPort() {
    return selectedPort;
  }

  public Content getContent() {
    return content;
  }
}
