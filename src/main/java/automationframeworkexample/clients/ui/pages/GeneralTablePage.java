package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.BasePage;
import automationframeworkexample.clients.ui.DriverManager;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope("prototype")
public abstract class GeneralTablePage extends BasePage {

    @FindBy(css = "div[data-role^='event-body-']")
    private List<WebElement> rawRows;                       // fresh list every DOM read

    @Autowired
    protected GeneralTablePage(DriverManager driverManager) {
        super(driverManager);
        logInfo("Opened page with event table");
    }

    public List<EventRow> getRows() {
        return rawRows.stream()
                .map(r -> new EventRow(driver, r))
                .toList();
    }

    /* -------------------------------------------------------------------- helpers */

    private EventRow findRowFlexible(String matchName) {
        String target = EventRow.canon(matchName);
        return getRows().stream()
                .filter(r -> {
                    String key = EventRow.canon(r.getMatchName());
                    return key.equals(target) || key.contains(target) || target.contains(key);
                })
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Match not found: " + matchName));
    }

    /* -------------------------------------------------------------------- read */

    public List<String> getAllFavorites() {
        return getRows().stream()
                .filter(EventRow::isFavorite)
                .map(EventRow::getMatchName)
                .toList();
    }

    /* -------------------------------------------------------------------- bulk */

    public List<String> selectAllAvailableFavorites() {
        logInfo("Select all available favourites");
        List<String> selected = new ArrayList<>();
        for (EventRow row : getRows()) {
            row.selectFavoriteIfNotSelected();
            selected.add(row.getMatchName());
        }
        return selected;
    }

    /* -------------------------------------------------------------------- single row actions */

    public void selectFavoriteByName(String matchName) {
        logInfo(String.format("Select favourite \"%s\"", matchName));
        findRowFlexible(matchName).selectFavoriteIfNotSelected();
    }

    public void removeFavoriteByName(String matchName) {
        logInfo(String.format("Remove favourite \"%s\"", matchName));

        ExpectedCondition<Boolean> deselected = d -> {
            try {
                EventRow row = findRowFlexible(matchName);  // always fresh DOM
                if (row.isFavorite()) {
                    row.deselectFavoriteIfSelected();       // may trigger re-render
                    return false;                           // keep waiting
                }
                return true;                                // star already off
            } catch (StaleElementReferenceException e) {
                return false;                               // retry on stale
            } catch (NoSuchElementException e) {
                return true;                                // row disappeared – treat as removed
            }
        };

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(StaleElementReferenceException.class)
                .until(deselected);
    }
}
