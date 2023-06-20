package orcun.balcilar.selenium.support.pagefactory;

import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class WebComponentImpl extends WebComponent {

  @Autowired protected Browser browser;

  @PostConstruct
  private void init() {
    WebDriver driver = browser.getDriver();
    PageFactory.initElements(
        new NestedElementFieldDecorator(
            driver, new NestedElementLocatorFactory(getSearchContext(), 30)),
        this);
  }
}
