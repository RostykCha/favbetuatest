package automationframeworkexample.clients.ui;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
public class DriverManager {

    private final ThreadLocal<WebDriver> holder = new ThreadLocal<>();
    private final WebDriverFactory factory;

    @Autowired
    public DriverManager(WebDriverFactory factory) {
        this.factory = factory;
    }

    public WebDriver getDriver() {
        if (holder.get() == null) holder.set(factory.newChromeDriver());
        return holder.get();
    }

    public void quit() {
        WebDriver d = holder.get();
        if (d != null) {
            d.quit();
            holder.remove();           // guarantees next get() builds fresh driver
        }
    }

    public static void scrollToCenter(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'})", element);
    }

    public static boolean switchToFirstAvailableFrame(WebDriver driver) {
        driver.switchTo().defaultContent();
        return searchFramesRecursively(driver, 0);
    }

    private static boolean searchFramesRecursively(WebDriver driver, int depth) {
        List<WebElement> frames = driver.findElements(By.tagName("iframe"));
        logInfo("Depth " + depth + " – found " + frames.size() + " iframe(s)");
        for (int i = 0; i < frames.size(); i++) {
            WebElement frame = frames.get(i);
            try {
                logInfo("Depth " + depth + " – try frame index " + i);
                driver.switchTo().frame(frame);

                boolean bodyVisible = !driver.findElements(By.tagName("body")).isEmpty();
                if (bodyVisible) {
                    logInfo("Depth " + depth + " – switched to frame index " + i);
                    return true;
                }

                if (searchFramesRecursively(driver, depth + 1)) {
                    return true;
                }

                driver.switchTo().parentFrame();
            } catch (NoSuchFrameException | StaleElementReferenceException e) {
                logInfo("Depth " + depth + " – stale or missing frame, resetting");
                driver.switchTo().defaultContent();
            }
        }
        return false;
    }

    public static String switchToNewTab(WebDriver driver, Duration timeout) {
        String original = driver.getWindowHandle();
        Set<String> oldHandles = driver.getWindowHandles();

        new WebDriverWait(driver, timeout)
                .until(d -> d.getWindowHandles().size() > oldHandles.size());

        Set<String> newHandles = driver.getWindowHandles();
        newHandles.removeAll(oldHandles);
        String newHandle = newHandles.iterator().next();
        driver.switchTo().window(newHandle);
        return original;                       // return original so you can switch back
    }

    public static void closeCurrentAndReturn(WebDriver driver, String originalHandle) {
        driver.close();
        driver.switchTo().window(originalHandle);
    }
}
