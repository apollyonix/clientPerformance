package ua.hillel.webinar.pages.order;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ua.hillel.webinar.pages.BasePage;

public class CreateAccountPage extends BasePage {
    @FindBy(css = "#customer_firstname")
    private WebElement firstnameInput;

    @FindBy(css = "#customer_lastname")
    private WebElement lastnameInput;

    @FindBy(css = "#passwd")
    private WebElement passwordInput;

    @FindBy(css = "#address1")
    private WebElement addressInput;

    @FindBy(css = "#city")
    private WebElement cityInput;

    @FindBy(css = "#id_state")
    private WebElement stateSelect;

    @FindBy(css = "#postcode")
    private WebElement zipInput;

    @FindBy(css = "#id_country")
    private WebElement countrySelect;

    @FindBy(css = "#phone_mobile")
    private WebElement mobileInput;

    @FindBy(css = "#alias")
    private WebElement aliasInput;

    @FindBy(css = "#submitAccount")
    private WebElement createAccountButton;

    public CreateAccountPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(lastnameInput));
    }

    public CreateAccountPage setFirstname(String firstname) {
        firstnameInput.sendKeys(firstname);
        return this;
    }

    public CreateAccountPage setLastname(String lastname) {
        lastnameInput.sendKeys(lastname);
        return this;
    }

    public CreateAccountPage setPassword(String password) {
        passwordInput.sendKeys(password);
        return this;
    }

    public CreateAccountPage setAddress(String address) {
        addressInput.sendKeys(address);
        return this;
    }

    public CreateAccountPage setCity(String city) {
        cityInput.sendKeys(city);
        return this;
    }

    public CreateAccountPage setState(String state) {
        new Select(stateSelect).selectByVisibleText(state);
        return this;
    }

    public CreateAccountPage setZipCode(String zip) {
        zipInput.sendKeys(zip);
        return this;
    }

    public CreateAccountPage setCountry(String country) {
        new Select(countrySelect).selectByVisibleText(country);
        return this;
    }

    public CreateAccountPage setMobilePhone(String mobilePhone) {
        mobileInput.sendKeys(mobilePhone);
        return this;
    }

    public CreateAccountPage setAlias(String alias) {
        aliasInput.clear();
        aliasInput.sendKeys(alias);
        return this;
    }

    public OrderAddressPage createAccount() {
        createAccountButton.click();
        return new OrderAddressPage(driver);
    }
}
