package com.cong.selenium;

import com.cong.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhengcong on 2017/6/14.
 * 操作selenium的工具类
 */
public class SeleniumBase {

    private static final String CHROME_PATH = Config.getVal("CHROME_PATH");

    private static final String CHROME_DRIVER = Config.getVal("CHROME_DRIVER");

    private static final String SCREENSHOT_PATH = Config.getVal("SCREENSHOT_PATH");

    public static WebDriver getCurrentDriver() {

        if (ThreadDriverHolder.getDriver() == null) {

            WebDriver webDriver = createDriver();
            if (webDriver != null) {
                ThreadDriverHolder.setDriver(webDriver);
            } else {
                throw new RuntimeException("driver创建失败");
            }

        }
        return ThreadDriverHolder.getDriver();

    }


    /**
     * 创建基于Headless chrome的无代理的webdriver
     */
    private static WebDriver createDriver() {

        DesiredCapabilities dec = DesiredCapabilities.chrome();
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(CHROME_PATH);
        options.addArguments("test-type"); //忽略证书错误
        options.addArguments("headless");// 开启无头模式的关键设置
        options.addArguments("disable-gpu"); //避免现阶段headless chrome可能引发的错误，后期可以去掉此项
        dec.setJavascriptEnabled(true);  //允许selenium执行javascript脚本
        dec.setCapability(ChromeOptions.CAPABILITY, options);

        WebDriver driver = new ChromeDriver(dec);
        return driver;


    }

    /**
     * 创建开启代理的基于headless chrome的webdriver
     */
    private static WebDriver createDriverByProxy() {

        String proxyHost = "";
        String proxyIp = "";
        //TODO :获取代理
        String proxIpAndPort = proxyHost + ":" + proxyIp;
        DesiredCapabilities dec = DesiredCapabilities.chrome();
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxIpAndPort).setSslProxy(proxIpAndPort);
        dec.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        dec.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
        dec.setCapability(CapabilityType.PROXY, proxy);

        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setBinary(CHROME_PATH);
        chromeOptions.addArguments("test-type");
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("disable-gpu");

        dec.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        dec.setJavascriptEnabled(true);

        WebDriver driver = new ChromeDriver(dec);
        return driver;


    }

    public static void quitDriver() {

        WebDriver webDriver = ThreadDriverHolder.getDriver();
        if (webDriver != null) {
            webDriver.quit();
            ThreadDriverHolder.clearDriver();
        }

    }

    public static void takeScreenShot() {

        byte[] bs = ((TakesScreenshot) getCurrentDriver()).getScreenshotAs(OutputType.BYTES);
        if (bs != null && bs.length > 0) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(new File(SCREENSHOT_PATH + "/" + System.currentTimeMillis() + ".gif"));
                fileOutputStream.write(bs);
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public static boolean openPage(String url, ExpectedCondition<Boolean> condition,
                                   long timeOutInSeconds) {

        try {
            //

            getCurrentDriver().get(url);

        } catch (Exception e) {
            //终止等待页面
            if (TimeoutException.class.isInstance(e)) {
                System.out.println(String.format("打开页面%s超时", url));
            }
            throw e;

            //在页面加载出错后的期望判断，考虑页面异步未加载完成但不影响页面操作的情况
//			return condition.apply(getCurrentWebDriver());
        }
        try {
            (new WebDriverWait(getCurrentDriver(), timeOutInSeconds))
                    .until(condition);
            return true;
        } catch (Exception e) {
            //终止等待页面
            if (TimeoutException.class.isInstance(e)) {
                System.out.println(String.format("打开页面%s超时", url));
                throw e;
            }
            return condition.apply(getCurrentDriver());
        }
    }

    public static Boolean checkElement(ExpectedCondition<Boolean> condition) {

        try {
            new WebDriverWait(getCurrentDriver(), 15).until(condition);
            return true;
        } catch (Exception e) {
            if (TimeoutException.class.isInstance(e)) {
                System.out.println("预期元素未出现在页面中");
                return false;
            }
            throw e;
        }

    }

    /**
     * 控制webdriver同步执行脚本
     *
     * @param js         要执行的脚本语句
     * @param webElement 脚本语句中需要传递的webelement对象
     */
    public static void exeuteJS(String js, WebElement webElement) {

        if (webElement == null) {
            ((JavascriptExecutor) getCurrentDriver()).executeScript(js);
        } else {

            ((JavascriptExecutor) getCurrentDriver()).executeScript(js, webElement);
        }

    }


    /**
     * 控制webdriver异步执行脚本
     *
     * @param js         待执行的脚本
     * @param webElement 脚本语句中需要传递的webelement对象
     */
    public static void executeAsyncJS(String js, WebElement webElement) {

        if (StringUtils.isEmpty(js)) {
            return;
        }
        if (webElement == null) {
            ((JavascriptExecutor) getCurrentDriver()).executeAsyncScript(js);
        } else {
            ((JavascriptExecutor) getCurrentDriver()).executeAsyncScript(js, webElement);
        }

    }

    public static void sendKey(By by, String key) {

        WebElement webElement = getCurrentDriver().findElement(by);
        if (webElement != null) {
            webElement.sendKeys(key);
        } else {
            throw new RuntimeException("元素未找到");
        }

    }

    public static void click(By by) {

        WebElement webElement = getCurrentDriver().findElement(by);
        if (webElement != null) {
            webElement.click();
        } else {
            throw new RuntimeException("元素未找到");
        }

    }

    static {

        File file = new File(SCREENSHOT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }

    }


}
