package integration.test.springstructure;

import io.cucumber.spring.ScenarioScope;
import io.github.orcunbalcilar.selenium.support.pagefactory.SpringWebComponent;
import lombok.Getter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
@ScenarioScope
public class PortOriginContainer extends SpringWebComponent {
  private WebElement selectedPort;

  @Autowired
  @FindBy(css = ".input-button__content")
  private Content content;
}
