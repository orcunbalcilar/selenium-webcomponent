package io.github.orcunbalcilar.selenium.support.pagefactory;

import lombok.Getter;
import org.openqa.selenium.SearchContext;

/**
 * WebComponent is an abstract class representing a component of a web page.
 *
 * <p>Using this class, WebElement search scope is localized to the component itself, rather than
 * the entire page.
 *
 * <p>And it also provides the ability to modularize the methods of the page into components.
 *
 * <p>With usage of this class, the web page can be broken down into smaller components instead of
 * designing a whole web page.
 *
 * <p>
 *
 * <p>
 *
 * <p>For example, consider the following web page Turkish Airlines Home Page: <a
 * href="https://www.turkishairlines.com">Turkish Airlines</a>
 *
 * <p>
 *
 * <p>
 *
 * <p>You can simply define a HomePage class and its components as follows:
 *
 * <p>
 *
 * <p>
 *
 * <p><b>Web Page: HomePage</b>
 *
 * <pre>
 * {@literal}@ScenarioScope
 * {@literal}@Component
 * public class HomePage extends Page {
 *
 *   {@literal}@Autowired
 *   {@literal}@FindBy(id = "newMiniBooker")
 *   private MiniBooker miniBooker;
 *
 *   {@literal}@FindBy(id = "signin")
 *   private WebElement signInButton; // WebElement for the whole page -> You are also allowed to use WebElement fields.
 *                                    // It is fully compatible with Selenium PageFactory features.
 *
 * }
 * </pre>
 *
 * <p><b>WebComponent: MiniBooker</b>
 *
 * <pre>
 * {@literal}@ScenarioScope
 * {@literal}@Component
 * public class MiniBooker extends WebComponent {
 *
 *   {@literal}@Autowired
 *   {@literal}@FindBy(id = "bookerAvailabilityTab")
 *   private BookerAvailabilityTab bookerAvailabilityTab; // Another WebComponent for chaining.
 *
 * }
 * </pre>
 *
 * <p><b>WebComponent: BookerAvailabilityTab</b>
 *
 * <pre>
 * {@literal}@ScenarioScope
 * {@literal}@Component
 * public class BookerAvailabilityTab extends WebComponent {
 *
 *   {@literal}@FindBy(id = "a[data-type='round']")
 *   private WebElement roundTripButton;
 *
 *   {@literal}@FindBy(id = "a[data-type='oneway']")
 *   private WebElement oneWayButton;
 *
 *   {@literal}@PageScoped // Just for demonstration. You're not restricted to narrow down the search scope into this web component's one.
 *   {@literal}@FindBy(id = "a[data-type='multi']")
 *   private WebElement multiCityButton;
 *
 * }
 * </pre>
 *
 * @author Orçun Balcılar
 */
@Getter
public abstract class WebComponent {
  private SearchContext searchContext;

  public void setSearchContext(SearchContext searchContext) {
    this.searchContext = searchContext;
  }
}
