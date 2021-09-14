package com.hvt.booking_lux.seleniumTesting.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected WebDriver driver;
    public final WebDriverWait driverWait;


    public BasePage(WebDriver webDriver) {
        this.driver = webDriver;
        driverWait = new WebDriverWait(webDriver, 10);
    }
}
