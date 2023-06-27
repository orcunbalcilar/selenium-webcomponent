package orcun.balcilar.selenium.support.pagefactory;

import io.cucumber.spring.ScenarioScope;
import java.util.List;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ScenarioScope
@Component
public class HomePage extends Page {

  @Autowired
  @FindBy(css = "fsw-input-button")
  private List<Row> rows;

  public void selectFromPort(String port) {
    System.out.println(port);
    rows.get(0).selectPort(port);
  }

  public void selectToPort(String port) {
    System.out.println(port);
    rows.get(1).selectPort(port);
  }
}
