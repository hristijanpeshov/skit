package com.hvt.booking_lux.seleniumTesting;

import com.hvt.booking_lux.seleniumTesting.pages.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class SeleniumTests {
    private WebDriver driver;

    @BeforeEach
    public void setup(){
        System.setProperty("webdriver.chrome.driver", "D:\\Downloads\\chromedriver_win32 (1)\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown() {
        driver.quit();
    }

    @Test
    public void InvalidCredentialsTest() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.login("random", "random");
        Assertions.assertEquals(loginPage.getErrorMesage(), "Username or password is incorrect");
    }

    @Test
    public void EmptyCredentialsTest() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.login("", "");
        Assertions.assertEquals(loginPage.getErrorMesage(), "Username or password is incorrect");
    }

    @Test
    public void LoginSuccessful() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = new HomePage(driver);
        loginPage.open();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.login("user@user.com", "user");
        Assertions.assertTrue(homePage.isLoaded());
    }

    @Test
    public void ShowStatistics() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        ManageAccountPage accountPage = new ManageAccountPage(driver);
        loginPage.open();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.login("user@user.com", "user");
        accountPage.open();
        Assertions.assertTrue(accountPage.isLoaded());
        accountPage.showStats();
        Assertions.assertTrue(accountPage.statsLoaded());
    }

    @Test
    public void changeAccountInfo() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        ManageAccountPage accountPage = new ManageAccountPage(driver);
        loginPage.open();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.login("user@user.com", "user");
        accountPage.open();
        Assertions.assertTrue(accountPage.isLoaded());
        accountPage.changeName("test123");
        Assertions.assertEquals(accountPage.getName(), "test123");
    }

    @Test
    public void bookUnitWithLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        AccommodationsPage accommodationsPage = new AccommodationsPage(driver);
        ConfirmationPage confirmationPage = new ConfirmationPage(driver);
        loginPage.open();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.login("user@user.com", "user");
        accommodationsPage.open();
        Assertions.assertTrue(accommodationsPage.isLoaded());
        accommodationsPage.bookUnit();
        Assertions.assertTrue(confirmationPage.isLoaded());
    }

    @Test
    public void bookUnitWithoutLogin() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        AccommodationsPage resObjectPage = new AccommodationsPage(driver);
        resObjectPage.open();
        Assertions.assertTrue(resObjectPage.isLoaded());
        resObjectPage.bookUnit();
        Assertions.assertTrue(loginPage.isLoaded());
    }

    @Test
    public void register() throws InterruptedException {
        RegisterPage registerPage = new RegisterPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        registerPage.open();
        Assertions.assertTrue(registerPage.isLoaded());
        registerPage.register("Test1", "Test2", "testtesttest@test.com", "test", "test");
        Assertions.assertTrue(loginPage.isLoaded());
    }

    @Test
    public void addNewUnit() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        Assertions.assertTrue(loginPage.isLoaded());
        loginPage.login("user@user.com", "user");
        ListingsPage listingsPage = new ListingsPage(driver);
        listingsPage.open();
        Assertions.assertTrue(listingsPage.isLoaded());
        listingsPage.addUnit("naslov", "opis", "24", "24", "https://images.unsplash.com/photo-1554995207-c18c203602cb?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8bGl2aW5nJTIwcm9vbXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80");
        Assertions.assertTrue(listingsPage.roomDetailsIsLoaded());
    }
}
