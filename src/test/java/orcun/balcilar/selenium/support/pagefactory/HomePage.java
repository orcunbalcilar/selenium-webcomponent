package orcun.balcilar.selenium.support.pagefactory;

import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class HomePage extends Page {

  @Autowired
  @FindBy(css = "fsw-input-button[uniqueid='departure']")
  private Row departureRow;

  @Autowired
  @FindBy(css = "fsw-input-button[uniqueid='destination']")
  private Row arrivalRow;

  @Autowired
  @FindBy(css = "fsw-origin-container")
  private PortOriginContainer portOriginContainer;

  @Autowired
  @FindBy(css = "fsw-destination-container")
  private PortDestinationContainer portDestinationContainer;

  @FindBy(css = ".home")
  private Home home;

  public void selectFromPort(String port) {departureRow.enterPort(port);}

  public void selectToPort(String port) {
    arrivalRow.enterPort(port);
  }
}
