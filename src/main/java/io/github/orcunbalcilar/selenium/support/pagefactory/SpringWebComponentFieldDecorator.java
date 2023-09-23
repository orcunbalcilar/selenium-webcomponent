package io.github.orcunbalcilar.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

public class SpringWebComponentFieldDecorator extends WebComponentFieldDecorator {

  public SpringWebComponentFieldDecorator(WebDriver driver, WebComponentLocatorFactory factory) {
    super(driver, factory);
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
    } else if (WebComponent.class.isAssignableFrom(field.getType())
        && hasNoSpringDIAnnotation(field)) {
      return proxyForWebComponent(loader, locator, field);
    } else if (List.class.isAssignableFrom(field.getType())
        && !(SpringWebComponent.class.isAssignableFrom((Class<?>) getListType(field)))
        && hasNoSpringDIAnnotation(field)) {
      return WebElement.class.isAssignableFrom((Class<?>) getListType(field))
          ? proxyForListLocator(loader, locator)
          : proxyForListWebComponent(loader, locator, field);
    } else {
      return null;
    }
  }

  public boolean isDecoratableListSpringWebComponent(Field field) {
    return hasFindByAnnotation(field)
        && List.class.isAssignableFrom(field.getType())
        && hasNoSpringDIAnnotation(field)
        && SpringWebComponent.class.isAssignableFrom((Class<?>) getListType(field));
  }

  public boolean isDecoratableSpringWebComponent(Field field) {
    return hasFindByAnnotation(field)
        && SpringWebComponent.class.isAssignableFrom(field.getType())
        && !hasNoSpringDIAnnotation(field);
  }

  private boolean hasNoSpringDIAnnotation(Field field) {
    return Arrays.stream(field.getAnnotations())
        .noneMatch(
            annotation ->
                annotation.annotationType().getName().equals("jakarta.inject.Inject")
                    || annotation
                        .annotationType()
                        .getName()
                        .equals("org.springframework.beans.factory.annotation.Autowired"));
  }

  protected WebComponentLocatorFactory getFactory() {
    return (WebComponentLocatorFactory) factory;
  }

  protected WebDriver getDriver() {
    return driver;
  }
}
