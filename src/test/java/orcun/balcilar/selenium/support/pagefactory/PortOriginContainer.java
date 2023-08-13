package orcun.balcilar.selenium.support.pagefactory;

import io.cucumber.spring.ScenarioScope;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class PortOriginContainer extends WebComponent {
  private WebElement selectedPort;
}
