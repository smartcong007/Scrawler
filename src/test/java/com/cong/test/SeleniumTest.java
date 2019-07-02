package com.cong.test;

import static com.cong.selenium.SeleniumBase.checkElementExsist;
import static com.cong.selenium.SeleniumBase.click;
import static com.cong.selenium.SeleniumBase.getWebElements;
import static com.cong.selenium.SeleniumBase.openPage;
import static com.cong.selenium.SeleniumBase.quitDriver;
import static com.cong.selenium.SeleniumBase.select;
import static com.cong.selenium.SeleniumBase.sendKey;
import static com.cong.selenium.SeleniumBase.takeFullScreenShot;
import static com.cong.selenium.SeleniumBase.takeScreenShot;
import static com.cong.selenium.SeleniumBase.waitForDialogDisappear;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cong.selenium.SeleniumBase;
import java.util.List;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
        quitDriver();


    }

    @Test
    public void test2() {
    }

    @Test
    public void test3() throws InterruptedException {

        String article = "{\n"
            + "    \"促销车型\": \"奇骏\",\n"
            + "    \"促销时间\": \"2019-08-01/2019-08-30\",\n"
            + "    \"促销车款\": [\n"
            + "        {\n"
            + "            \"车款\": \"2.0L CVT 两驱 时尚版 5座 2019款\",\n"
            + "            \"优惠金额\": \"1\",\n"
            + "            \"库存状态\": \"少量现车\"\n"
            + "        }\n"
            + "    ],\n"
            + "    \"新闻标题\": \"广汇弘帆日产奇骏优惠高达2.6万元\",\n"
            + "    \"短标题\": \"日产奇骏优惠高达2.6万元\",\n"
            + "    \"导语\": \"广汇弘帆日产奇骏优惠高达2.6万元，感兴趣的朋友可以到店咨询购买，具体优惠信息如下：\",\n"
            + "    \"礼包\": {\n"
            + "        \"礼包总价值\": \"3000\",\n"
            + "        \"礼包内容\": {\n"
            + "            \"汽车用品\": \"超强 防滑垫\",\n"
            + "            \"油卡\": \"3\",\n"
            + "            \"商业险\": \"1年\",\n"
            + "            \"交强险\": \"1年\",\n"
            + "            \"购置税\": \"赠送\",\n"
            + "            \"保养\": \"500元\"\n"
            + "        }\n"
            + "    },\n"
            + "    \"添加信息\": [\n"
            + "        \"在新闻内容下方添加保养信息\",\n"
            + "        \"在新闻内容下方添加公司地址\",\n"
            + "        \"在新闻内容下方添加地图名片\"\n"
            + "    ]\n"
            + "}";

        JSONObject jsonObject = JSON.parseObject(article);

        String cookie = "LastMenuID=101001003784e9c7a-c1f1-49b5-9638-23d8bdc237d01501028; ASP.NET_SessionId=eb5mmyzd2dkzsvi0tdyqndex; OPUserID=0; Api.Client.Current.UserKey_00000000-0000-0000-0000-000000000000=c3b57fd2-9047-48f8-bcd8-7a961985fe58; Api.Client.Current.UserSecret_00000000-0000-0000-0000-000000000000=600B2C0D3B5A111FE2F6FB2F0840E444DB89EA477EEC5AB7DE78059DBBAC73C17AC73B8FBC2218DB; Api.Client.Current.UserKey_784e9c7a-c1f1-49b5-9638-23d8bdc237d0=c3b57fd2-9047-48f8-bcd8-7a961985fe58; Api.Client.Current.UserSecret_784e9c7a-c1f1-49b5-9638-23d8bdc237d0=600B2C0D3B5A111FE2F6FB2F0840E444DB89EA477EEC5AB7DE78059DBBAC73C17AC73B8FBC2218DB; EasySystem_CYT=F16D11E1B65718E1C80A73A0CCD4ECBFEAA29F84C1B416B50EA02ADD6AFA9B776F3F67FE0600B71EF481B3A46D9F7DB6F01C95167D34AC201F73F75D83053B704EBFB40251E9BD262C8365DE5893B7F68C2BF3335594AB34886B56AEE75CBCE7DCECF0D9";
        if (openPage("https://das.yichehuoban.cn/InforManage/News/TemplateNews.aspx?source=1", null, 0)) {
            SeleniumBase.addCookie(cookie);
        }
        openPage("https://das.yichehuoban.cn/InforManage/News/TemplateNews.aspx?source=1",
            webDriver -> webDriver.findElement(By.id("btnEditDetail")) != null, 15);
        //选择促销车型
        click(By.xpath(String.format("//label[@csshowname='%s']/../input[1]", jsonObject.getString("促销车型"))), null);
        //设置促销起始时间
        String[] dates = jsonObject.getString("促销时间").split("/");
        click(By.id("txtDateTimeBegin"), ExpectedConditions.invisibilityOfElementLocated(By.id("TLoading")));
        if (SeleniumBase.switchIframe(1)) {
            click(By.xpath("//*[@id='dpClearInput']"), null);
        }
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        sendKey(By.id("txtDateTimeBegin"), dates[0]);
        //设置促销结束时间
        click(By.id("txtDateTimeEnd"), null);
        if (SeleniumBase.switchIframe(1)) {
            click(By.xpath("//*[@id=\"dpClearInput\"]"), null);
        }
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        sendKey(By.id("txtDateTimeEnd"), dates[1]);

        click(By.id("txtDateTimeBegin"), null);
        click(By.id("txtDateTimeEnd"), null);
        if (SeleniumBase.switchIframe(1)) {
            click(By.id("dpOkInput"), null);
        }
        SeleniumBase.getCurrentDriver().switchTo().defaultContent();
        waitForDialogDisappear(By.id("TLoading"));
        //先反选所有年份
        List<WebElement> years = getWebElements(By.xpath("//input[@name='chklYearType'][@type='checkbox']"));
        for (WebElement year:years) {
            if (year.isSelected()) {
                year.click();
            }
        }

        //选中车款
        JSONArray promotionModel = jsonObject.getJSONArray("促销车款");
        for (int i = 0; i < promotionModel.size(); i++) {
            JSONObject model = promotionModel.getJSONObject(i);
            String modeName = model.getString("车款");
            click(By.xpath(String.format("//td[@title='%s']/div/input[2]", modeName)), null);
        }

        sendKey(By.id("txtMoney"), "2");
        click(By.id("allColor"), null);
        //编辑详细
        click(By.id("btnEditDetail"), null);
        //填写车款详情表格
        for (int i = 0; i < promotionModel.size(); i++) {
            JSONObject model = promotionModel.getJSONObject(i);
            String tdMoney = model.getString("优惠金额");
            String modelName = model.getString("车款");
            String storage = model.getString("库存状态");
            sendKey(By.xpath(String.format("//td[@title='%s']/../td[4]/input[1]", modelName)), tdMoney);
            select(By.xpath(String.format("//td[@title='%s']/../td[7]/select", modelName)), storage);
        }
        waitForDialogDisappear(By.id("BitDoMsg"));

        sendKey(By.id("title_article"), jsonObject.getString("新闻标题"));
        sendKey(By.id("shorttitle_article"), jsonObject.getString("短标题"));
        sendKey(By.id("txtLead"), jsonObject.getString("导语"));
        if (jsonObject.get("礼包") != null) {
            click(By.id("btnGift"), null);
            if (checkElementExsist(By.id("Submit2"))) {
                click(By.id("Submit2"), null);
            }
            waitForDialogDisappear(By.id("BitDoMsg"));
            JSONObject gift = jsonObject.getJSONObject("礼包");
            sendKey(By.id("txtPrice"), gift.getString("礼包总价值"));
            click(By.id("gift1"), null);
            click(By.id("addMerChandise"), null);
            click(By.xpath("//td[text()='东风日产达芬奇系列抱枕']/../td[1]/input"), null);
            click(By.id("btnSelect"), null);
        }

        takeFullScreenShot();
        quitDriver();
        //*[@id="listInfo"]/tr[2]/td[1]/div/label
        //*[@id="listInfo"]/tr[2]/td[4]/input
        //*[@id="listInfo"]/tr[2]/td[7]/select

    }


}
