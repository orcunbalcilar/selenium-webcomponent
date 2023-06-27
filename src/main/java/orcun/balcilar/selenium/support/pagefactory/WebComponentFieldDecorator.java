package orcun.balcilar.selenium.support.pagefactory;

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
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class WebComponentFieldDecorator extends DefaultFieldDecorator {

  protected final WebDriver driver;

  public WebComponentFieldDecorator(WebDriver driver, WebComponentLocatorFactory factory) {
    super(factory);
    this.driver = driver;
  }

  @Override
  public Object decorate(ClassLoader loader, Field field) {
    if (!(WebElement.class.isAssignableFrom(field.getType())
        || isDecoratableWebComponent(field)
        || isDecoratableList(field))) {
      return null;
    }

    ElementLocator locator = factory.createLocator(field);
    if (locator == null) {
      return null;
    }

    if (WebElement.class.isAssignableFrom(field.getType())) {
      return proxyForLocatorByScope(loader, locator, field);
    } else if (WebComponent.class.isAssignableFrom(field.getType())) {
      return proxyForWebComponent(loader, locator, field);
    } else if (List.class.isAssignableFrom(field.getType())) {
      return getListType(field) instanceof WebElement
          ? proxyForListLocatorByScope(loader, locator, field)
          : proxyForListWebComponent(loader, locator, field);
    } else {
      return null;
    }
  }

  private Type getListType(Field field) {
    return ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
  }

  @SuppressWarnings("unchecked")
  protected WebComponent proxyForWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    WebComponent webComponent = createInstance((Class<? extends WebComponent>) field.getType());
    WebElement proxyWebElement = proxyForLocatorByScope(loader, locator, field);
    webComponent.setSearchContext(proxyWebElement);
    initWebComponentElements(proxyWebElement, webComponent);
    return webComponent;
  }

  protected List<? extends WebComponent> proxyForListWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    List<WebElement> elements = proxyForListLocatorByScope(loader, locator, field);
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
    return WebComponent.class.isAssignableFrom(field.getType()) && hasFindByAnnotation(field);
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

    Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

    // isInstance used to support for inner classes
    if (!listType.getClass().isInstance(WebElement.class)
        || !listType.getClass().isInstance(WebComponent.class)) {
      return false;
    }

    return hasFindByAnnotation(field);
  }

  private boolean hasFindByAnnotation(Field field) {
    return field.getAnnotation(FindBy.class) != null
        || field.getAnnotation(FindBys.class) != null
        || field.getAnnotation(FindAll.class) != null;
  }

  protected final WebElement proxyForLocatorByScope(
      ClassLoader loader, ElementLocator locator, Field field) {
    return hasPageScopeAnnotation(field)
        ? proxyForLocator(loader, new DefaultElementLocatorFactory(driver).createLocator(field))
        : proxyForLocator(loader, locator);
  }

  protected final List<WebElement> proxyForListLocatorByScope(
      ClassLoader loader, ElementLocator locator, Field field) {
    return hasPageScopeAnnotation(field)
        ? proxyForListLocator(loader, new DefaultElementLocatorFactory(driver).createLocator(field))
        : proxyForListLocator(loader, locator);
  }

  private boolean hasPageScopeAnnotation(Field field) {
    return field.isAnnotationPresent(PageScoped.class);
  }
}
