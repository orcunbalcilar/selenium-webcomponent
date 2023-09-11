package integration.test.springstructure;

import io.cucumber.spring.ScenarioScope;
import io.github.orcunbalcilar.selenium.support.pagefactory.SpringWebComponent;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class PortDestinationContainer extends SpringWebComponent {
  private WebElement selectedPort;
}
