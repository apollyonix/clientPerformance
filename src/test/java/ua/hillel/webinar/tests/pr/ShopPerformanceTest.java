package ua.hillel.webinar.tests.pr;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ua.hillel.webinar.pages.CartPopup;
import ua.hillel.webinar.pages.CategoryShop;
import ua.hillel.webinar.pages.HomePage;
import ua.hillel.webinar.pages.order.*;

import java.io.File;
import java.io.IOException;

public class ShopPerformanceTest {
    private WebDriver driver;
    private BrowserMobProxy browserMobProxy;

    @Test
    public void shopPerfTest() throws IOException {
        User user = new User();

        browserMobProxy.newHar();
        browserMobProxy.newPage("Home page");
        driver.get("http://automationpractice.com/index.php");

        browserMobProxy.newPage("Category page");
        CategoryShop categoryShop = new HomePage(driver).openCategoryPage();
        categoryShop.addProductToCart();

        browserMobProxy.newPage("Order login page");
        OrderSigninPage orderSigninPage = new CartPopup(driver).proceedToCheckout().proceedToOrderLogin();

        browserMobProxy.newPage("User creation page");
        CreateAccountPage createAccountPage = orderSigninPage.createNewAccount(user.email);

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
        OrderAddressPage orderAddressPage = createAccountPage.createAccount();

        browserMobProxy.newPage("Order shipping page");
        OrderShippingPage orderShippingPage = orderAddressPage.proceedToOrderShipping();

        browserMobProxy.newPage("Order payment page");
        OrderPaymentPage orderPaymentPage = orderShippingPage.acceptTerms()
            .proceedToPayment();
        orderPaymentPage.payWithBankWire();

        browserMobProxy.newPage("Order confirmation page");
        OrderConfirmationPage orderConfirmationPage = orderPaymentPage.confirmOrder();
        String status = orderConfirmationPage.getOrderStatus();

        Assert.assertEquals(status, "Your order on My Store is complete.",
                "Order status is incorrect");

        File harFile = new File("target/newHar.har");
        if (harFile.exists()) {
            harFile.delete();
        }
        Har har = browserMobProxy.getHar();
        har.writeTo(harFile);
    }

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        browserMobProxy = new BrowserMobProxyServer();
        browserMobProxy.start(10080);

        browserMobProxy.setHarCaptureTypes(CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS, CaptureType.RESPONSE_CONTENT);

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(browserMobProxy);
        seleniumProxy.setHttpProxy("127.0.0.1:10080");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setCapability(CapabilityType.PROXY, seleniumProxy);

        driver = new ChromeDriver(chromeOptions);

        driver.manage().window().maximize();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        browserMobProxy.stop();
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
