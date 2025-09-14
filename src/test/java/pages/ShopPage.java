package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.math.BigDecimal;

public class ShopPage {

    public final Page page;
    public final Locator CART_TOTAL;

    public ShopPage(Page page) {
        this.page = page;
        this.CART_TOTAL = page.locator("//*[contains(@class, 'cart-count')]");
    }

    public BigDecimal getProductPrice(String productName) {
        String selectorPrice = "//*[contains(text(), '{productName}')]/..//*[contains(text(), '$')]".replace("{productName}", productName);
        String removeDollar = page.locator(selectorPrice).textContent().replace("$", "");
        return new BigDecimal(removeDollar);
    }

    public void purchaseProduct(String productName) {
        String selectorProduct = "//*[contains(text(), '{productName}')]/..//*[contains(text(), 'Buy')]".replace("{productName}", productName);
        page.locator(selectorProduct).click();
    }
}