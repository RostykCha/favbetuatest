package automationframeworkexample.clients.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

public class EventRow {

    private static final By STAR_CONTAINER = By.cssSelector("[data-role='event-favorite-star']");
    private static final By STAR_ICON = By.cssSelector("[data-role='event-favorite-star-icon']");
    private static final By PARTICIPANTS = By.cssSelector("[data-role^='event-participants-name-'] span");
    private static final Duration SHORT = Duration.ofSeconds(2);
    private static final By STAR = By.cssSelector("[data-role='event-favorite-star']");
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private final WebDriver driver;
    private final WebElement root;

    EventRow(WebDriver driver, WebElement root) {
        this.driver = driver;
        this.root = root;
    }

    /* locale-independent normalisation helper (used by GeneralTablePage) */
    static String canon(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFKD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[\\s\\p{Pd}]+", "")
                .toLowerCase(java.util.Locale.ROOT);
    }

    private WebElement starContainer() {
        return root.findElement(STAR_CONTAINER);
    }

    private WebElement starIcon() {
        return root.findElement(STAR_ICON);
    }

    public boolean isFavorite() {
        String fill = starIcon().getAttribute("fill");
        return fill != null && !fill.equalsIgnoreCase("none") && !fill.isBlank();
    }

    public void selectFavoriteIfNotSelected() {
        if (isFavorite()) return;
        logInfo("Click star to select favourite");
        starContainer().click();
        new WebDriverWait(driver, SHORT).until(d -> isFavorite());
    }

    public void deselectFavoriteIfSelected() {
        if (!isFavorite()) return;
        logInfo("Click star to deselect favourite");

        new WebDriverWait(driver, TIMEOUT)
                .ignoring(StaleElementReferenceException.class, NoSuchElementException.class)
                .until(d -> {
                    d.switchTo().defaultContent();      //   ←  frame may have been rebuilt
                    d.switchTo().frame(0);              //      re-enter first frame
                    d.findElement(STAR).click();        //      fresh element every poll
                    return !isFavorite();               //      loop until deselected
                });
    }

    private void sleep(Duration d) {
        try {
            Thread.sleep(d.toMillis());
        } catch (InterruptedException ignored) {
        }
    }

    public List<String> getParticipants() {
        return root.findElements(PARTICIPANTS).stream()
                .map(WebElement::getText)
                .map(t -> t.split("\\(")[0].trim())
                .collect(Collectors.toList());
    }

    public String getMatchName() {
        return String.join(" - ", getParticipants());
    }
}
