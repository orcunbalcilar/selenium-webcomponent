package integration.test.pojo;

import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponentFieldDecorator;
import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponentLocatorFactory;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends Page {

  private WebElement abc;

  @FindBy(css = "fsw-input-button")
  private List<Row> rows;

  @FindBy(css = ".home")
  private Home home;

  public HomePage(WebDriver driver, WebComponentLocatorFactory factory) {
    super(driver, factory);
    PageFactory.initElements(new WebComponentFieldDecorator(driver, factory), this);
  }

  public void selectFromPort(String port) {
    rows.get(0).enterPort(port);
  }

  public void selectToPort(String port) {
    rows.get(1).enterPort(port);
  }
}
