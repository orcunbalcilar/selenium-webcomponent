package io.github.orcunbalcilar.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;

/**
 * The SpringBeanElementInitializer interface provides a default method for initializing web
 * elements within a Spring bean using the PageFactory pattern and a custom
 * WebComponentFieldDecorator.
 */
public interface SpringBeanElementInitializer {
  default void initElements(WebDriver driver, WebComponentLocatorFactory factory) {
    SpringWebComponentFieldDecorator decorator =
        new SpringWebComponentFieldDecorator(driver, factory);
    PageFactory.initElements(decorator, this);
    Field[] fields = AopUtils.getTargetClass(this).getDeclaredFields();
    Arrays.stream(fields)
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
    Arrays.stream(fields)
        .filter(decorator::isDecoratableListSpringWebComponent)
        .forEach(
            field -> {
              List<WebElement> webElements =
                  decorator.proxyForListLocatorByScope(
                      this.getClass().getClassLoader(),
                      decorator.getFactory().createLocator(field),
                      field);
              Type t = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
              try {
                List<SpringWebComponent> c =
                    webElements.stream()
                        .map(
                            w -> {
                              SpringWebComponent s =
                                  (SpringWebComponent)
                                      getApplicationContext()
                                          .getAutowireCapableBeanFactory()
                                          .createBean((Class<?>) t);
                              getApplicationContext()
                                  .getAutowireCapableBeanFactory()
                                  .autowireBean(s);
                              try {
                                s.setSearchContext(w);
                                WebComponentLocatorFactory locatorFactory =
                                    new WebComponentLocatorFactory(
                                        w,
                                        decorator.getFactory().getTimeOutInSeconds(),
                                        decorator.getFactory().getExceptions());
                                s.initElements(decorator.getDriver(), locatorFactory);
                              } catch (Exception e) {
                                throw new RuntimeException(e);
                              }
                              return s;
                            })
                        .collect(Collectors.toList());
                field.setAccessible(true);
                field.set(this, c);
              } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
              }
            });
  }

  ApplicationContext getApplicationContext();
}
