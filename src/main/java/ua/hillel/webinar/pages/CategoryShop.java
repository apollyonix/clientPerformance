package ua.hillel.webinar.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CategoryShop extends BasePage {
    @FindBy(css = "div.left-block div.product-image-container")
    private WebElement product;

    @FindBy(css = "a.ajax_add_to_cart_button")
    private WebElement addToCartButton;

    public CategoryShop(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void addProductToCart() {
        actions.moveToElement(product).moveToElement(addToCartButton).click(addToCartButton)
                .build().perform();
    }
}
