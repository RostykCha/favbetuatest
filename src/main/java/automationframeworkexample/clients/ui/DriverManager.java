package automationframeworkexample.clients.ui;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DriverManager {

    private final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    private final WebDriverFactory factory;

    @Autowired
    public DriverManager(WebDriverFactory factory) {
        this.factory = factory;
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

                if (!driver.findElements(By.tagName("body")).isEmpty()) {
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
        return original;
    }

    public static void closeCurrentAndReturn(WebDriver driver, String originalHandle) {
        driver.close();
        driver.switchTo().window(originalHandle);
    }

    public WebDriver getDriver() {
        if (tlDriver.get() == null) {
            tlDriver.set(factory.build());
        }
        return tlDriver.get();
    }

    public void startFreshDriver() {
        quit();
        tlDriver.set(factory.build());
    }

    public void quit() {
        WebDriver d = tlDriver.get();
        if (d != null) {
            d.quit();
            tlDriver.remove();
        }
    }
}

