package ua.hillel.webinar.pages.order;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ua.hillel.webinar.pages.BasePage;

public class OrderPaymentPage extends BasePage {
    @FindBy(css = "li.step_current.last")
    private WebElement currentStep;

    @FindBy(css = "a.bankwire")
    private WebElement bankwireButton;

    @FindBy(css = "button.button-medium[type='submit']")
    private WebElement confirmOrderButton;

    public OrderPaymentPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(currentStep));
    }

    public OrderPaymentPage payWithBankWire() {
        bankwireButton.click();
        return this;
    }

    public OrderConfirmationPage confirmOrder() {
        confirmOrderButton.click();
        return new OrderConfirmationPage(driver);
    }
}
