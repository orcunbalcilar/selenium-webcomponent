package orcun.balcilar.selenium.support.pagefactory;

import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(value = "prototype")
@Component
public class Row extends SpringWebComponent {

  @Autowired
  @FindBy(css = ".input-button__content")
  private Content content;

  public void enterPort(String port) {
    content.click();
    content.clear();
    content.sendKeys(port);
  }
}
