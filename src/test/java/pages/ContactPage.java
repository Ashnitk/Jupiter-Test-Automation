package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class ContactPage {

    public final Page page;
    public final Locator HEADER_MESSAGE;
    public final Locator FORENAME_ERROR;
    public final Locator EMAIL_ERROR;
    public final Locator MESSAGE_ERROR;
    public final Locator FORENAME_TEXTBOX;
    public final Locator EMAIL_TEXTBOX;
    public final Locator MESSAGE_TEXTBOX;
    public final Locator FORENAME_ERROR_MESSAGE;
    public final Locator EMAIL_ERROR_MESSAGE;
    public final Locator QUERY_ERROR_MESSAGE;
    public final Locator SENDING_MESSAGE;

    public ContactPage(Page page){
        this.page = page;
        this.HEADER_MESSAGE = page.locator("#header-message");
        this.FORENAME_ERROR = page.locator("#forename-err");
        this.EMAIL_ERROR = page.locator("#email-err");
        this.MESSAGE_ERROR = page.locator("#message-err");
        this.FORENAME_TEXTBOX = page.locator("#forename");
        this.EMAIL_TEXTBOX = page.locator("#email");
        this.MESSAGE_TEXTBOX = page.locator("#message");
        this.FORENAME_ERROR_MESSAGE = page.getByText("Forename is required");
        this.EMAIL_ERROR_MESSAGE = page.getByText("Email is required");
        this.QUERY_ERROR_MESSAGE = page.getByText("Message is required");
        this.SENDING_MESSAGE = page.getByText("Sending Feedback");
    }

    public Locator getConfirmationMessage(String person) {
        String thanksMessage = "Thanks {person}, we appreciate your feedback.".replace("{person}", person);
        return page.getByText(thanksMessage);
    }

    public void clickSubmit() {
        page.click("role=link[name='Submit']");
    }
}