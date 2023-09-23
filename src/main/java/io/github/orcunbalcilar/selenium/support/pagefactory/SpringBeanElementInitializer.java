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
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
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
        .forEach(field -> initSpringWebComponent(decorator, field));
    Arrays.stream(fields)
        .filter(decorator::isDecoratableListSpringWebComponent)
        .forEach(field -> initListSpringWebComponent(decorator, field));
  }

  private void initSpringWebComponent(SpringWebComponentFieldDecorator decorator, Field field) {
    field.setAccessible(true);
    WebElement webElement =
        decorator.proxyForLocator(
            this.getClass().getClassLoader(), decorator.createLocatorByScope(field));
    try {
      SpringWebComponent springWebComponent = (SpringWebComponent) field.get(this);
      springWebComponent.setSearchContext(webElement);
      WebComponentLocatorFactory factory = decorator.getFactory();
      WebComponentLocatorFactory locatorFactory =
          new WebComponentLocatorFactory(
              webElement, factory.getTimeOutInSeconds(), factory.getExceptions());
      springWebComponent.initElements(decorator.getDriver(), locatorFactory);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private void initListSpringWebComponent(SpringWebComponentFieldDecorator decorator, Field field) {
    field.setAccessible(true);
    List<WebElement> webElements =
        decorator.proxyForListLocator(
            this.getClass().getClassLoader(), decorator.createLocatorByScope(field));
    List<SpringWebComponent> list = createList(webElements, field);
    for (int i = 0; i < webElements.size(); i++) {
      WebElement webElement = webElements.get(i);
      SpringWebComponent springWebComponent = list.get(i);
      springWebComponent.setSearchContext(webElement);
      WebComponentLocatorFactory factory = decorator.getFactory();
      WebComponentLocatorFactory locatorFactory =
          new WebComponentLocatorFactory(
              webElement, factory.getTimeOutInSeconds(), factory.getExceptions());
      springWebComponent.initElements(decorator.getDriver(), locatorFactory);
    }
    try {
      field.set(this, list);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private List<SpringWebComponent> createList(List<WebElement> webElements, Field field) {
    Type t = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    AutowireCapableBeanFactory beanFactory =
        getApplicationContext().getAutowireCapableBeanFactory();
    return webElements.stream()
        .map(
            webElement -> {
              SpringWebComponent s = (SpringWebComponent) beanFactory.createBean((Class<?>) t);
              beanFactory.autowireBean(s);
              return s;
            })
        .collect(Collectors.toList());
  }

  ApplicationContext getApplicationContext();
}
