package unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.github.orcunbalcilar.selenium.support.pagefactory.SpringWebComponentFieldDecorator;
import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponentLocatorFactory;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class SpringWebComponentFieldDecoratorTest {

  @Test
  public void testDecorate_WebComponentField() throws Exception {
    WebDriver driver = mock(WebDriver.class);

    WebElement mockParentElement = mock(WebElement.class);
    when(driver.findElement(By.cssSelector(".test-web-component"))).thenReturn(mockParentElement);

    WebElement mockTestElement = mock(WebElement.class);
    when(mockParentElement.findElement(By.id("test"))).thenReturn(mockTestElement);

    SpringWebComponentFieldDecorator decorator =
        new SpringWebComponentFieldDecorator(driver, new WebComponentLocatorFactory(driver, 5));
    TestPage testPage = new TestPage();
    PageFactory.initElements(decorator, testPage);

    // Reaching testElement from TestWebComponent
    Field webComponent = testPage.getClass().getDeclaredField("webComponent");
    webComponent.setAccessible(true);
    TestWebComponent component = (TestWebComponent) webComponent.get(testPage);
    Field testElementField = component.getClass().getDeclaredField("testElement");
    testElementField.setAccessible(true);
    WebElement testElementFromComponent = (WebElement) testElementField.get(component);

    assertNotNull(component);
    assertEquals(mockParentElement.toString(), component.getSearchContext().toString());
    assertEquals(mockTestElement.toString(), testElementFromComponent.toString());
  }

  @Test
  public void testDecorate_PageScopedField() throws Exception {
    WebDriver driver = mock(WebDriver.class);

    WebElement mockPageScopedElement = mock(WebElement.class);
    when(driver.findElement(By.id("pageScopedElement"))).thenReturn(mockPageScopedElement);

    SpringWebComponentFieldDecorator decorator =
        new SpringWebComponentFieldDecorator(driver, new WebComponentLocatorFactory(driver, 5));
    TestPage testPage = new TestPage();
    PageFactory.initElements(decorator, testPage);

    // Reaching pageScopedElement from TestWebComponent
    Field webComponentField = testPage.getClass().getDeclaredField("webComponent");
    webComponentField.setAccessible(true);
    TestWebComponent webComponent = (TestWebComponent) webComponentField.get(testPage);
    Field pageScopedElementField = webComponent.getClass().getDeclaredField("pageScopedElement");
    pageScopedElementField.setAccessible(true);

    WebElement pageScopedElement = (WebElement) pageScopedElementField.get(webComponent);

    assertNotNull(pageScopedElement);
    assertEquals(mockPageScopedElement.toString(), pageScopedElement.toString());
  }
}
