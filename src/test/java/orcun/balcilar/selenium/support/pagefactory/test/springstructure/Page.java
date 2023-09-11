package orcun.balcilar.selenium.support.pagefactory.test.springstructure;

import javax.annotation.PostConstruct;
import orcun.balcilar.selenium.support.pagefactory.SpringBeanElementInitializer;
import orcun.balcilar.selenium.support.pagefactory.WebComponentLocatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class Page implements SpringBeanElementInitializer {
  @Autowired protected Browser browser;
  @Autowired protected ApplicationContext applicationContext;

  @PostConstruct
  public void init() {
    initElements(browser.getDriver(), new WebComponentLocatorFactory(browser.getDriver(), 30));
  }

  @Override
  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
