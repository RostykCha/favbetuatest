package tests.ui;

import automationframeworkexample.clients.ui.dto.UserDto;
import automationframeworkexample.clients.ui.pages.FavoritesPage;
import automationframeworkexample.clients.ui.pages.HomePage;
import automationframeworkexample.clients.ui.pages.LivePage;
import automationframeworkexample.clients.ui.pages.SettingsPage;
import automationframeworkexample.clients.ui.utils.retry.AutomationRetry;
import automationframeworkexample.clients.ui.utils.wrappers.TestCaseDocumentationId;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.*;
import tests.TestBase;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;
import static automationframeworkexample.clients.ui.utils.wrappers.TestDataRandomizer.getRandomProperEmailFormat;
import static automationframeworkexample.clients.ui.utils.wrappers.TestDataRandomizer.getRandomProperPass;

public class FavbetUiTests extends TestBase {

    //because we have 3 threads in TestNG config
    private static final int USERS_TO_CREATE = Integer.parseInt(System.getProperty("favbet.users", "2"));

    @BeforeClass(alwaysRun = true)
    public void seedUsers() {
        logInfo("[Suite setup] Creating " + USERS_TO_CREATE + " fresh test users via UI");

        IntStream.range(0, USERS_TO_CREATE).forEach(i -> {
            UserDto user = new UserDto(getRandomProperEmailFormat(), getRandomProperPass());
            try {
                ctx.getBean(HomePage.class)
                        .openHomePage()
                        .navigateToRegisterPage()
                        .registerUser(user)
                        .waitUntilUserIsLogged();
                TEST_USERS.add(user);
                logInfo("  – seeded user " + user.getEmail());
            } finally {
                dm.quit();
            }
        });
    }

    @BeforeMethod(alwaysRun = true)
    public void startDriver() {
        logInfo("[Test setup] Start Fresh Driver");
        dm.startFreshDriver();
    }

    @Test(dataProvider = "userLoginDataProvider", retryAnalyzer = AutomationRetry.class)
    @TestCaseDocumentationId(testCaseId = "001")
    public void verifyFavoritesLogic_T001(UserDto userDto) {
        logInfo("[T001] Start: verifyFavoritesLogic");

        HomePage home = ctx.getBean(HomePage.class);

        logInfo("[T001] Opening home page and logging in");
        LivePage livePage = home.openHomePage()
                .navigateToLoginPage()
                .loginUser(userDto)
                .navigateToLivePage();
        logInfo("[T001] Arrived at Live page");

        List<String> selectedFavorites = livePage.selectAllAvailableFavorites();
        logInfo("[T001] Selected favourites count: " + selectedFavorites.size());

        FavoritesPage favoritesPage = livePage.navigateToFavoritesPage();
        logInfo("[T001] Navigated to Favourites page");

        List<String> actualFavorites = favoritesPage.getAllFavorites();
        logInfo("[T001] Retrieved favourites from the table. Count: " + actualFavorites.size());

        logInfo("[T001] Verifying favourite lists match");
        Assertions.assertEquals(Set.copyOf(selectedFavorites), Set.copyOf(actualFavorites),
                "Mismatch between selected and actual favorites");
        logInfo("[T001] Favourite lists verified equal");

        String itemToRemove = selectedFavorites.get(0);
        logInfo("[T001] Removing favourite: " + itemToRemove);
        favoritesPage.removeFavoriteByName(itemToRemove);

        logInfo("[T001] Refreshing table after removal");
        favoritesPage.refreshWaitForTableBody();

        List<String> updatedFavorites = favoritesPage.getAllFavorites();
        logInfo("[T001] Updated favourites count: " + updatedFavorites.size());

        logInfo("[T001] Verifying removal and count decrement");
        Assertions.assertAll(
                () -> Assertions.assertFalse(updatedFavorites.contains(itemToRemove),
                        "Removed favorite still present"),
                () -> Assertions.assertEquals(selectedFavorites.size() - 1,
                        updatedFavorites.size(),
                        "Favorites count did not decrease by 1"));
        logInfo("[T001] Removal verified successfully");

        logInfo("[T001] End: verifyFavoritesLogic");
    }

    @Test(dataProvider = "userLoginDataProvider", retryAnalyzer = AutomationRetry.class)
    @TestCaseDocumentationId(testCaseId = "002")
    public void verifyYoutubeVide_T002(UserDto userDto) {
        logInfo("[T002] Start: verifyYoutubeVide");

        HomePage home = ctx.getBean(HomePage.class);
        logInfo("[T002] Opening home page and logging in");
        home.openHomePage()
                .navigateToLoginPage()
                .loginUser(userDto);

        logInfo("[T002] Opening YouTube channel from footer");
        home.openYoutubeFromFooter()
                .acceptCookies()
                .verifyYoutubeChannelName()
                .checkVideoIsPresent("FAVBET | Support Those Who Support Us: ENGLAND | 2022 FIFA World Cup");
        logInfo("[T002] Verified YouTube channel name and video presence");

        logInfo("[T002] End: verifyYoutubeVide");
    }

    @Test(dataProvider = "userLoginDataProvider", retryAnalyzer = AutomationRetry.class)
    @TestCaseDocumentationId(testCaseId = "003")
    public void verifyLanguageAndThemeChangeLogic_T003(UserDto userDto) {
        logInfo("[T003] Start: verifyLanguageAndThemeChangeLogic");

        HomePage home = ctx.getBean(HomePage.class);
        logInfo("[T003] Opening home page and logging in");
        SettingsPage settingsPage = home.openHomePage()
                .navigateToLoginPage()
                .loginUser(userDto)
                .navigateToSettingsPage();
        logInfo("[T003] Navigated to Settings page");

        settingsPage.cnahgeToUkrainianAndVerify();
        logInfo("[T003] Language changed to Ukrainian");
        settingsPage.changeToDarkThemeAndVerify();
        logInfo("[T003] Theme changed to Dark (UA)");
        settingsPage.changeToLightThemeAndVerify();
        logInfo("[T003] Theme changed back to Light (UA)");

        settingsPage.cnahgeToEnglishAndVerify();
        logInfo("[T003] Language changed to English");
        settingsPage.changeToDarkThemeAndVerify();
        logInfo("[T003] Theme changed to Dark (EN)");
        settingsPage.changeToLightThemeAndVerify();
        logInfo("[T003] Theme changed back to Light (EN)");

        logInfo("[T003] End: verifyLanguageAndThemeChangeLogic");
    }

    @AfterMethod(alwaysRun = true)
    public void killDriver() {
        dm.quit();
    }

    @AfterClass(alwaysRun = true)
    public void closeAllDrivers() {
        dm.quit();
    }
}
