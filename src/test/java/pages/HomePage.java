package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class HomePage {
    public final Page page;
    public final Locator WELCOME_MESSAGE;

    public HomePage(Page page){
        this.page = page;
        this.WELCOME_MESSAGE = page.getByText("Welcome to Jupiter Toys");
    }

    public void clickContactPage() {
        page.click("role=link[name='Contact']");
    }

    public void clickShopPage() {
        page.click("role=link[name='Shop']");
    }

    public void clickCartPage() {
        page.click("#nav-cart");
    }
}
