package ua.hillel.webinar.tests.api;

import com.github.javafaker.Faker;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
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

import java.util.HashMap;
import java.util.Map;

public class ShopPerformanceTest {
    private WebDriver driver;
    private JavascriptExecutor jsExecutor;

    @Test
    public void shopPerfTest() {
        User user = new User();

        driver.get("http://automationpractice.com/index.php");
        getTimings("Open site");

        CategoryShop categoryShop = new HomePage(driver).openCategoryPage();
        getTimings("Navigate to category");

        categoryShop.addProductToCart();
        getTimings("Add item to cart");

        OrderSigninPage orderSigninPage = new CartPopup(driver).proceedToCheckout().proceedToOrderLogin();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {}
        getTimings("Proceed to login");

        CreateAccountPage createAccountPage = orderSigninPage.createNewAccount(user.email);
        getTimings("Create new user");

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
        getTimings("Save user");

        OrderShippingPage orderShippingPage = orderAddressPage.proceedToOrderShipping();


        OrderPaymentPage orderPaymentPage = orderShippingPage.acceptTerms()
            .proceedToPayment();
        getTimings("Proceed to payment");

        orderPaymentPage.payWithBankWire();
        getTimings("Select payment method");

        OrderConfirmationPage orderConfirmationPage = orderPaymentPage.confirmOrder();
        getTimings("Confirm order");

        String status = orderConfirmationPage.getOrderStatus();
        Assert.assertEquals(status, "Your order on My Store is complete.",
                "Order status is incorrect");
    }

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        jsExecutor = (JavascriptExecutor) driver;
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

//    private void getTimings(String action) {
//        Long navStart = Long.valueOf(jsExecutor.executeScript("return window.performance.timing.navigationStart").toString());
//        Long reqStart = Long.valueOf(jsExecutor.executeScript("return window.performance.timing.requestStart").toString());
//        Long respEnd = Long.valueOf(jsExecutor.executeScript("return window.performance.timing.responseEnd").toString());
//        Long complete = Long.valueOf(jsExecutor.executeScript("return window.performance.timing.domComplete").toString());
//
//        System.out.println("===== " + action + "=====");
//        System.out.println("Backend request processing time: " + (respEnd - reqStart));
//        System.out.println("Total time: " + (complete - navStart));
//        System.out.println("==========" + "==========");
//    }


    private void getTimings(String action) {
        String timings = jsExecutor.executeScript("return window.performance.getEntriesByType('navigation')").toString();
        Map<String, String> entryMap = parseEntry(timings);

        Double navStart = Double.valueOf(entryMap.get("startTime"));
        Double reqStart = Double.valueOf(entryMap.get("requestStart"));
        Double respEnd = Double.valueOf(entryMap.get("responseEnd"));
        Double complete = Double.valueOf(entryMap.get("domComplete"));
        System.out.println("===== " + action + "=====");
        System.out.println("Backend request processing time: " + (respEnd - reqStart));
        System.out.println("Total time: " + (complete - navStart));
        System.out.println("==========" + "==========");
    }


    private Map<String, String> parseEntry(String entry) {
        String content = entry.substring(2, entry.length()-2);

        Map<String, String> result = new HashMap<>();
        for (String record : content.split(", ")) {
            result.put(record.split("=")[0], record.split("=")[1]);
        }
        return result;
    }
}
