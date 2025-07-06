package automationframeworkexample.clients.ui.utils.wrappers;

import io.qameta.allure.Step;

public class LoggerWrapper {

    @Step("{message}")
    public static void logInfo(String message) {
        System.out.println(message);
    }

    @Step("ERROR: {message}")
    public static void logError(String message) {
        System.out.println("ERROR: " + message);
    }

}
