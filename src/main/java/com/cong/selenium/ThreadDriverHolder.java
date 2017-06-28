package com.cong.selenium;

import org.openqa.selenium.WebDriver;

/**
 * Created by zhengcong on 2017/6/14.
 */
public class ThreadDriverHolder {

    private static ThreadLocal<WebDriver> webdriver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return webdriver.get();
    }

    public static void setDriver(WebDriver driver) {
        webdriver.set(driver);
    }

    public static void clearDriver() {
        webdriver.remove();
        
    }
}
