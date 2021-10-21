package ua.hillel.webinar.pages.order;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OrderShippingPage extends AbstractOrderPage {
    @FindBy(css = "li.step_current.four")
    private WebElement currentStep;

    @FindBy(css = "#cgv")
    private WebElement acceptTermCheckbox;

    public OrderShippingPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(currentStep));
    }

    public OrderShippingPage acceptTerms() {
        acceptTermCheckbox.click();
        return this;
    }

    public OrderPaymentPage proceedToPayment() {
        super.proceed();
        return new OrderPaymentPage(driver);
    }
}
