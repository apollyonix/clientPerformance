package ua.hillel.webinar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ua.hillel.webinar.pages.order.OrderSummaryPage;

public class CartPopup extends BasePage {
    @FindBy(css = "div#layer_cart")
    private WebElement cartPopup;

    @FindBy(css = "a.btn-default.button-medium")
    private WebElement proceedToCheckoutButton;

    public CartPopup(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(cartPopup));
    }

    public OrderSummaryPage proceedToCheckout() {
        proceedToCheckoutButton.click();
        return new OrderSummaryPage(driver);
    }
}
