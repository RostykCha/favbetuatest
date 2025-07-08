package automationframeworkexample.clients.ui.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

import static automationframeworkexample.AppConstants.LONG_WAIT;
import static automationframeworkexample.AppConstants.SHORT_WAIT;
import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

public class EventRow {

    @FindBy(css = "[data-role='event-favorite-star']")
    private WebElement favoriteStar;

    @FindBy(css = "[data-role='event-favorite-star-icon']")
    private WebElement starIcon;

    @FindBy(css = "[data-role^='event-participants-name-'] span")
    private List<WebElement> participantSpans;

    private final WebDriver driver;
    private final By rowLocator;

    EventRow(WebDriver driver, WebElement root) {
        this.driver = driver;
        this.rowLocator = By.xpath("//div[@data-role='" + root.getAttribute("data-role") + "']");
        PageFactory.initElements(new DefaultElementLocatorFactory(root), this);
    }

    public static String canon(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFKD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[\\s\\p{Pd}]+", "")
                .toLowerCase(java.util.Locale.ROOT);
    }

    public boolean isFavorite() {
        try {
            String fill = starIcon.getAttribute("fill");
            return fill != null && !fill.equalsIgnoreCase("none") && !fill.isBlank();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    public void selectFavoriteIfNotSelected() {
        if (isFavorite()) return;
        logInfo("Click star to select favourite");
        retryClickUntil(true);
    }

    public void deselectFavoriteIfSelected() {
        if (!isFavorite()) return;
        logInfo("Click star to deselect favourite");
        retryClickUntil(false);
    }

    public List<String> getParticipants() {
        return participantSpans.stream()
                .map(WebElement::getText)
                .map(t -> t.split("\\(")[0].trim())
                .collect(Collectors.toList());
    }

    public String getMatchName() {
        return String.join(" - ", getParticipants());
    }

    private void retryClickUntil(boolean wantSelected) {
        new WebDriverWait(driver, LONG_WAIT)
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    try {
                        favoriteStar.click();
                    } catch (StaleElementReferenceException ignored) {
                        WebElement freshRow = d.findElement(rowLocator);
                        freshRow.findElement(By.cssSelector("[data-role='event-favorite-star']")).click();
                    }
                    return new WebDriverWait(d, SHORT_WAIT)
                            .until(driver1 -> isFavorite() == wantSelected);
                });
    }
}
