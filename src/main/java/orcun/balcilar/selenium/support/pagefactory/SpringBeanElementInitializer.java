package orcun.balcilar.selenium.support.pagefactory;

import java.util.Arrays;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.springframework.aop.support.AopUtils;

public interface SpringBeanElementInitializer {
  default void initElements(WebDriver driver, WebComponentLocatorFactory factory) {
    SpringWebComponentFieldDecorator decorator =
        new SpringWebComponentFieldDecorator(driver, factory);
    PageFactory.initElements(decorator, this);
    Arrays.stream(AopUtils.getTargetClass(this).getDeclaredFields())
        .filter(decorator::isDecoratableSpringWebComponent)
        .forEach(
            field -> {
              WebElement webElement =
                  decorator.proxyForLocatorByScope(
                      this.getClass().getClassLoader(),
                      decorator.getFactory().createLocator(field),
                      field);
              try {
                field.setAccessible(true);
                SpringWebComponent springWebComponent = (SpringWebComponent) field.get(this);
                springWebComponent.setSearchContext(webElement);
                WebComponentLocatorFactory locatorFactory =
                    new WebComponentLocatorFactory(
                        webElement,
                        decorator.getFactory().getTimeOutInSeconds(),
                        decorator.getFactory().getExceptions());
                springWebComponent.initElements(decorator.getDriver(), locatorFactory);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            });
  }
}
