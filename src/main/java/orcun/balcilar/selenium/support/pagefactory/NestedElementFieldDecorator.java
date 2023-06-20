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
        || isDecoratableListNestedContext(field)
        || isDecoratableList(field))) {
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
    } else if (isDecoratableNestedContext(field)) {
      return initNestedContext(loader, locator, field);
    } else if (isDecoratableListNestedContext(field)) {
      return initListNestedContext(loader, locator, field);
    } else {
      return null;
    }
  }

  protected WebComponent initNestedContext(
      ClassLoader loader, ElementLocator locator, Field field) {
    try {
      WebComponent context = (WebComponent) field.getType().getConstructor().newInstance();
      WebElement contextElement = proxyForLocatorByScope(loader, locator, field);
      context.setSearchContext(contextElement);
      PageFactory.initElements(
          new NestedElementFieldDecorator(
              driver,
              new NestedElementLocatorFactory(
                  contextElement, ((NestedElementLocatorFactory) factory).getTimeOutInSeconds())),
          context);
      return context;
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | InvocationTargetException e) {
      throw new RuntimeException("Cannot create instance from : " + field.getType(), e);
    }
  }

  @SuppressWarnings("unchecked")
  protected List<? extends WebComponent> initListNestedContext(
      ClassLoader loader, ElementLocator locator, Field field) {
    Type listType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    List<WebElement> elements = proxyForListLocatorByScope(loader, locator, field);
    List<? extends WebComponent> nestedContexts =
        elements.stream()
            .map(
                e -> {
                  try {
                    return (WebComponent)
                        ((Class<? extends WebComponent>) listType).getConstructor().newInstance();
                  } catch (InstantiationException
                      | IllegalAccessException
                      | InvocationTargetException
                      | NoSuchMethodException ex) {
                    throw new RuntimeException(
                        "Cannot create instance from : " + field.getType(), ex);
                  }
                })
            .collect(Collectors.toList());
    IntStream.range(0, elements.size())
        .forEach(
            i -> {
              nestedContexts.get(i).setSearchContext(elements.get(i));
              PageFactory.initElements(
                  new NestedElementFieldDecorator(
                      driver,
                      new NestedElementLocatorFactory(
                          elements.get(i),
                          ((NestedElementLocatorFactory) factory).getTimeOutInSeconds(),
                          ((NestedElementLocatorFactory) factory).getExceptions())),
                  nestedContexts.get(i));
            });
    return nestedContexts;
  }

  protected boolean isDecoratableNestedContext(Field field) {
    return WebComponent.class.isAssignableFrom(field.getType()) && hasFindByAnnotation(field);
  }

  protected boolean isDecoratableListNestedContext(Field field) {
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
    return Arrays.stream(field.getDeclaredAnnotations()).anyMatch(a -> a instanceof PageScope)
        ? proxyForLocator(loader, new DefaultElementLocatorFactory(driver).createLocator(field))
        : proxyForLocator(loader, locator);
  }

  private List<WebElement> proxyForListLocatorByScope(
      ClassLoader loader, ElementLocator locator, Field field) {
    return Arrays.stream(field.getDeclaredAnnotations()).anyMatch(a -> a instanceof PageScope)
        ? proxyForListLocator(loader, new DefaultElementLocatorFactory(driver).createLocator(field))
        : proxyForListLocator(loader, locator);
  }
}
