package automationframeworkexample.clients.api;

import automationframeworkexample.clients.api.dto.BonusCountResponse;
import automationframeworkexample.clients.api.dto.BonusCounts;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

import static automationframeworkexample.clients.ui.utils.wrappers.LoggerWrapper.logInfo;
import static io.restassured.RestAssured.given;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FavbetApiClient {

    private static final String ORIGIN = "https://www.favbet.ua";
    private static final String BONUS_COUNT_ENDPOINT = "/accounting/api/crm_roxy/getanybonuscount";

    static {
        RestAssured.baseURI = ORIGIN;
    }

    private final CookieFilter cookieJar = new CookieFilter();
    private final ObjectMapper mapper = new ObjectMapper();
    private RequestSpecification baseSpec;
    private WebDriver browser;
    private String userAgent = "Mozilla/5.0";

    public void bootstrapFromSelenium(WebDriver driver) {
        this.browser = driver;

        Set<org.openqa.selenium.Cookie> selCookies = driver.manage().getCookies();
        selCookies.forEach(sc -> {
            BasicClientCookie bc = new BasicClientCookie(sc.getName(), sc.getValue());
            bc.setDomain(sc.getDomain());
            bc.setPath(sc.getPath());
            bc.setSecure(sc.isSecure());
            bc.setExpiryDate(sc.getExpiry());
            cookieJar.getCookieStore().addCookie(bc);
        });
        boolean hasCf = cookieJar.getCookieStore().getCookies()
                .stream().anyMatch(c -> "cf_clearance".equals(c.getName()));
        logInfo("[API] Copied " + selCookies.size() + " cookies (cf_clearance present: " + hasCf + ")");

        Object ua = ((JavascriptExecutor) driver).executeScript("return navigator.userAgent");
        if (ua != null) userAgent = ua.toString();
        logInfo("[API] UA: " + userAgent);

        baseSpec = new RequestSpecBuilder()
                .addFilter(cookieJar)
                .setRelaxedHTTPSValidation()
                .setContentType(ContentType.JSON)
                .addHeader("User-Agent", userAgent)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Origin", ORIGIN)
                .addHeader("Referer", ORIGIN + "/")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();
    }

    public BonusCounts getListOfBonuses() {
        logInfo("[API] Fetching bonus counters");

        BonusCounts viaApi = tryRestAssured();
        if (viaApi != null) {
            return viaApi;
        }

        logInfo("[API] CF blocked direct call → falling back to browser fetch");
        return fetchThroughBrowser();
    }

    public void assertBonusCountsAreAccurate(BonusCounts counts) {
        int expectedAll = counts.getRiskFree() + counts.getRealMoney() + counts.getFreeSpin();
        Assertions.assertEquals(
                expectedAll, counts.getAll(),
                "'All' counter ≠ sum of RiskFree, RealMoney & FreeSpin"
        );
        logInfo("[API] Bonus counters are accurate");
    }

    private BonusCounts tryRestAssured() {
        // XSRF token (if present) must be echoed back
        String xsrf = cookieJar.getCookieStore().getCookies().stream()
                .filter(c -> "XSRF-TOKEN".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse("");

        Response r = given().spec(baseSpec)
                .header(new Header("X-XSRF-TOKEN", xsrf))
                .get(BONUS_COUNT_ENDPOINT);

        if (r.statusCode() == 200) {
            BonusCountResponse parsed = r.as(BonusCountResponse.class);
            BonusCounts counts = parsed.getResponse().getResponse();
            logInfo("[API] Parsed bonus counters (direct): " + counts);
            return counts;
        }
        logInfo("[API] Direct call returned " + r.statusCode());
        return null;
    }

    private BonusCounts fetchThroughBrowser() {

        JavascriptExecutor js = (JavascriptExecutor) browser;

        String script =
                "const done = arguments[arguments.length - 1];" +
                        "fetch('" + BONUS_COUNT_ENDPOINT + "', {credentials: 'include'})" +
                        "  .then(r => r.text())" +                 // ← grab text so we can always log it
                        "  .then(t => done(t))" +
                        "  .catch(e => done('ERROR:' + e.message));";

        Object resultObj = js.executeAsyncScript(script);
        if (resultObj == null) {
            throw new IllegalStateException("[API] Browser fetch returned null");
        }

        String result = resultObj.toString();
        logInfo("[API] Raw payload from browser: " +
                (result.length() > 250 ? result.substring(0, 250) + " …" : result));

        if (result.startsWith("ERROR:")) {
            throw new IllegalStateException("[API] Browser fetch failed: " + result);
        }

        try {
            BonusCountResponse parsed = mapper.readValue(result, BonusCountResponse.class);
            BonusCounts counts = parsed.getResponse().getResponse();
            logInfo("[API] Parsed bonus counters (browser): " + counts);
            return counts;
        } catch (Exception ex) {
            logInfo("[API] Deserialization error: " + ex.getMessage());
            throw new RuntimeException("Failed to deserialize JSON from browser", ex);
        }
    }
}
