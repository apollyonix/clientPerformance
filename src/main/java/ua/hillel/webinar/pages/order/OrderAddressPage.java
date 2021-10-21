package ua.hillel.webinar.pages.order;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OrderAddressPage extends AbstractOrderPage {
    @FindBy(css = "li.step_current.third")
    private WebElement currentStep;

    @FindBy(css = "button[name='processAddress']")
    private WebElement proceedButton;

    public OrderAddressPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(currentStep));
    }

    public OrderShippingPage proceedToOrderShipping() {
        proceedButton.click();
        return new OrderShippingPage(driver);
    }
}
