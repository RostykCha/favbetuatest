package tests.ui;

import automationframeworkexample.clients.ui.dto.UserDto;
import automationframeworkexample.clients.ui.pages.HomePage;
import automationframeworkexample.clients.ui.pages.LivePage;
import automationframeworkexample.clients.ui.utils.retry.AutomationRetry;
import automationframeworkexample.clients.ui.utils.wrappers.TestCaseDocumentationId;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import tests.TestBase;

import java.util.List;

public class FavbetTests extends TestBase {

    @BeforeSuite
    void doBeforePreparations() {
        HomePage homePage = basePage.openHomePage();
        List<UserDto> newRegisteredUsers = homePage.registerUser(10);
    }

    @Test(retryAnalyzer = AutomationRetry.class, dataProvider = "userLoginDataProvider")
    @TestCaseDocumentationId(testCaseId = "473")
    public void verifyLoginUI_T473(UserDto userDto) {
        HomePage homePage = basePage.openHomePage();

        LivePage livePage = homePage.loginUser(userDto).navigateToLivePage();
        List<String> selectedFavorites = livePage.selectRandomFavorites(3);

        FavoritesPage favoritesPage = livePage.navigateToFavorites();
        List<String> actualFavorites = favoritesPage.getAllActualFavorites();
        Assertions.assertEquals(actualFavorites, selectedFavorites);

        String favoriteItemToRemove = selectedFavorites.get(0);
        favoritesPage.removeFavorite(favoriteItemToRemove);

        List<String> updatedActualFavorites = favoritesPage.getAllActualFavorites();
        Assertions.assertFalse(updatedActualFavorites.contains(favoriteItemToRemove));
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
    //3. change language to english\ukrainian.
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

    @AfterSuite
    void closeAllDrivers() {
        basePage.closeDrivers();
    }
}
