package com.hvt.booking_lux.seleniumTesting.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegisterPage extends BasePage {
    public RegisterPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void open(){
        driver.get("http://localhost:8080/register");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).isDisplayed();
    }

    public void register(String firstName, String lastName, String username, String password, String confirm) throws InterruptedException {
        driver.findElement(By.id("name")).sendKeys(firstName);
        Thread.sleep(1000);
        driver.findElement(By.id("surname")).sendKeys(lastName);
        Thread.sleep(1000);
        driver.findElement(By.id("username")).sendKeys(username);
        Thread.sleep(1000);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(1000);
        driver.findElement(By.id("repeatedPassword")).sendKeys(confirm);
        Thread.sleep(1000);
        driver.findElement(By.id("registerButton")).click();
        Thread.sleep(2000);
    }
}
