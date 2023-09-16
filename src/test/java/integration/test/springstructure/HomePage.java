package integration.test.springstructure;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class HomePage extends Page {

  private WebElement abc;

  @FindBy(css = ".fake-list")
  private List<WebElement> fakeList;

  @FindBy(css = "fsw-input-button")
  private List<Row> rows;

  @Autowired
  @FindBy(css = "fsw-origin-container")
  private PortOriginContainer portOriginContainer;

  @Autowired
  @FindBy(css = "fsw-destination-container")
  private PortDestinationContainer portDestinationContainer;

  @FindBy(css = ".home")
  private Home home;

  public void selectFromPort(String port) {
    rows.get(0).enterPort(port);
  }

  public void selectToPort(String port) {
    rows.get(1).enterPort(port);
  }
}
