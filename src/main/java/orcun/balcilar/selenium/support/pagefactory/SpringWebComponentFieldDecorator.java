package orcun.balcilar.selenium.support.pagefactory;

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

  public WebDriver getDriver() {
    return driver;
  }

  public WebComponentLocatorFactory getFactory() {
    return (WebComponentLocatorFactory) this.factory;
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
    } else if (WebComponent.class.isAssignableFrom(field.getType())
        && hasNoSpringDIAnnotation(field)) {
      return proxyForWebComponent(loader, locator, field);
    } else if (List.class.isAssignableFrom(field.getType()) && hasNoSpringDIAnnotation(field)) {
      return getListType(field) instanceof WebElement
          ? proxyForListLocatorByScope(loader, locator, field)
          : proxyForListWebComponent(loader, locator, field);
    } else {
      return null;
    }
  }

  public boolean isDecoratableSpringWebComponent(Field field) {
    return SpringWebComponent.class.isAssignableFrom(field.getType())
        && !hasNoSpringDIAnnotation(field)
        && hasFindByAnnotation(field);
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
}
