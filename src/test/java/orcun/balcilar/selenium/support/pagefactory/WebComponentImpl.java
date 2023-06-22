package orcun.balcilar.selenium.support.pagefactory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class WebComponentImpl extends WebComponent implements ApplicationContextAware {

  @Autowired protected Browser browser;

  @Value("${timeout}")
  private int timeout;

  private ApplicationContext applicationContext;

  /*@PostConstruct
  private void init() {
    PageFactory.initElements(createFieldDecorator(), this);
  }

  private AutowiredNestedElementFieldDecorator createFieldDecorator() {
    System.out.println("timeout " + timeout);
    AutowiredNestedElementFieldDecorator fieldDecorator =
        applicationContext.getBean(
            AutowiredNestedElementFieldDecorator.class,
            browser.getDriver(),
            new NestedElementLocatorFactory(getSearchContext(), timeout),
            this);
    applicationContext.getAutowireCapableBeanFactory().autowireBean(fieldDecorator);
    return fieldDecorator;
  }*/

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
