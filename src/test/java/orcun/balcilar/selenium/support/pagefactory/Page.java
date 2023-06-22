package orcun.balcilar.selenium.support.pagefactory;

import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class Page {
  @Autowired protected ApplicationContext applicationContext;

  @Autowired protected Browser browser;

  @PostConstruct
  private void init() {
    WebDriver driver = browser.getDriver();
    AutowiredNestedElementFieldDecorator fieldDecorator =
        applicationContext.getBean(
            AutowiredNestedElementFieldDecorator.class,
            driver,
            new NestedElementLocatorFactory(driver, 30),
            this);
    PageFactory.initElements(fieldDecorator, this);
  }
}
