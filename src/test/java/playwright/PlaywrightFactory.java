package playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

public class PlaywrightFactory {

    private static Playwright playwright;
    private static Page page;

    @BeforeAll
    public static void beforeAll() {
        playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false));
        BrowserContext context = browser.newContext();
        page = context.newPage();
    }

    @AfterAll
    public static void afterAll() {
        playwright.close();
    }

    public static Page getPage() {
        return page;
    }
}
