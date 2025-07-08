package automationframeworkexample.clients.ui.pages;

import automationframeworkexample.clients.ui.BasePage;
import automationframeworkexample.clients.ui.DriverManager;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static automationframeworkexample.clients.ui.utils.AppConstants.LONG_WAIT;
import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;

@Component
@Scope("prototype")
public abstract class GeneralTablePage extends BasePage {

    @FindBy(css = "div[data-role^='event-body-']")
    private List<WebElement> rawRows;

    @FindBy(xpath = "//div[@class='BuJhQ QBFqm']")
    private WebElement tableBody;


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


    public List<String> getAllFavorites() {
        return getRows().stream()
                .filter(EventRow::isFavorite)
                .map(EventRow::getMatchName)
                .toList();
    }

    public List<String> selectAllAvailableFavorites() {
        logInfo("Select all available favourites");
        List<String> selected = new ArrayList<>();
        for (EventRow row : getRows()) {
            row.selectFavoriteIfNotSelected();
            selected.add(row.getMatchName());
        }
        return selected;
    }


    public void selectFavoriteByName(String matchName) {
        logInfo(String.format("Select favourite \"%s\"", matchName));
        findRowFlexible(matchName).selectFavoriteIfNotSelected();
    }

    public void removeFavoriteByName(String matchName) {
        logInfo("Remove favourite ");

        ExpectedCondition<Boolean> deselected = d -> {
            try {
                EventRow row = findRowFlexible(matchName);
                if (row.isFavorite()) {
                    row.deselectFavoriteIfSelected();
                    return false;
                }
                return true;
            } catch (StaleElementReferenceException e) {
                return false;
            } catch (NoSuchElementException e) {
                return true;
            }
        };

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(StaleElementReferenceException.class)
                .until(deselected);
    }

    public void refreshWaitForTableBody() {
        driver.navigate().refresh();
        new WebDriverWait(driver, LONG_WAIT)
                .until(ExpectedConditions.visibilityOf(tableBody));
    }
}
