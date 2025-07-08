package automationframeworkexample;

import java.time.Duration;

public final class AppConstants {

    private AppConstants() { /* prevent instantiation */ }

    public static final String FAVBET_BASE_URL = "https://www.favbet.ua/";
    public static final Duration SHORT_WAIT = Duration.ofSeconds(10);
    public static final Duration LONG_WAIT = Duration.ofSeconds(20);


}
