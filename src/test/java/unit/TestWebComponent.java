package unit;

import io.github.orcunbalcilar.selenium.support.pagefactory.PageScoped;
import io.github.orcunbalcilar.selenium.support.pagefactory.WebComponent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TestWebComponent extends WebComponent {
  @FindBy(id = "test")
  private WebElement testElement;

  @PageScoped
  @FindBy(id = "pageScopedElement")
  private WebElement pageScopedElement;

  public TestWebComponent() {
    super();
  }
}
