package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartPage {
    public final Page page;
    public final Locator CART_TABLE;
    public final Locator TOTAL;

    public CartPage(Page page) {
        this.page = page;
        this.CART_TABLE = page.locator("//table[contains(@class,'cart-items')]");
        this.TOTAL = page.getByText("Total:");
    }
}