package ua.hillel.webinar.pages.order;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ua.hillel.webinar.pages.BasePage;

public class OrderConfirmationPage extends BasePage {
    @FindBy(css = "#step_end")
    private WebElement currentStep;

    @FindBy(css = "p.cheque-indent strong")
    private WebElement orderStatus;

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(currentStep));
    }

    public String getOrderStatus() {
        return orderStatus.getText();
    }
}
