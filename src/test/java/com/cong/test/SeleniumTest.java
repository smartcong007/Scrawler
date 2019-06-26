package com.cong.test;

import com.cong.selenium.SeleniumBase;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

/**
 * Created by zhengcong on 2017/6/14.
 */
public class SeleniumTest {

    @Test
    public void test() {

        SeleniumBase.openPage("https://www.baidu.com", webDriver -> webDriver.findElement(By.id("su")) != null, 15);
        SeleniumBase.takeScreenShot();

        SeleniumBase.sendKey(By.id("kw"), "headless chrome");
        SeleniumBase.takeScreenShot();

        SeleniumBase.click(By.id("su"));
        if (SeleniumBase.checkElement(webDriver -> webDriver.findElement(By.id("content_right")) != null)) {
            SeleniumBase.exeuteJS("window.scrollTo(0, document.body.scrollHeight)", null);
            SeleniumBase.takeScreenShot();
        }
        SeleniumBase.quitDriver();


    }

    @Test
    public void test2() {
        String cookie = "LastMenuID=101001003784e9c7a-c1f1-49b5-9638-23d8bdc237d01501028; ASP.NET_SessionId=ql1d0jgth4sm1wkrtlhdpr1j; Api.Client.Current.UserKey_00000000-0000-0000-0000-000000000000=95774285-b268-496f-ae3e-54a7960bc759; Api.Client.Current.UserSecret_00000000-0000-0000-0000-000000000000=A2A2B90D98D19A2C3D3232AAAE70AD31777474ADB135E59E028A17267E4B2CAF1EE5FC71FAE09245; Api.Client.Current.UserKey_784e9c7a-c1f1-49b5-9638-23d8bdc237d0=95774285-b268-496f-ae3e-54a7960bc759; Api.Client.Current.UserSecret_784e9c7a-c1f1-49b5-9638-23d8bdc237d0=A2A2B90D98D19A2C3D3232AAAE70AD31777474ADB135E59E028A17267E4B2CAF1EE5FC71FAE09245; EasySystem_CYT=D1E76CE0C3FEB62E4A9B25A8B3E2AD9B2E28894BED190126B8A5273EC10E42B179A003CF24636823990671BC8531A850B645A2AB9DC13837AACD5F8EC52B6D63D1C057B87BF141B96D03934606E72B550D79BA2038AE5D36FFF06C75D7BC08043382B59E; OPUserID=0";
        WebDriver webDriver = SeleniumBase.getCurrentDriver();
        String[] cookies = cookie.split("; ");
        for (String s : cookies) {
            String[] tm = s.split("=");
            webDriver.manage().addCookie(new Cookie(tm[0], tm[1]));
        }
    }

    @Test
    public void test3() throws InterruptedException {

        String cookie = "LastMenuID=101001003784e9c7a-c1f1-49b5-9638-23d8bdc237d01501028; ASP.NET_SessionId=eb5mmyzd2dkzsvi0tdyqndex; Api.Client.Current.UserKey_00000000-0000-0000-0000-000000000000=1fe757be-7603-4da8-ab1b-e25ad852d99b; Api.Client.Current.UserSecret_00000000-0000-0000-0000-000000000000=F0C8D16F1DD3DD8C8361BC810DC5F01129411BFC4EBFB4C3D3C1C5DD9B5736E18769286761C73518; Api.Client.Current.UserKey_784e9c7a-c1f1-49b5-9638-23d8bdc237d0=1fe757be-7603-4da8-ab1b-e25ad852d99b; Api.Client.Current.UserSecret_784e9c7a-c1f1-49b5-9638-23d8bdc237d0=F0C8D16F1DD3DD8C8361BC810DC5F01129411BFC4EBFB4C3D3C1C5DD9B5736E18769286761C73518; EasySystem_CYT=EC7C0240CBB5CBDBDC2DDB78C6D0837736BFB5812470E5A932E776591F7A0702411283B1A48C26337C858775AB14FA91E18335B5B2569A8A3CE76CD860925867EBD791211911FD3EB1392E51314648C1E7AD8056B6FBAFB84583E89BFC20952D9ED3A1C6; OPUserID=0";
        if (SeleniumBase.openPage("https://das.yichehuoban.cn/InforManage/News/TemplateNews.aspx?source=1",null, 0)) {
            SeleniumBase.addCookie(cookie);
        }
        SeleniumBase.openPage("https://das.yichehuoban.cn/InforManage/News/TemplateNews.aspx?source=1",
            webDriver -> webDriver.findElement(By.id("btnEditDetail")) != null, 15);
        if (SeleniumBase.checkElement(webDriver -> webDriver.findElement(By.id("2158")) != null)) {
            SeleniumBase.sendKey(By.id("2158"), Keys.SPACE);
        }
        Thread.sleep(2000);
        if (SeleniumBase.checkElement(webDriver -> webDriver.findElement(By.id("txtDateTimeBegin"))!=null)) {
            SeleniumBase.click(By.id("txtDateTimeBegin"));
        }
        Thread.sleep(2000);
        SeleniumBase.getCurrentDriver().switchTo().frame(1);
        if (SeleniumBase
            .checkElement(webDriver -> webDriver.findElement(By.xpath("//*[@id=\"dpClearInput\"]")) != null)) {
            SeleniumBase.click(By.xpath("//*[@id=\"dpClearInput\"]"));
        }
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        SeleniumBase.sendKey(By.id("txtDateTimeBegin"), "2019-08-01");
        if (SeleniumBase.checkElement(webDriver -> webDriver.findElement(By.id("txtDateTimeEnd")) != null)) {
            SeleniumBase.click(By.id("txtDateTimeEnd"));
        }
        Thread.sleep(2000);
        SeleniumBase.getCurrentDriver().switchTo().frame(1);
        if (SeleniumBase
            .checkElement(webDriver -> webDriver.findElement(By.xpath("//*[@id=\"dpClearInput\"]")) != null)) {
            SeleniumBase.click(By.xpath("//*[@id=\"dpClearInput\"]"));
        }
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        SeleniumBase.sendKey(By.id("txtDateTimeEnd"), "2019-08-30");
        SeleniumBase.click(By.id("txtDateTimeBegin"));
        SeleniumBase.click(By.id("txtDateTimeEnd"));
        Thread.sleep(2000);
        SeleniumBase.getCurrentDriver().switchTo().frame(1);
        if (SeleniumBase.checkElement(webDriver -> webDriver.findElement(By.id("dpOkInput")) != null)) {
            SeleniumBase.click(By.id("dpOkInput"));
        }
        Thread.sleep(2000);
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        if (SeleniumBase.checkElement(
            webDriver -> webDriver.findElement(By.xpath("//input[@value='113864'][@type='checkbox']")) != null)) {
            SeleniumBase.sendKey(By.xpath("//input[@value='113864'][@type='checkbox']"), Keys.SPACE);
        }

        SeleniumBase.sendKey(By.id("txtMoney"), "2");
        SeleniumBase.click(By.id("allColor"));
        if (SeleniumBase.checkElement(webDriver -> webDriver.findElement(By.id("btnEditDetail"))!=null)) {
            SeleniumBase.click(By.id("btnEditDetail"));
        }
        SeleniumBase.takeFullScreenShot();
        SeleniumBase.quitDriver();

    }


}
