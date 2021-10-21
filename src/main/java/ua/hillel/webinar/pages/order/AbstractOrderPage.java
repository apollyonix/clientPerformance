package ua.hillel.webinar.pages.order;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ua.hillel.webinar.pages.BasePage;

abstract class AbstractOrderPage extends BasePage {
    @FindBy(css = ".standard-checkout")
    protected WebElement proceedToCheckout;

    public AbstractOrderPage(WebDriver driver) {
        super(driver);
    }

    public void proceed() {
        proceedToCheckout.click();
    }
}
