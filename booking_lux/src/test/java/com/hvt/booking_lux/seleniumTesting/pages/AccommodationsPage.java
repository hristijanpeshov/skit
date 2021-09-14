package com.hvt.booking_lux.seleniumTesting.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AccommodationsPage extends BasePage {
    public AccommodationsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void open(){
        driver.get("http://localhost:8080/accommodation");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".rooms"))).isDisplayed();
    }

    public void bookUnit() throws InterruptedException {
        driver.findElement(By.cssSelector("[href='/accommodation/1']")).click();
        driver.findElement(By.cssSelector("[data-id='1']")).click();
        Thread.sleep(2000);
        WebElement checkInDate = driver.findElement(By.cssSelector("[name='checkInDate']"));
        checkInDate.sendKeys("11122021");
        Thread.sleep(2000);
        WebElement checkOutDate = driver.findElement(By.cssSelector("[name='checkOutDate']"));
        checkOutDate.sendKeys("14122021");
        driver.findElement(By.id("bookButton")).click();
    }

}
