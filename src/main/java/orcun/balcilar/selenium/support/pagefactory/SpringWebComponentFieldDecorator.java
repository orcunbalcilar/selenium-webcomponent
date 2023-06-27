package orcun.balcilar.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.util.Arrays;
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

/**
 * A class implementing the functionality to decorate web components with Spring annotations. It
 * extends the WebComponentFieldDecorator and is annotated with @Component and @Scope("prototype").
 *
 * @see WebComponentFieldDecorator
 */
@Component
@Scope("prototype")
public class SpringWebComponentFieldDecorator extends WebComponentFieldDecorator {

  private final Object page;

  @Autowired private ApplicationContext applicationContext;

  @Autowired
  public SpringWebComponentFieldDecorator(
      WebDriver driver, WebComponentLocatorFactory factory, Object page) {
    super(driver, factory);
    this.page = page;
  }

  /**
   * This method returns a proxy for a web component. It overrides the proxyForWebComponent defined
   * in the {@link org.openqa.selenium.support.PageFactory} class.
   *
   * @param loader the class loader
   * @param locator the locator used to locate the element
   * @param field the field annotated with Autowired
   * @return a WebComponent proxy
   */
  @Override
  protected WebComponent proxyForWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    if (hasDIAnnotation(field)) {
      return super.proxyForWebComponent(loader, locator, field);
    }

    return proxyForSpringWebComponent(loader, locator, field);
  }

  /**
   * This method creates a WebComponent object that is annotated with Autowired. It sets up a proxy
   * for the web element that is located using the specified locator. This method is used internally
   * by proxyForWebComponent() method.
   *
   * @param loader the class loader
   * @param locator the locator used to locate the element
   * @param field the field annotated with Autowired
   * @return a WebComponent object
   */
  private WebComponent proxyForSpringWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
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
  
  /**
    * This method proxies for a list of WebComponent objects. If the field is not annotated with Jakarta Inject
 * or Spring Autowired, it calls the parent proxyForListWebComponent() method. If the field is annotated with
 * one of these annotations, it calls the proxyForListSpringWebComponent() method to create a List of WebComponent
 * objects.
 *
 * @param loader the class loader
 * @param locator the locator used to locate the elements for the list
 * @param field the field annotated with Jakarta Inject or Spring Autowired
 * @return a List of WebComponent objects
 */
  @Override
  protected List<? extends WebComponent> proxyForListWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    if (hasDIAnnotation(field)) {
      return super.proxyForListWebComponent(loader, locator, field);
    }
    
    return proxyForListSpringWebComponent(loader, locator, field);
  }
  
  @SuppressWarnings("unchecked")
  private List<? extends WebComponent> proxyForListSpringWebComponent(
      ClassLoader loader, ElementLocator locator, Field field) {
    try {
      List<WebElement> elements = proxyForListLocatorByScope(loader, locator, field);
      field.setAccessible(true);
      List<WebComponent> webComponents = (List<WebComponent>) field.get(page);
      initWebComponentElements(elements.get(0), webComponents.get(0));
      propagateWebComponentList(elements, webComponents, field);
      return webComponents;
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  
  private void propagateWebComponentList(
      List<WebElement> elements, List<WebComponent> webComponents, Field field) {
    IntStream.range(1, elements.size())
        .forEach(
            i -> {
              WebComponent webComponent = createInstance(getTypeArgumentWebComponentClass(field));
              webComponent.setSearchContext(elements.get(i));
              applicationContext.getAutowireCapableBeanFactory().autowireBean(webComponent);
              initWebComponentElements(elements.get(i), webComponent);
              webComponents.add(webComponent);
            });
  }

  /**
   * This method extends from the parent class's initWebComponentElements method, and it configures
   * the WebComponent's fields by utilizing a SpringWebComponentFieldDecorator. An instance of
   * SpringWebComponentFieldDecorator is fetched through the application context and is used to
   * initialize the fields of the WebComponent. The decorator has the driver, the
   * WebComponentLocatorFactory, and the WebComponent itself as dependencies.
   *
   * @param proxyWebElement The WebElement being used as a stand-in during the initialization
   *     process
   * @param webComponent The WebComponent whose fields will be initialized
   */
  @Override
  protected void initWebComponentElements(WebElement proxyWebElement, WebComponent webComponent) {
    SpringWebComponentFieldDecorator fieldDecorator =
        applicationContext.getBean(
            SpringWebComponentFieldDecorator.class,
            driver,
            new WebComponentLocatorFactory(proxyWebElement, 30),
            webComponent);
    PageFactory.initElements(fieldDecorator, webComponent);
  }
  
  private boolean hasDIAnnotation(Field field) {
    return Arrays.stream(field.getAnnotations())
        .anyMatch(
            annotation ->
                annotation.annotationType().getName().equals("jakarta.inject.Inject")
                    || annotation.annotationType().getName().equals("org.springframework.beans.factory.annotation.Autowired"));
  }
}
