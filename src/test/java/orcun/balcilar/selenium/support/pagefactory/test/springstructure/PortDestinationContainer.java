package orcun.balcilar.selenium.support.pagefactory.test.springstructure;

import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class PortDestinationContainer
    extends orcun.balcilar.selenium.support.pagefactory.SpringWebComponent {
  private WebElement selectedPort;
}
