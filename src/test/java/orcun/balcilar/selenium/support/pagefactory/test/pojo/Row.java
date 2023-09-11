package orcun.balcilar.selenium.support.pagefactory.test.pojo;

import orcun.balcilar.selenium.support.pagefactory.WebComponent;
import org.openqa.selenium.support.FindBy;

public class Row extends WebComponent {

  @FindBy(css = ".input-button__content")
  private Content content;

  public Row() {
    System.out.println("iyi miyiz");
  }

  public void enterPort(String port) {
    content.click();
    content.clear();
    content.sendKeys(port);
  }
}
