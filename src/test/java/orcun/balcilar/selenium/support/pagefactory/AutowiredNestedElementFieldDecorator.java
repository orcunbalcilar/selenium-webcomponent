package orcun.balcilar.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.IntStream;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AutowiredNestedElementFieldDecorator extends NestedElementFieldDecorator {

  private final Object page;

  @Autowired private ApplicationContext applicationContext;

  public AutowiredNestedElementFieldDecorator(
      WebDriver driver, NestedElementLocatorFactory factory, Object page) {
    super(driver, factory);
    this.page = page;
  }

  @Override
  protected WebComponent proxyForWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    if (!field.isAnnotationPresent(Autowired.class)) {
      return super.proxyForWebComponent(loader, locator, field);
    } else {
      try {
        field.setAccessible(true);
        WebComponent webComponent = (WebComponent) field.get(page);
        WebElement proxyWebElement = proxyForLocatorByScope(loader, locator, field);
        webComponent.setSearchContext(proxyWebElement);
        initWebComponentElements(proxyWebElement, webComponent);
        return webComponent;
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  protected void initWebComponentElements(WebElement proxyWebElement, WebComponent webComponent) {
    AutowiredNestedElementFieldDecorator fieldDecorator =
        applicationContext.getBean(
            AutowiredNestedElementFieldDecorator.class,
            driver,
            new NestedElementLocatorFactory(proxyWebElement, 30),
            webComponent);
    PageFactory.initElements(fieldDecorator, webComponent);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected List<? extends WebComponent> proxyForListWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    if (!field.isAnnotationPresent(Autowired.class)) {
      return super.proxyForListWebComponent(loader, locator, field);
    } else {
      try {
        List<WebElement> elements = proxyForListLocatorByScope(loader, locator, field);
        field.setAccessible(true);
        List<WebComponent> webComponents = (List<WebComponent>) field.get(page);
        initWebComponentElements(elements.get(0), webComponents.get(0));
        IntStream.range(webComponents.size(), elements.size())
            .forEach(
                i -> {
                  WebComponent webComponent =
                      createInstance(getTypeArgumentWebComponentClass(field));
                  webComponent.setSearchContext(elements.get(i));
                  applicationContext.getAutowireCapableBeanFactory().autowireBean(webComponent);
                  initWebComponentElements(elements.get(i), webComponent);
                  webComponents.add(webComponent);
                });
        return webComponents;
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
