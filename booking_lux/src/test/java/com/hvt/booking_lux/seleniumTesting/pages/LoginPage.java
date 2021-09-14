package com.hvt.booking_lux.seleniumTesting.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {
    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void open() {
        driver.get("http://localhost:8080/user/login");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).isDisplayed();
    }

    public void login(String user, String password) throws InterruptedException {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(user);
        Thread.sleep(2000);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(2000);
        driver.findElement(By.cssSelector(".btn.primary-btn")).click();
        Thread.sleep(2000);
    }

    public String getErrorMesage(){
        WebElement errorPage = driver.findElement(By.cssSelector("span.text-danger"));
        return errorPage.getText();
    }
}
