package com.cong.test;

import static com.cong.selenium.SeleniumBase.click;
import static com.cong.selenium.SeleniumBase.openPage;
import static com.cong.selenium.SeleniumBase.sendKey;
import static com.cong.selenium.SeleniumBase.takeScreenShot;

import com.cong.selenium.SeleniumBase;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by zhengcong on 2017/6/14.
 */
public class SeleniumTest {

    @Test
    public void test() {

        openPage("https://www.baidu.com", webDriver -> webDriver.findElement(By.id("su")) != null, 15);
        takeScreenShot();

        sendKey(By.id("kw"), "headless chrome");
        takeScreenShot();

        click(By.id("su"), null);
        if (SeleniumBase.checkElementExsist(By.id("content_right"))) {
            SeleniumBase.exeuteJS("window.scrollTo(0, document.body.scrollHeight)", null);
            takeScreenShot();
        }
        SeleniumBase.quitDriver();


    }

    @Test
    public void test2() {
    }

    @Test
    public void test3() throws InterruptedException {

        String cookie = "LastMenuID=101001003784e9c7a-c1f1-49b5-9638-23d8bdc237d01501028; ASP.NET_SessionId=eb5mmyzd2dkzsvi0tdyqndex; Api.Client.Current.UserKey_00000000-0000-0000-0000-000000000000=1fe757be-7603-4da8-ab1b-e25ad852d99b; Api.Client.Current.UserSecret_00000000-0000-0000-0000-000000000000=F0C8D16F1DD3DD8C8361BC810DC5F01129411BFC4EBFB4C3D3C1C5DD9B5736E18769286761C73518; Api.Client.Current.UserKey_784e9c7a-c1f1-49b5-9638-23d8bdc237d0=1fe757be-7603-4da8-ab1b-e25ad852d99b; Api.Client.Current.UserSecret_784e9c7a-c1f1-49b5-9638-23d8bdc237d0=F0C8D16F1DD3DD8C8361BC810DC5F01129411BFC4EBFB4C3D3C1C5DD9B5736E18769286761C73518; EasySystem_CYT=EC7C0240CBB5CBDBDC2DDB78C6D0837736BFB5812470E5A932E776591F7A0702411283B1A48C26337C858775AB14FA91E18335B5B2569A8A3CE76CD860925867EBD791211911FD3EB1392E51314648C1E7AD8056B6FBAFB84583E89BFC20952D9ED3A1C6; OPUserID=0";
        if (openPage("https://das.yichehuoban.cn/InforManage/News/TemplateNews.aspx?source=1", null, 0)) {
            SeleniumBase.addCookie(cookie);
        }
        openPage("https://das.yichehuoban.cn/InforManage/News/TemplateNews.aspx?source=1",
            webDriver -> webDriver.findElement(By.id("btnEditDetail")) != null, 15);
        //选择促销车型
        click(By.xpath("//label[@csshowname='贵士']/../input[1]"), null);
        //设置促销起始时间
        click(By.id("txtDateTimeBegin"), ExpectedConditions.invisibilityOfElementLocated(By.id("TLoading")));
        if (SeleniumBase.switchIframe(1)) {
            click(By.xpath("//*[@id='dpClearInput']"), null);
        }
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        sendKey(By.id("txtDateTimeBegin"), "2019-08-01");
        //设置促销结束时间
        click(By.id("txtDateTimeEnd"), null);
        if (SeleniumBase.switchIframe(1)) {
            click(By.xpath("//*[@id=\"dpClearInput\"]"), null);
        }
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        sendKey(By.id("txtDateTimeEnd"), "2019-08-30");

        click(By.id("txtDateTimeBegin"), null);
        click(By.id("txtDateTimeEnd"), null);
        if (SeleniumBase.switchIframe(1)) {
            click(By.id("dpOkInput"), null);
        }
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        click(By.xpath("//input[@name='chklYearType'][@value='2015']"), ExpectedConditions.invisibilityOfElementLocated(By.id("TLoading")));
        sendKey(By.id("txtMoney"), "2");
        click(By.id("allColor"), null);
        click(By.id("btnEditDetail"), null);

//        click(By.xpath("//td[@title='3.5L CVT SL 2015款']/div[1]/input[2]"), null);
        SeleniumBase.select(By.xpath("//*[@id=\"listInfo\"]/tr[2]/td[7]/select"), "少量现车");
        SeleniumBase.takeFullScreenShot();
        SeleniumBase.quitDriver();
        //*[@id="listInfo"]/tr[2]/td[1]/div/label
        //*[@id="listInfo"]/tr[2]/td[4]/input
        //*[@id="listInfo"]/tr[2]/td[7]/select


    }



}
