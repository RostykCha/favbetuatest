package tests.ui;

import automationframeworkexample.clients.ui.dto.UserDto;
import automationframeworkexample.clients.ui.pages.FavoritesPage;
import automationframeworkexample.clients.ui.pages.HomePage;
import automationframeworkexample.clients.ui.pages.LivePage;
import automationframeworkexample.clients.ui.pages.RegisterPage;
import automationframeworkexample.clients.ui.utils.retry.AutomationRetry;
import automationframeworkexample.clients.ui.utils.wrappers.TestCaseDocumentationId;
import org.junit.AfterClass;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.TestBase;

import java.util.List;
import java.util.Set;

import static automationframeworkexample.clients.ui.utils.wrappers.TestDataRandomizer.getRandomProperEmailFormat;
import static automationframeworkexample.clients.ui.utils.wrappers.TestDataRandomizer.getRandomProperPass;

public class FavbetUiTests extends TestBase {
    UserDto userDto;

    @BeforeClass
    public void doBeforePreparations() {
        userDto = new UserDto(getRandomProperEmailFormat(), getRandomProperPass());

        RegisterPage registerPage = ctx.getBean(HomePage.class)
                .openHomePage()
                .navigateToRegisterPage()
                .registerUser(userDto);
        registerPage.closeNotificationIfPresent();
        registerPage.waitUntilRegistered();

        dm.quit();
    }

    @Test(retryAnalyzer = AutomationRetry.class)
    @TestCaseDocumentationId(testCaseId = "473")
    public void verifyFavoritesLogic_T473() {
        HomePage home = ctx.getBean(HomePage.class);
        LivePage livePage = home.openHomePage()
                .navigateToLoginPage()
                .loginUser(userDto)
                .navigateToLivePage();

        List<String> selectedFavorites = livePage.selectAllAvailableFavorites();

        FavoritesPage favoritesPage = livePage.navigateToFavoritesPage();
        List<String> actualFavorites = favoritesPage.getAllFavorites();

        Assertions.assertEquals(Set.copyOf(selectedFavorites),
                Set.copyOf(actualFavorites),
                "Mismatch between selected and actual favorites");

        String itemToRemove = selectedFavorites.get(0);
        favoritesPage.removeFavoriteByName(itemToRemove);

        List<String> updatedFavorites = favoritesPage.getAllFavorites();

        Assertions.assertAll(
                () -> Assertions.assertFalse(updatedFavorites.contains(itemToRemove),
                        "Removed favorite still present"),
                () -> Assertions.assertEquals(selectedFavorites.size() - 1,
                        updatedFavorites.size(),
                        "Favorites count did not decrease by 1"));
    }

    //Create UI test solution that contains the following test cases:
    //test1
    //1. login to favbet.ua
    //2. Navigate to Live
    //3. Add several items to favorites
    //4. Navigate to favorites.
    //5. Check selected items are present.
    //6. Remove any item
    //7. Refresh page and check item is removed.
    //

    //test2
    //1. login to favbet.ua
    //2. Navigate to Youtube social network
    //3. Check accurate channel is opened
    //4. Check video 'FAVBET | Support Those Who Support Us: ENGLAND | 2022 FIFA World Cup' is present.
    //
    //test3
    //1. login to favbet.ua
    //2. open settings page
    //3. change language to english ukrainian.
    //4. verify language is updated
    //5. update theme to dark\light
    //6. check theme is applied
    //
    //Create API test solution that contains the following test cases:
    //test1
    //1. Login
    //2. get the list of bonuses
    //3. check accurate list of bonuses is returned
    //
    //test2
    //1. login
    //2. add some instant games to favorites
    //3. get the list of favorites instant games
    //4. check favorites contain accurate list of games (previously added)

    @AfterClass
    public void closeAllDrivers() {
    }
}
