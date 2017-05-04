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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestClass {

    private WebDriver driver;

    @BeforeTest
    public void beforeTest() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Features({"Жанры"})
    @Stories({"Проверка жанров"})
    @Title("Проверка жанров")
    @Description(
            value = "Заходим на главную и пробует открыть Жанры",
            type = DescriptionType.MARKDOWN
    )
    @Severity(SeverityLevel.CRITICAL)
    @Test
    public void test() throws Throwable {
        driver.get("https://music.yandex.ru/");
        List<String> genres = getGenres();
        for (String genre : genres.subList(0,2)) {
            List<String> tracks = getTracks(genre);
            System.out.println("Жанр : " + genre + " Треки : " + tracks);
        }
    }

    @Step("Получение жанров")
    public List<String> getGenres() {
        driver.findElement(By.xpath("//div[text()='Жанры']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='head__subnav nav__subnav']")));
        ScreenShooter.takeScreenshot("Жанры");
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='nav__sub-item'][position() > 1 and position() < last()]/a/span"));
        List<String> genres = new ArrayList<String>();
        for (WebElement element1 : list) {
            genres.add(element1.getText());
        }
        attachText("Жанры", genres.toString());
        return genres;
    }

    @Step("Получение списка треков жанра {0}")
    public List<String> getTracks(String genre) throws Throwable {
        List<String> trackNames = new ArrayList<String>();
        WebElement webElement = driver.findElement(By.xpath("//span[@class='button__label' and text()='" + genre + "']"));
        ScreenShooter.takeScreenshot(genre);
        webElement.click();
        Thread.sleep(1500);
        List<WebElement> tracks = driver.findElements(By.xpath("//div[@class='track__name-wrap']/a"));
        for (WebElement element : tracks) {
            trackNames.add(element.getText());
        }
        attachText("Треки жанра " + genre, trackNames.toString());
        Assert.assertEquals(tracks.size(), 10);
        return trackNames;
    }

    @Attachment(value = "{0}")
    private String attachText(String title, String text) {
        return text;
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

}
