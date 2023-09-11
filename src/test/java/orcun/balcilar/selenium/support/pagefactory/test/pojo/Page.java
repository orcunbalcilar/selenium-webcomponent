package orcun.balcilar.selenium.support.pagefactory.test.pojo;

import orcun.balcilar.selenium.support.pagefactory.WebComponentLocatorFactory;
import org.openqa.selenium.WebDriver;

public abstract class Page {

  private final WebDriver driver;
  private final WebComponentLocatorFactory factory;

  public Page(WebDriver driver, WebComponentLocatorFactory factory) {
    this.driver = driver;
    this.factory = factory;
  }
}
