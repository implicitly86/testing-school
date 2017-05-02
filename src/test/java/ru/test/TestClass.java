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

    @Test
    public void test() throws Throwable {
        driver.get("https://music.yandex.ru/");
        List<String> genres = getGenres();
        for (String genre : genres) {
            List<String> tracks = getTracks(genre);
            System.out.println("Жанр : " + genre + " Треки : " + tracks);
        }
    }

    public List<String> getGenres() {
        driver.findElement(By.xpath("//div[text()='Жанры']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@class='head__subnav nav__subnav']")));
        List<WebElement> list = driver.findElements(By.xpath("//div[@class='nav__sub-item'][position() > 1 and position() < last()]/a/span"));
        List<String> genres = new ArrayList<String>();
        for (WebElement element1 : list) {
            genres.add(element1.getText());
        }
        return genres;
    }

    public List<String> getTracks(String genre) throws Throwable {
        List<String> trackNames = new ArrayList<String>();
        driver.findElement(By.xpath("//span[@class='button__label' and text()='" + genre + "']")).click();
        Thread.sleep(1500);
        List<WebElement> tracks = driver.findElements(By.xpath("//div[@class='track__name-wrap']/a"));
        for (WebElement element : tracks) {
            trackNames.add(element.getText());
        }
        Assert.assertEquals(tracks.size(), 10);
        return trackNames;
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }

}
