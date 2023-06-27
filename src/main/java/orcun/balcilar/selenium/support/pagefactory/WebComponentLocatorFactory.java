package orcun.balcilar.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.util.List;
import lombok.Getter;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class WebComponentLocatorFactory implements ElementLocatorFactory {
  private final SearchContext searchContext;
  @Getter private final int timeOutInSeconds;
  @Getter private final List<Class<? extends WebDriverException>> exceptions;
  private static final List<Class<? extends WebDriverException>> DEFAULT_EXCEPTIONS =
      List.of(StaleElementReferenceException.class, NoSuchElementException.class);

  public WebComponentLocatorFactory(
      SearchContext searchContext,
      int timeOutInSeconds,
      List<Class<? extends WebDriverException>> exceptions) {
    this.searchContext = searchContext;
    this.timeOutInSeconds = timeOutInSeconds;
    this.exceptions = exceptions;
  }

  public WebComponentLocatorFactory(SearchContext searchContext, int timeOutInSeconds) {
    this.searchContext = searchContext;
    this.timeOutInSeconds = timeOutInSeconds;
    this.exceptions = DEFAULT_EXCEPTIONS;
  }

  @Override
  public ElementLocator createLocator(Field field) {
    return new WebComponentLocator(searchContext, field, timeOutInSeconds, exceptions);
  }
}
