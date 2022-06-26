package ua.hillel.webinar.tests.regular;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ua.hillel.webinar.pages.CartPopup;
import ua.hillel.webinar.pages.HomePage;

public class ShopTest {
    private WebDriver driver;

    @Test
    public void shopTest() {
        driver.get("http://automationpractice.com/index.php");

        User user = new User();

        new HomePage(driver).openCategoryPage().addProductToCart();

        String status = new CartPopup(driver).proceedToCheckout()
                .proceedToOrderLogin()
                .createNewAccount(user.email)
                .setFirstname(user.firstname)
                .setLastname(user.lastname)
                .setPassword(user.password)
                .setAddress(user.address.address)
                .setCity(user.address.city)
                .setState(user.address.state)
                .setCountry(user.address.country)
                .setZipCode(user.address.zip)
                .setMobilePhone(user.mobile)
                .setAlias("Test Address")
                .createAccount()
                .proceedToOrderShipping()
                .acceptTerms()
                .proceedToPayment()
                .payWithBankWire()
                .confirmOrder()
                .getOrderStatus();

        Assert.assertEquals(status, "Your order on My Store is complete.",
                "Order status is incorrect");
    }

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    private static class User {
        private final String firstname;
        private final String lastname;
        private final String email;
        private final String password;
        private final String mobile;
        private final Address address;

        User() {
            Faker faker = new Faker();
            firstname = faker.name().firstName();
            lastname = faker.name().lastName();
            email = String.format("%s.%s@mail.com", firstname.toLowerCase(), lastname.toLowerCase());
            password = faker.random().hex();
            mobile = faker.phoneNumber().cellPhone();

            address = new Address();
        }
    }

    private static class Address {
        private final String address;
        private final String city;
        private final String state;
        private final String country;
        private final String zip;

        Address() {
            Faker faker = new Faker();
            address = faker.address().streetAddress();
            city = faker.address().city();
            state = faker.address().state();
            country = "United States";
            zip = faker.address().zipCode().substring(0, 5);


        }
    }
}
