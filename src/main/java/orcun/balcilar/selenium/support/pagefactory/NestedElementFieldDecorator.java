package orcun.balcilar.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class NestedElementFieldDecorator extends DefaultFieldDecorator {

  private final WebDriver driver;

  public NestedElementFieldDecorator(WebDriver driver, NestedElementLocatorFactory factory) {
    super(factory);
    this.driver = driver;
  }

  @Override
  public Object decorate(ClassLoader loader, Field field) {
    if (!(Stream.of(WebElement.class, WebComponent.class)
            .anyMatch(c -> c.isAssignableFrom(field.getType()))
        || isDecoratableList(field) || isDecoratableListWebComponent(field))) {
      return null;
    }

    ElementLocator locator = factory.createLocator(field);
    if (locator == null) {
      return null;
    }

    if (WebElement.class.isAssignableFrom(field.getType())) {
      return proxyForLocatorByScope(loader, locator, field);
    } else if (isDecoratableList(field)) {
      return proxyForListLocatorByScope(loader, locator, field);
    } else if (isDecoratableWebComponent(field)) {
      return proxyForWebComponent(loader, locator, field);
    } else if (isDecoratableListWebComponent(field)) {
      return proxyForListWebComponent(loader, locator, field);
    } else {
      return null;
    }
  }

  protected WebComponent proxyForWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    WebComponent webComponent = createInstance(field.getType());
    WebElement proxyWebElement = proxyForLocatorByScope(loader, locator, field);
    webComponent.setSearchContext(proxyWebElement);
    initWebComponentElements(proxyWebElement, webComponent);
    return webComponent;
  }

  protected List<? extends WebComponent> proxyForListWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    WebComponent listType =
        (WebComponent) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    List<WebElement> elements = proxyForListLocatorByScope(loader, locator, field);
    List<? extends WebComponent> webComponents =
        elements.stream()
            .map(e -> createInstance(listType.getClass()))
            .collect(Collectors.toList());
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

  private WebComponent createInstance(Class<?> runtimeClass) {
    try {
      return (WebComponent) runtimeClass.getConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | InvocationTargetException
        | NoSuchMethodException e) {
      throw new RuntimeException("Cannot create instance from : " + runtimeClass, e);
    }
  }

  private void initWebComponentElements(WebElement proxyWebElement, WebComponent webComponent) {
    PageFactory.initElements(
        new NestedElementFieldDecorator(
            driver,
            new NestedElementLocatorFactory(
                proxyWebElement,
                ((NestedElementLocatorFactory) factory).getTimeOutInSeconds(),
                ((NestedElementLocatorFactory) factory).getExceptions())),
        webComponent);
  }

  protected boolean isDecoratableWebComponent(Field field) {
    return WebComponent.class.isAssignableFrom(field.getType()) && hasFindByAnnotation(field);
  }

  protected boolean isDecoratableListWebComponent(Field field) {
    if (!field.getType().equals(List.class)) {
      return false;
    }

    // Type erasure in Java isn't complete. Attempt to discover the generic
    // type of the list.
    Type genericType = field.getGenericType();
    if (!(genericType instanceof ParameterizedType)) {
      return false;
    }

    Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

    // isInstance used to support for inner classes
    if (!listType.getClass().isInstance(WebComponent.class)) {
      return false;
    }

    return hasFindByAnnotation(field);
  }

  private boolean hasFindByAnnotation(Field field) {
    return field.getAnnotation(FindBy.class) != null
        || field.getAnnotation(FindBys.class) != null
        || field.getAnnotation(FindAll.class) != null;
  }

  private WebElement proxyForLocatorByScope(
      ClassLoader loader, ElementLocator locator, Field field) {
    return hasPageScopeAnnotation(field)
        ? proxyForLocator(loader, new DefaultElementLocatorFactory(driver).createLocator(field))
        : proxyForLocator(loader, locator);
  }

  private List<WebElement> proxyForListLocatorByScope(
      ClassLoader loader, ElementLocator locator, Field field) {
    return hasPageScopeAnnotation(field)
        ? proxyForListLocator(loader, new DefaultElementLocatorFactory(driver).createLocator(field))
        : proxyForListLocator(loader, locator);
  }

  private boolean hasPageScopeAnnotation(Field field) {
    return Arrays.stream(field.getDeclaredAnnotations()).anyMatch(a -> a instanceof PageScope);
  }
}
