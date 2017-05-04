package ru.test;

import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static ru.yandex.qatools.ashot.cropper.indent.IndentFilerFactory.blur;

/**
 * Класс
 * =====
 *
 * @author EMurzakaev@it.ru.
 */
public class ScreenShooter {

    private static AShot ashot;

    private static WebDriver driver;

    public static void init(WebDriver driver) {
        ScreenShooter.driver = driver;
        ashot = new AShot().imageCropper(new IndentCropper().addIndentFilter(blur()));
    }

    public static void takeScreenshot(String title) {
        Screenshot screenshot = ashot.takeScreenshot(driver);
        saveScreenshot(screenshot, title);
    }

    @Attachment(value = "{1}", type = "image/png")
    private static byte[] saveScreenshot(Screenshot screenshot, String title) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(screenshot.getImage(), "png", stream);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return stream.toByteArray();
    }

}
