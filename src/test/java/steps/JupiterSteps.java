package steps;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import io.cucumber.java.en.Given;
import org.junit.jupiter.api.Assertions;
import playwright.PlaywrightFactory;

public class JupiterSteps {

    private final Page page = PlaywrightFactory.getPage();

    @Given("I can access the Jupiter website")
    public void accessJupiterURL() {
        String jupiterURL = "http://jupiter.cloud.planittesting.com";
        Response response = page.navigate(jupiterURL);
        Assertions.assertEquals(200, response.status());
    }
}
