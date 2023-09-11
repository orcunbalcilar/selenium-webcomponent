package io.github.orcunbalcilar.selenium.support.pagefactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The PageScoped annotation is a marker typically associated with fields in subclasses of the
 * {@link WebComponent} class. Its presence triggers a global search scope for the WebElement across
 * the entire page, mirroring the behavior of the driver.findElement(By.id(".")) method.
 *
 * <p>In the absence of this annotation, the search operation for a WebElement is localized,
 * operating within the search context defined by the {@link WebComponent} class itself, thereby
 * limiting the search region.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PageScoped {}
