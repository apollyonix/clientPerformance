package ua.hillel.webinar.pages.order;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class OrderSummaryPage extends AbstractOrderPage {
    @FindBy(css = "ul.step li.first.step_current")
    private WebElement currentStep;

    public OrderSummaryPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(currentStep));
    }

    public OrderSigninPage proceedToOrderLogin() {
        super.proceed();
        return new OrderSigninPage(driver);
    }
}
