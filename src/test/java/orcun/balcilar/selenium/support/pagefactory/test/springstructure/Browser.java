package orcun.balcilar.selenium.support.pagefactory.test.springstructure;

import io.cucumber.spring.ScenarioScope;
import io.github.bonigarcia.wdm.WebDriverManager;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Getter
@Component
@ScenarioScope
public class Browser {
  private WebDriver driver;

  @PostConstruct
  public void init() {
    driver = WebDriverManager.firefoxdriver().create();
    driver.manage().window().maximize();
  }

  public void get(String url) {
    driver.get(url);
  }
}
