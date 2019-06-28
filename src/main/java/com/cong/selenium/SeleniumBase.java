package com.cong.selenium;

import com.cong.common.BrowerTypeEnum;
import com.cong.util.Config;
import com.cong.util.ImageUtil;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by zhengcong on 2017/6/14.
 * 操作selenium的工具类
 */
public class SeleniumBase {

    private static final String CHROME_PATH = Config.getVal("CHROME_PATH");

    private static final String CHROME_DRIVER = Config.getVal("CHROME_DRIVER");

    private static final String SCREENSHOT_PATH = Config.getVal("SCREENSHOT_PATH");

    private static final String current_brower = Config.getVal("CURRENT_BROWER");

    /**
     * 获取WebDriver
     */
    public static WebDriver getCurrentDriver(String... browerType) {

        final String brower = browerType != null && browerType.length == 1 ? browerType[0] : current_brower;
        if (ThreadDriverHolder.getDriver() == null) {

            WebDriver webDriver;
            if (brower.equals(BrowerTypeEnum.GOOGLE_CHROME.getCode())) {
                webDriver = createDriver();
            } else if (brower.equals(BrowerTypeEnum.PHANTOM_JS.getCode())) {
                webDriver = createDriverByPhantomJS();
                webDriver.manage().window().maximize();
            } else {
                throw new IllegalArgumentException("不合法的浏览器类型");
            }
            if (webDriver != null) {
                webDriver.manage().window().maximize();
                ThreadDriverHolder.setDriver(webDriver);
            } else {
                throw new RuntimeException("driver创建失败");
            }

        }
        return ThreadDriverHolder.getDriver();

    }

    /**
     * 为当前Webdriver设置cookie
     */
    public static void addCookie(String cookie) {
        WebDriver webDriver = getCurrentDriver();
        if (webDriver == null) {
            throw new IllegalStateException("webdriver初始化异常");
        }
        String[] cookies = cookie.split("; ");
        for (String s : cookies) {
            String[] tm = s.split("=");
            webDriver.manage().addCookie(new Cookie(tm[0], tm[1]));
        }
    }


    /**
     * 创建基于Headless chrome的无代理的webdriver
     */
    private static WebDriver createDriver() {

        DesiredCapabilities dec = DesiredCapabilities.chrome();
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER);
        ChromeOptions options = new ChromeOptions();
        options.setBinary(CHROME_PATH);
        options.addArguments("disable-infobars");
        //忽略证书错误
        options.addArguments("test-type");
        // 开启无头模式的关键设置
//        options.addArguments("headless");
        //避免现阶段headless chrome可能引发的错误，后期可以去掉此项
//        options.addArguments("disable-gpu");
        //允许selenium执行javascript脚本
        dec.setJavascriptEnabled(true);
        dec.setCapability(ChromeOptions.CAPABILITY, options);

        WebDriver driver = new ChromeDriver(options);
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
        chromeOptions.addArguments("disable-infobars");

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
        sCaps.setCapability("takesScreenshot", false);
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
        sCaps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS, new String[]{
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

    /**
     * 截取当前屏幕中的页面
     */
    public static String takeScreenShot() {

        byte[] bs = ((TakesScreenshot) getCurrentDriver()).getScreenshotAs(OutputType.BYTES);
        String filePath = null;
        if (bs != null && bs.length > 0) {
            FileOutputStream fileOutputStream = null;
            try {
                filePath = SCREENSHOT_PATH + "/" + System.currentTimeMillis() + ".gif";
                fileOutputStream = new FileOutputStream(new File(filePath));
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
        return filePath;

    }

    /**
     * 截取全页面
     */
    public static void takeFullScreenShot() {
        WebDriver webDriver = getCurrentDriver();
        if (webDriver == null) {
            throw new IllegalStateException("webdriver获取失败");
        }
        //获取当前页面的总的高度
        final String jsHeight = "return document.body.clientHeight";
        //获取当前浏览器中可显示的页面的高度
        final String screenHeight = "return window.innerHeight";
        //控制页面滚动
        String scrollJs = "window.scrollTo(0,%d)";
        List<String> tmps = Lists.newArrayList();
        int height = ((Long) exeuteJS(jsHeight, null)).intValue();
        int sHeight = ((Long) exeuteJS(screenHeight, null)).intValue();
        int k = 0;
        while (k * sHeight < height) {
            //每次向下滚动一次就截屏一次
            exeuteJS(String.format(scrollJs, k * sHeight), null);
            tmps.add(takeScreenShot());
            height = ((Long) exeuteJS(jsHeight, null)).intValue();
            k++;
        }
        String filePath = SCREENSHOT_PATH + "/" + System.currentTimeMillis() + ".gif";
        //将所有已保存的截屏片段拼接成完整的页面
        ImageUtil.merge(tmps.toArray(new String[tmps.size()]), "gif", filePath);
        //截取完需要删除截屏片段
        for (String tmp : tmps) {
            new File(tmp).delete();
        }
    }

    /**
     * 打开指定页面
     * @param url 页面链接
     * @param condition 载入页面后需要检查的条件
     * @param timeOutInSeconds 条件校验超时时间
     * @return
     */
    public static boolean openPage(String url, ExpectedCondition<Boolean> condition,
        long timeOutInSeconds) {

        try {
            getCurrentDriver().get(url);
        } catch (Exception e) {
            //终止等待页面
            if (TimeoutException.class.isInstance(e)) {
                System.out.println(String.format("打开页面%s超时", url));
            }
            quitDriver();
            throw e;

            //在页面加载出错后的期望判断，考虑页面异步未加载完成但不影响页面操作的情况
//			return condition.apply(getCurrentWebDriver());
        }
        if (condition == null) {
            return true;
        }
        try {
            (new WebDriverWait(getCurrentDriver(), timeOutInSeconds))
                .until(condition);
            return true;
        } catch (Exception e) {
            //终止等待页面
            quitDriver();
            if (TimeoutException.class.isInstance(e)) {
                System.out.println(String.format("打开页面%s超时", url));
                throw e;
            }
            return condition.apply(getCurrentDriver());
        }
    }

    /**
     * 检测元素可见
     * @param by 待检测元素
     * @return
     */
    public static Boolean checkElementExsist(By by) {

        try {
            new WebDriverWait(getCurrentDriver(), 15).until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (Exception e) {
            if (TimeoutException.class.isInstance(e)) {
                System.out.println("预期元素未出现在页面中");
                return false;
            }
            quitDriver();
            throw e;
        }

    }

    /**
     * 控制webdriver同步执行脚本
     *
     * @param js 要执行的脚本语句
     * @param webElement 脚本语句中需要传递的webelement对象
     */
    public static Object exeuteJS(String js, WebElement webElement) {

        if (webElement == null) {
            return ((JavascriptExecutor) getCurrentDriver()).executeScript(js);
        } else {
            return ((JavascriptExecutor) getCurrentDriver()).executeScript(js, webElement);
        }

    }


    /**
     * 控制webdriver异步执行脚本
     * @param js 待执行的脚本
     * @param webElement 脚本语句中需要传递的webelement对象
     */
    public static Object executeAsyncJS(String js, WebElement webElement) {

        if (StringUtils.isEmpty(js)) {
            throw new IllegalArgumentException();
        }
        if (webElement == null) {
            return ((JavascriptExecutor) getCurrentDriver()).executeAsyncScript(js);
        } else {
            return ((JavascriptExecutor) getCurrentDriver()).executeAsyncScript(js, webElement);
        }

    }


    /**
     * 给元素赋值
     * @param by 待赋值元素
     * @param key 赋值
     */
    public static void sendKey(By by, CharSequence key) {
        if (!checkElementExsist(by)) {
            quitDriver();
            throw new IllegalStateException("元素未找到");
        }
        WebElement webElement = getCurrentDriver().findElement(by);
        if (webElement != null) {
            webElement.sendKeys(key);
        } else {
            quitDriver();
            throw new RuntimeException("元素未找到");
        }

    }


    /**
     * 点击元素
     * @param by 待点击元素
     * @param elementExpectedCondition 可以指定点击前的校验条件
     */
    public static void click(By by, ExpectedCondition elementExpectedCondition) {
        try {
            ExpectedCondition<WebElement> condition =
                elementExpectedCondition == null ? ExpectedConditions.elementToBeClickable(by)
                    : elementExpectedCondition;
            new WebDriverWait(getCurrentDriver(), 15).until(condition);
        } catch (Exception e) {
            quitDriver();
            if (e instanceof TimeoutException) {
                throw new IllegalStateException("等待元素可点击超时");
            }
            throw e;
        }
        WebElement webElement = getCurrentDriver().findElement(by);
        if (webElement != null) {
            webElement.click();
        } else {
            quitDriver();
            throw new RuntimeException("元素未找到");
        }

    }

    /**
     * 根据索引切换iframe
     * @param frameLocate iframe的索引
     * @return
     */
    public static boolean switchIframe(int frameLocate) {
        try {
            new WebDriverWait(getCurrentDriver(), 15)
                .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocate));
            return true;
        } catch (Exception e) {
            quitDriver();
            if (e instanceof TimeoutException) {
                throw new IllegalStateException("切换iframe失败");
            }
            throw e;
        }
    }

    public static void select(By by, String text) {
        if (!checkElementExsist(by)) {
            quitDriver();
            throw new IllegalStateException("元素未找到");
        }
        WebElement webElement = getCurrentDriver().findElement(by);
        Select select = new Select(webElement);
        select.selectByVisibleText(text);
    }

    static {
        //初始化截图保存路径
        File file = new File(SCREENSHOT_PATH);
        if (!file.exists()) {
            file.mkdir();
        }

    }


}
