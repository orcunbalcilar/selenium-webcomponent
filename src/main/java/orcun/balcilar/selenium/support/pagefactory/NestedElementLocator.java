package orcun.balcilar.selenium.support.pagefactory;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.DefaultElementLocator;

public class NestedElementLocator extends DefaultElementLocator {
  protected final int timeOutInSeconds;
  private final Clock clock;
  private final List<Class<? extends WebDriverException>> exceptions;
  private WebDriverException lastException;

  public NestedElementLocator(
      SearchContext searchContext,
      Field field,
      int timeOutInSeconds,
      List<Class<? extends WebDriverException>> exceptions) {
    super(searchContext, field);
    this.timeOutInSeconds = timeOutInSeconds;
    this.clock = Clock.systemDefaultZone();
    this.exceptions = exceptions;
  }

  @Override
  public WebElement findElement() {
    return searchElement();
  }

  @Override
  public List<WebElement> findElements() {
    return searchElements();
  }

  private WebElement searchElement() {
    Instant end = clock.instant().plus(Duration.ofSeconds(timeOutInSeconds));
    while (clock.instant().isBefore(end)) {
      try {
        return NestedElementLocator.super.findElement();
      } catch (WebDriverException e) {
        if (exceptions.stream().noneMatch(ex -> ex.isInstance(e))) {
          throw new Error("Unable to locate the element " + lastException.getMessage());
        } else {
          lastException = e;
        }
      }
      waitFor();
    }
    throw new NoSuchElementException(lastException.getMessage());
  }

  private List<WebElement> searchElements() {
    Instant end = clock.instant().plus(Duration.ofSeconds(timeOutInSeconds));
    while (clock.instant().isBefore(end)) {
      try {
        List<WebElement> elements = NestedElementLocator.super.findElements();
        if (elements.isEmpty()) {
          throw new NoSuchElementException("Unable to locate the element");
        }
        return elements;
      } catch (WebDriverException e) {
        if (exceptions.stream().noneMatch(ex -> ex.isInstance(e))) {
          throw new Error("Unable to locate the elements " + lastException.getMessage());
        } else {
          lastException = e;
        }
      }
      waitFor();
    }
    throw new NoSuchElementException("Cannot locate elements : " + lastException.getMessage());
  }

  private void waitFor() {
    try {
      Thread.sleep(sleepFor());
      System.out.println("Waiting for " + sleepFor() + " ms");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new AssertionError(e);
    }
  }

  protected int sleepFor() {
    return 250;
  }
}
