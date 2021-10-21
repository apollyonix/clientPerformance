package ua.hillel.webinar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {
    @FindBy(css = "#block_top_menu ul.sf-menu > li:nth-child(3)")
    private WebElement topMenuItem;

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        webDriverWait.until(ExpectedConditions.visibilityOf(topMenuItem));
    }

    public CategoryShop openCategoryPage() {
        topMenuItem.click();
        return new CategoryShop(driver);
    }
}
