package com.cong.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUtil {

    public static void main(String[] args) {
        // 设置Chrome浏览器驱动路径
        System.setProperty("webdriver.chrome.driver", "/Users/11108854/Downloads/chromedriver");
        // 创建Chrome浏览器驱动
        WebDriver driver = new ChromeDriver();
        // 打开百度首页
        driver.get("https://www.baidu.com/");
        // 定位搜索框元素
        WebElement searchBox = driver.findElement(By.id("kw"));
        // 在搜索框中输入关键字
        searchBox.sendKeys("chatgpt");
        // 点击搜索按钮
        WebElement searchButton = driver.findElement(By.id("su"));
        searchButton.click();
        // 等待搜索结果页面加载完成
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement searchResult = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("content_left")));
        // 截屏保存到本地
        try {
            File screenshot = ((org.openqa.selenium.TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
            BufferedImage fullImg = ImageIO.read(screenshot);
//            BufferedImage searchResultImg = fullImg.getSubimage(searchResult.getLocation().getX(), searchResult.getLocation().getY(), searchResult.getSize().getWidth(), searchResult.getSize().getHeight());
            ImageIO.write(fullImg, "png", new File("searchResult.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 关闭浏览器驱动
        driver.quit();
    }
}
