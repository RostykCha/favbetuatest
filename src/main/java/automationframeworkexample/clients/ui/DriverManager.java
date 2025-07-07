package automationframeworkexample.clients.ui;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DriverManager {

    private final ThreadLocal<WebDriver> holder = new ThreadLocal<>();
    private final WebDriverFactory factory;

    @Autowired
    public DriverManager(WebDriverFactory factory) {
        this.factory = factory;
    }

    public WebDriver get() {
        if (holder.get() == null) holder.set(factory.newChromeDriver());
        return holder.get();
    }

    public void quit() {
        WebDriver d = holder.get();
        if (d != null) {
            d.quit();
            holder.remove();           // guarantees next get() builds fresh driver
        }
    }
}
