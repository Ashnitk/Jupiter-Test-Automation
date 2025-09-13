package steps;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.testng.asserts.SoftAssert;
import pages.ContactPage;
import pages.HomePage;
import playwright.PlaywrightFactory;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class JupiterSteps {

    public static final String JUPITER_URL = "http://jupiter.cloud.planittesting.com";
    private final Page page = PlaywrightFactory.getPage();
    HomePage homePage;
    ContactPage contactPage;

    @Given("I access the Jupiter website")
    public void accessJupiterURL() {
        homePage = new HomePage(this.page);
        Response response = page.navigate(JUPITER_URL);
        Assertions.assertEquals(200, response.status());
        assertThat(homePage.WELCOME_MESSAGE).isVisible();
    }

    @When("I navigate to the contact page")
    public void accessContactPage() {
        homePage.clickContactPage();
        contactPage = new ContactPage(this.page);
    }

    @And("I click submit button")
    public void submitForm() {
        contactPage.clickSubmit();
    }

    @Then("Verify error messages are present")
    public void validateErrorMessagesArePresent() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(contactPage.HEADER_MESSAGE.textContent().strip(), "We welcome your feedback - but we won't get it unless you complete the form correctly.", "Header error message not present");
        softAssert.assertEquals(contactPage.FORENAME_ERROR.textContent(), "Forename is required", "Forename error message not present");
        softAssert.assertEquals(contactPage.EMAIL_ERROR.textContent(), "Email is required", "Email error message not present");
        softAssert.assertEquals(contactPage.MESSAGE_ERROR.textContent(), "Message is required", "Message error message not present");
        softAssert.assertAll();
    }

    @And("I populate the mandatory fields for {string}")
    public void populateMandatoryFields(String personName) {
        contactPage.FORENAME_TEXTBOX.fill(personName);
        contactPage.EMAIL_TEXTBOX.fill(personName + "@TestEmail.com");
        contactPage.MESSAGE_TEXTBOX.fill("Please contact me back :)");
        submitForm();
    }

    @Then("Validate the errors have disappeared for {string}")
    public void validateErrorsAreGone(String personName) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertFalse(contactPage.FORENAME_ERROR_MESSAGE.isVisible(), "Forename error is visible");
        softAssert.assertFalse(contactPage.EMAIL_ERROR_MESSAGE.isVisible(), "Email error is visible");
        softAssert.assertFalse(contactPage.QUERY_ERROR_MESSAGE.isVisible(), "Message error is visible");
        if(contactPage.SENDING_MESSAGE.isVisible()){
            contactPage.SENDING_MESSAGE.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.HIDDEN));
        }
        softAssert.assertTrue(contactPage.getConfirmationMessage(personName).isVisible(), "Confirmation message not visible");
        softAssert.assertAll();
    }
}
