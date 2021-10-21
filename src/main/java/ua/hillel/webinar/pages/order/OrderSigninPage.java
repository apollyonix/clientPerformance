package ua.hillel.webinar.pages.order;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ua.hillel.webinar.pages.BasePage;

public class OrderSigninPage extends BasePage {
    @FindBy(css = "li.step_current.second")
    private WebElement currentStep;

    @FindBy(css = "#email_create")
    private WebElement createEmailInput;

    @FindBy(css = "#SubmitCreate")
    private WebElement createAccountButton;

    public OrderSigninPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(currentStep));
    }

    public CreateAccountPage createNewAccount(String email) {
        createEmailInput.sendKeys(email);
        createAccountButton.click();
        return new CreateAccountPage(driver);
    }
}
