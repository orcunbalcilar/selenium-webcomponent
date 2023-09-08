package orcun.balcilar.selenium.support.pagefactory.test.pojo;

import java.util.List;
import orcun.balcilar.selenium.support.pagefactory.WebComponentFieldDecorator;
import orcun.balcilar.selenium.support.pagefactory.WebComponentLocatorFactory;
import orcun.balcilar.selenium.support.pagefactory.test.springstructure.*;
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
    System.out.println("heyoo");
    rows.get(0).enterPort(port);
  }

  public void selectToPort(String port) {
    rows.get(1).enterPort(port);
  }
}
