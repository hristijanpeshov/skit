package com.hvt.booking_lux.seleniumTesting.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ConfirmationPage extends BasePage {
    public ConfirmationPage(WebDriver webDriver) {
        super(webDriver);
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmation"))).isDisplayed();
    }
}
