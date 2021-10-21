package ua.hillel.webinar.tests.st;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.time.StopWatch;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ua.hillel.webinar.pages.CartPopup;
import ua.hillel.webinar.pages.CategoryShop;
import ua.hillel.webinar.pages.HomePage;
import ua.hillel.webinar.pages.order.*;

public class ShopPerformanceTest {
    private WebDriver driver;
    @Test
    public void shopPerfTest()  {
        User user = new User();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        driver.get("http://automationpractice.com/index.php");
        stopWatch.stop();
        long openSite = stopWatch.getTime();
        stopWatch.reset();

        stopWatch.start();
        CategoryShop categoryShop = new HomePage(driver).openCategoryPage();
        stopWatch.stop();
        long navigateToCategory = stopWatch.getTime();
        stopWatch.reset();

        stopWatch.start();
        categoryShop.addProductToCart();
        stopWatch.stop();
        long addItemToCategory = stopWatch.getTime();
        stopWatch.reset();

        stopWatch.start();
        OrderSigninPage orderSigninPage = new CartPopup(driver).proceedToCheckout().proceedToOrderLogin();
        stopWatch.stop();
        long proceedToLogin = stopWatch.getTime();
        stopWatch.reset();

        stopWatch.start();
        CreateAccountPage createAccountPage = orderSigninPage.createNewAccount(user.email);
        stopWatch.stop();
        long createNewUserForOrder = stopWatch.getTime();
        stopWatch.reset();

        createAccountPage.setFirstname(user.firstname)
            .setLastname(user.lastname)
            .setPassword(user.password)
            .setAddress(user.address.address)
            .setCity(user.address.city)
            .setState(user.address.state)
            .setCountry(user.address.country)
            .setZipCode(user.address.zip)
            .setMobilePhone(user.mobile)
            .setAlias("Test Address");

        stopWatch.start();
        OrderAddressPage orderAddressPage = createAccountPage.createAccount();
        stopWatch.stop();
        long saveUser = stopWatch.getTime();
        stopWatch.reset();

        stopWatch.start();
        OrderShippingPage orderShippingPage = orderAddressPage.proceedToOrderShipping();
        stopWatch.stop();
        long proceedToShipping = stopWatch.getTime();
        stopWatch.reset();

        stopWatch.start();
        OrderPaymentPage orderPaymentPage = orderShippingPage.acceptTerms()
            .proceedToPayment();
        stopWatch.stop();
        long proceedToPayment = stopWatch.getTime();
        stopWatch.reset();

        stopWatch.start();
        orderPaymentPage.payWithBankWire();
        stopWatch.stop();
        long selectPaymentMethod = stopWatch.getTime();
        stopWatch.reset();

        stopWatch.start();
        OrderConfirmationPage orderConfirmationPage = orderPaymentPage.confirmOrder();
        stopWatch.stop();
        long confirmOrder = stopWatch.getTime();
        stopWatch.reset();

        String status = orderConfirmationPage.getOrderStatus();

        Assert.assertEquals(status, "Your order on My Store is complete.",
                "Order status is incorrect");

        System.out.println("Open site:\t\t" + convertMillsToSeconds(openSite));
        System.out.println("Navigate to category:\t\t" + convertMillsToSeconds(navigateToCategory));
        System.out.println("Add item to cart:\t\t" + convertMillsToSeconds(addItemToCategory));
        System.out.println("Proceed to login:\t\t" + convertMillsToSeconds(proceedToLogin));
        System.out.println("Create new user:\t\t" + convertMillsToSeconds(createNewUserForOrder));
        System.out.println("Save user:\t\t" + convertMillsToSeconds(saveUser));
        System.out.println("Proceed to shipping:\t\t" + convertMillsToSeconds(proceedToShipping));
        System.out.println("Proceed to payment:\t\t" + convertMillsToSeconds(proceedToPayment));
        System.out.println("Select payment method:\t\t" + convertMillsToSeconds(selectPaymentMethod));
        System.out.println("Confirm order:\t\t" + convertMillsToSeconds(confirmOrder));
    }

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterClass(alwaysRun = true)
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

    private double convertMillsToSeconds(long mills) {
        return Double.parseDouble(String.format("%.2f", mills/1000.0).replace(",", "."));
    }
}
