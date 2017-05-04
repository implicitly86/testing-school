package ru.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.DescriptionType;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TestClass {

    private WebDriver driver;

    @BeforeTest
    public void beforeTest() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        ScreenShooter.init(driver);
    }

    @Features({"Жанры"})
    @Stories({"Поиск по жанрам"})
    @Description(
            value = "Описание теста",
            type = DescriptionType.MARKDOWN
    )
    @Title("Поиск по жанрам")
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void test() throws Throwable {
        driver.get("https://music.yandex.ru/");
        List<String> genres = getGenres();
        for (String genre : genres.subList(0, 1)) {
            List<String> tracks = getTracks(genre);
        }
    }

    @Step("Получение жанров")
    public List<String> getGenres() {
        driver.findElement(By.xpath("//div[text()='Жанры']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='head__subnav nav__subnav']")));
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='nav__sub-item'][position() > 1 and position() < last()]/a/span"));
        return list.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Получение треков из жанра \"{0}\"")
    public List<String> getTracks(String genre) throws Throwable {
        List<String> trackNames = new ArrayList<String>();
        driver.findElement(By.xpath("//span[@class='button__label' and text()='" + genre + "']")).click();
        Thread.sleep(1500);
        List<WebElement> tracks = driver.findElements(By.xpath("//div[@class='track__name-wrap']/a"));
        for (WebElement element : tracks) {
            trackNames.add(element.getText());
        }
        attachText(genre, trackNames.toString());
        ScreenShooter.takeScreenshot(genre);
        Assert.assertFalse(tracks.isEmpty());
        return trackNames;
    }

    @Attachment(value = "{0}")
    public String attachText(String title, String text) {
        return text;
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

}
