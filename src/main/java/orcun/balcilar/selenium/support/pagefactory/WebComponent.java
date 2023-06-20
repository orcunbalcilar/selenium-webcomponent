package orcun.balcilar.selenium.support.pagefactory;

import lombok.Getter;
import org.openqa.selenium.SearchContext;

@Getter
public abstract class WebComponent {
  private SearchContext searchContext;

  public void setSearchContext(SearchContext searchContext) {
    this.searchContext = searchContext;
  }
}
