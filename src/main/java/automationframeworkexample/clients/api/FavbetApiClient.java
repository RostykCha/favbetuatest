package automationframeworkexample.clients.api;

import automationframeworkexample.clients.ui.dto.UserDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;
import static io.restassured.RestAssured.given;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FavbetApiClient {

    static {
        RestAssured.baseURI = "https://www.favbet.ua";
    }

    @Autowired
    public FavbetApiClient() {
    }

    public Response createUser(UserDto user) {
        logInfo(String.format("Create user via API \"%s\"", user.getEmail()));

        Response response = given()
                .contentType("application/json; charset=UTF-8")
                .body(user)
                .post("/accounting/api/saveuser")
                .andReturn();

        int code = response.getStatusCode();
        if (code == 200 || code == 201) {
            logInfo(String.format("User \"%s\" created (HTTP %d)", user.getEmail(), code));
            try { Thread.sleep(1_000); } catch (InterruptedException ignored) { }
        } else {
            logInfo(String.format("User creation failed for \"%s\" (HTTP %d)", user.getEmail(), code));
        }
        return response;
    }

}
