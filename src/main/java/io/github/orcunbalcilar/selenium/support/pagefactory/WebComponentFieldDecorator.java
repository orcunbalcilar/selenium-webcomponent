package io.github.orcunbalcilar.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;

/**
 * The WebComponentFieldDecorator class provides a default method for initializing web elements
 * within a web component using the PageFactory pattern and a custom WebComponentLocatorFactory.
 */
public class WebComponentFieldDecorator extends DefaultFieldDecorator {

  protected final WebDriver driver;

  public WebComponentFieldDecorator(WebDriver driver, WebComponentLocatorFactory factory) {
    super(factory);
    this.driver = driver;
  }

  @Override
  public Object decorate(ClassLoader loader, Field field) {
    if (!(WebElement.class.isAssignableFrom(field.getType())
        || (hasFindByAnnotation(field)
            && (isDecoratableWebComponent(field) || isDecoratableList(field))))) {
      return null;
    }

    ElementLocator locator = createLocatorByScope(field);
    if (locator == null) {
      return null;
    }

    if (WebElement.class.isAssignableFrom(field.getType())) {
      return proxyForLocator(loader, locator);
    } else if (WebComponent.class.isAssignableFrom(field.getType())) {
      return proxyForWebComponent(loader, locator, field);
    } else if (List.class.isAssignableFrom(field.getType())) {
      return WebElement.class.isAssignableFrom((Class<?>) getListType(field))
          ? proxyForListLocator(loader, locator)
          : proxyForListWebComponent(loader, locator, field);
    } else {
      return null;
    }
  }

  ElementLocator createLocatorByScope(Field field) {
    return ((WebComponentLocatorFactory) factory).createLocator(field, driver);
  }

  protected Type getListType(Field field) {
    return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
  }

  @SuppressWarnings("unchecked")
  protected WebComponent proxyForWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    WebComponent webComponent = createInstance((Class<? extends WebComponent>) field.getType());
    WebElement proxyWebElement = proxyForLocator(loader, locator);
    webComponent.setSearchContext(proxyWebElement);
    initWebComponentElements(proxyWebElement, webComponent);
    return webComponent;
  }

  protected List<? extends WebComponent> proxyForListWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    List<WebElement> elements = proxyForListLocator(loader, locator);
    Class<? extends WebComponent> runtimeClass = getTypeArgumentWebComponentClass(field);
    List<? extends WebComponent> webComponents =
        elements.stream().map(e -> createInstance(runtimeClass)).collect(Collectors.toList());
    IntStream.range(0, elements.size())
        .forEach(
            i -> {
              WebElement proxyWebElement = elements.get(i);
              WebComponent webComponent = webComponents.get(i);
              webComponent.setSearchContext(proxyWebElement);
              initWebComponentElements(proxyWebElement, webComponent);
            });
    return webComponents;
  }

  @SuppressWarnings("unchecked")
  protected Class<? extends WebComponent> getTypeArgumentWebComponentClass(Field field) {
    return (Class<? extends WebComponent>)
        ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
  }

  protected WebComponent createInstance(Class<? extends WebComponent> runtimeClass) {
    try {
      return runtimeClass.getConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException("Cannot create instance from : " + runtimeClass, e);
    }
  }

  protected void initWebComponentElements(WebElement proxyWebElement, WebComponent webComponent) {
    PageFactory.initElements(
        new WebComponentFieldDecorator(
            driver,
            new WebComponentLocatorFactory(
                proxyWebElement,
                ((WebComponentLocatorFactory) factory).getTimeOutInSeconds(),
                ((WebComponentLocatorFactory) factory).getExceptions())),
        webComponent);
  }

  protected boolean isDecoratableWebComponent(Field field) {
    return WebComponent.class.isAssignableFrom(field.getType());
  }

  @Override
  protected boolean isDecoratableList(Field field) {
    if (!field.getType().equals(List.class)) {
      return false;
    }

    // Type erasure in Java isn't complete. Attempt to discover the generic
    // type of the list.
    Type genericType = field.getGenericType();
    if (!(genericType instanceof ParameterizedType)) {
      return false;
    }

    Type listType = getListType(field);

    return WebElement.class.isAssignableFrom((Class<?>) listType)
        || WebComponent.class.isAssignableFrom((Class<?>) listType);
  }

  protected final boolean hasFindByAnnotation(Field field) {
    return field.getAnnotation(FindBy.class) != null
        || field.getAnnotation(FindBys.class) != null
        || field.getAnnotation(FindAll.class) != null;
  }

  @Override
  protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
    return super.proxyForLocator(loader, locator);
  }

  @Override
  protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator) {
    return super.proxyForListLocator(loader, locator);
  }
}
