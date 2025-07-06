package automationframeworkexample.clients.ui.utils.wrappers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RestAuthorizationWrapper {
    public final static String AUTH_HEADER_NAME = "authorization";
    @Value("${service.username}")
    private String customerName;

    @Value("${service.pass}")
    private String customerPass;

    public String getAuthorizationHeader() {
        String plainCredentials = customerName + ":" + customerPass;
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        return "Basic " + base64Credentials;
    }
}
