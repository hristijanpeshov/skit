package com.hvt.booking_lux.seleniumTesting.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ListingsPage extends BasePage {
    public ListingsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void open(){
        driver.get("http://localhost:8080/accommodation/myListings");
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("listings"))).isDisplayed();
    }

    public void addUnit(String title, String desc, String size, String price, String image) throws InterruptedException {
        driver.findElement(By.cssSelector("[href='/accommodation/1/unit/add']")).click();
        Thread.sleep(2000);
        driver.findElement(By.id("title")).sendKeys(title);
        Thread.sleep(1000);
        driver.findElement(By.id("description")).sendKeys(desc);
        Thread.sleep(1000);
        driver.findElement(By.id("size")).sendKeys(size);
        Thread.sleep(1000);
        driver.findElement(By.id("price")).sendKeys(price);
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("[name='images']")).sendKeys(image);
        Thread.sleep(1000);
        driver.findElements(By.cssSelector("input")).get(5).clear();
        driver.findElements(By.cssSelector("input")).get(5).sendKeys("1");
        Thread.sleep(1000);
        driver.findElement(By.id("submitButton")).click();
    }

    public boolean roomDetailsIsLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".room-details"))).isDisplayed();
    }
}
