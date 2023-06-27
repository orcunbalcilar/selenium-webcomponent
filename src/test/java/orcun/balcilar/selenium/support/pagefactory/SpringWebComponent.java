package orcun.balcilar.selenium.support.pagefactory;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class SpringWebComponent extends WebComponent {
  @Autowired protected Browser browser;
}
