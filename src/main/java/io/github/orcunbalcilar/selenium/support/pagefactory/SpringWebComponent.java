package io.github.orcunbalcilar.selenium.support.pagefactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class SpringWebComponent extends WebComponent
    implements SpringBeanElementInitializer {
  @Autowired private ApplicationContext applicationContext;

  @Override
  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }
}
