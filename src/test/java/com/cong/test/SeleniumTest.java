package com.cong.test;

import com.cong.selenium.SeleniumBase;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by zhengcong on 2017/6/14.
 */
public class SeleniumTest {

    @Test
    public void test() {

        WebDriver driver = SeleniumBase.getCurrentDriver();
        SeleniumBase.openPage("https://www.baidu.com", webDriver -> webDriver.findElement(By.id("su")) != null, 15);
        SeleniumBase.takeScreenShot();

        SeleniumBase.sendKey(By.id("kw"), "headless chrome");
        SeleniumBase.takeScreenShot();

//        SeleniumBase.click(By.id("su"));
//        if (SeleniumBase.checkElement(webDriver -> webDriver.findElement(By.id("content_right")) != null)) {
//            SeleniumBase.exeuteJS("window.scrollTo(0, document.body.scrollHeight)", null);
//            SeleniumBase.takeScreenShot();
//        }
        SeleniumBase.quitDriver();


    }



}
