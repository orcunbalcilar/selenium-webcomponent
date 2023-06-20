package orcun.balcilar.selenium.support.pagefactory;

import io.cucumber.spring.ScenarioScope;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

@Getter
@Component
@ScenarioScope
public class Browser {
  private WebDriver driver;

  @PostConstruct
  private void init() {
    driver = WebDriverManager.firefoxdriver().create();
    driver.manage().window().maximize();
  }

  public void get(String url) {
    driver.get(url);
  }
}
