package orcun.balcilar.selenium.support.pagefactory;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Page implements SpringBeanElementInitializer {
  @Autowired protected Browser browser;
  @PostConstruct
  public void init() {
    initElements(browser.getDriver(), new WebComponentLocatorFactory(browser.getDriver(), 30));
  }
}
