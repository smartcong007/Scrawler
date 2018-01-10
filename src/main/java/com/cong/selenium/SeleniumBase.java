package com.cong.selenium;

import com.cong.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengcong on 2017/6/14.
 * 操作selenium的工具类
 */
public class SeleniumBase {

    private static final String CHROME_PATH = Config.getVal("CHROME_PATH");

    private static final String CHROME_DRIVER = Config.getVal("CHROME_DRIVER");

    private static final String SCREENSHOT_PATH = Config.getVal("SCREENSHOT_PATH");

    private static final String current_brower = Config.getVal("CURRENT_BROWER");

    public static WebDriver getCurrentDriver() {

        if (ThreadDriverHolder.getDriver() == null) {

            WebDriver webDriver = null;
            if(current_brower.equals("CHROME")){
                webDriver = createDriver();
            }else  {
                webDriver = createDriverByPhantomJS();
            }
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

    /**
     * 创建基于phantomJS的webdriver
     */
    private static WebDriver createDriverByPhantomJS() {
        WebDriver driver = null;
        DesiredCapabilities sCaps = new DesiredCapabilities();
        sCaps.setJavascriptEnabled(true);
        sCaps.setCapability("takesScreenshot",false);
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.97 Safari/537.11");
        sCaps.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, Config.getVal("PHANTOM_PATH"));
        ArrayList<String> cliArgsCap = new ArrayList<String>();
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        // Control LogLevel for GhostDriver, via CLI arguments
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[] {
                "--logLevel=" + "INFO"
        });
        driver = new PhantomJSDriver(sCaps);
        //设置Driver超时等属性
        driver.manage().timeouts().implicitlyWait(Long.valueOf(Config.getVal("ELEMENT_SEARCH_TIMEOUT")),
                TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(Long.valueOf(Config.getVal("PAGE_LOAD_TIMEOUT")),
                TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(Long.valueOf(Config.getVal("SCRIPT_EXECUTE")),
                TimeUnit.SECONDS);
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

    //给指定web元素作赋值操作
    public static void sendKey(By by, String key) {

        WebElement webElement = getCurrentDriver().findElement(by);
        if (webElement != null) {
            webElement.sendKeys(key);
        } else {
            throw new RuntimeException("元素未找到");
        }

    }

    //元素点击
    public static void click(By by) {

        WebElement webElement = getCurrentDriver().findElement(by);
        if (webElement != null) {
            webElement.click();
        } else {
            throw new RuntimeException("元素未找到");
        }

    }

    static {
        //初始化截图保存路径
        File file = new File(SCREENSHOT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }

    }


}
