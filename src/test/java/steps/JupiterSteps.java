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
import pages.CartPage;
import pages.ContactPage;
import pages.HomePage;
import pages.ShopPage;
import playwright.PlaywrightFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class JupiterSteps {

    public static final String JUPITER_URL = "http://jupiter.cloud.planittesting.com";
    private final Page page = PlaywrightFactory.getPage();
    HomePage homePage;
    ContactPage contactPage;
    ShopPage shopPage;
    CartPage cartPage;
    int quantityTotal = 0;
    HashMap<String, HashMap<String, Object>> purchaseOrder = new HashMap<>();
    BigDecimal totalOrderAmount = new BigDecimal("0.00");

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

    @When("I navigate to the shop page")
    public void accessShopPage() {
        homePage.clickShopPage();
        shopPage = new ShopPage(this.page);
    }

    @When("I navigate to the cart page")
    public void accessCartPage() {
        homePage.clickCartPage();
        cartPage = new CartPage(this.page);
    }

    @And("I purchase {int} {string}")
    public void purchaseProduct(int quantity, String product) {
        HashMap<String, Object> purchase = new HashMap<>();
        int cartTotal;
        BigDecimal price = shopPage.getProductPrice(product);
        quantityTotal += quantity;

        for (int i = 0; i < quantity; i++) {
            shopPage.purchaseProduct(product);
        }

        purchase.put("Quantity", quantity);
        purchase.put("Price", price);
        purchase.put("Subtotal", price.multiply(BigDecimal.valueOf(quantity)));
        purchaseOrder.put(product,purchase);

        cartTotal = Integer.parseInt(shopPage.CART_TOTAL.textContent());
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(cartTotal, quantityTotal, "Cart total has not updated");
        softAssert.assertAll();
    }

    @Then("Validate price and subtotal for each product is correct")
    public void validateSubtotalPrice() {
        SoftAssert softAssert = new SoftAssert();
        Locator table = cartPage.CART_TABLE;
        table.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        List<Locator> cartRows = table.locator("tbody tr").all();
        softAssert.assertEquals(cartRows.size(), purchaseOrder.size(), "Total items in cart is incorrect");
        for (Locator rows : cartRows) {
            List<Locator> rowCells = rows.locator("td").all();
            String product = rowCells.getFirst().textContent().strip();
            BigDecimal webUnitPrice = new BigDecimal(rowCells.get(1).textContent().replace("$", ""));
            BigDecimal webSubtotalPrice = new BigDecimal(rowCells.get(3).textContent().replace("$", ""));
            softAssert.assertEquals(webUnitPrice, purchaseOrder.get(product).get("Price"), "Unit price is incorrect for " + product);
            softAssert.assertEquals(webSubtotalPrice, purchaseOrder.get(product).get("Subtotal"), "Subtotal price is incorrect for " + product);
            totalOrderAmount = totalOrderAmount.add(new BigDecimal(rowCells.get(3).textContent().replace("$", "")));
        }
        softAssert.assertAll();
    }

    @And("Validate the total cost")
    public void validateTotalCost() {
        SoftAssert softAssert = new SoftAssert();
        BigDecimal webpageTotalOrderAmount = new BigDecimal(cartPage.TOTAL.textContent().replaceAll("[^\\d.]", ""));
        BigDecimal webpageTotalOrderAmountRounder = webpageTotalOrderAmount.setScale(2, RoundingMode.HALF_UP);
        softAssert.assertEquals(webpageTotalOrderAmountRounder, totalOrderAmount, "Unit price is incorrect");
        softAssert.assertAll();
    }
}