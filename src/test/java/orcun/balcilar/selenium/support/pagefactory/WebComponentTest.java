package orcun.balcilar.selenium.support.pagefactory;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasspathResource("WebComponent.feature")
@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME,
    value = "orcun.balcilar.selenium.support.pagefactory.glue")
public class WebComponentTest {}
