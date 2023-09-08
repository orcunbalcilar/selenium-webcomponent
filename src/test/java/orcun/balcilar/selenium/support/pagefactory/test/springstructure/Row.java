package orcun.balcilar.selenium.support.pagefactory.test.springstructure;

import javax.annotation.PostConstruct;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class Row extends orcun.balcilar.selenium.support.pagefactory.SpringWebComponent {

  @Autowired
  @FindBy(css = ".input-button__content")
  private Content content;

  @PostConstruct
  public void init() {
    System.out.println("iyi miyiz");
  }

  public void enterPort(String port) {
    content.click();
    content.clear();
    content.sendKeys(port);
  }
}
