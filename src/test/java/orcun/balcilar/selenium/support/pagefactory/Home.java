package orcun.balcilar.selenium.support.pagefactory;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class Home extends WebComponent {
  @FindBy(css = ".searchbox")
  private WebElement room;
}
