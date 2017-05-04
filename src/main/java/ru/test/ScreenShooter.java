package ru.test;

import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ScreenShooter {

    private static WebDriver driver;

    private static AShot aShot = new AShot();

    public static void init(WebDriver webDriver) {
        driver = webDriver;
    }

    public static void takeScreenshot(String title) {
        Screenshot screenshot = aShot.takeScreenshot(driver);
        saveScreenshot(title, screenshot);
    }

    @Attachment(value = "{0}", type = "image/png")
    private static byte[] saveScreenshot(String title, Screenshot screenshot) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(screenshot.getImage(), "png", stream);
        } catch(IOException exception) {
            exception.printStackTrace();
        }
        return stream.toByteArray();
    }

}
