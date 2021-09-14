package com.hvt.booking_lux.seleniumTesting.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ManageAccountPage extends BasePage {
    public ManageAccountPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void open(){
        driver.get("http://localhost:8080/user/manage");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".account__details"))).isDisplayed();
    }

    public void showStats(){
        driver.findElement(By.id("btnShowStatistics")).click();
    }

    public boolean statsLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("myBarChartVisitors"))).isDisplayed();
    }

    public void changeName(String name) throws InterruptedException {
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys(name);
        Thread.sleep(2000);
        driver.findElement(By.id("saveInfoButton")).click();
        Thread.sleep(2000);
    }

    public String getName(){
        WebElement nameElement = driver.findElement(By.id("firstName"));
        return nameElement.getText();
    }
}
