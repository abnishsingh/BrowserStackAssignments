package org.browserstack.utility;

import org.browserstack.conf.ConfigurationManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WebsiteValidator {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private ConfigurationManager config;

    public WebsiteValidator(WebDriver driver) {
        this.driver = driver;
        this.config = ConfigurationManager.getInstance();
        int timeoutSeconds = config.getIntProperty("browser.timeout.seconds");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public void validateSpanishLanguage() {
        try {
            // Verify using html lang attribute
            String htmlLang = driver.findElement(By.tagName("html")).getAttribute("lang");
            if (!(htmlLang.contentEquals("es-ES"))) {
                System.out.println("Warning: HTML lang attribute is not Spanish: " + htmlLang+"\n");

            }else {
                System.out.println("Success: HTML lang attribute is Spanish: " + htmlLang+"\n");
            }
        } catch (Exception e) {
            System.err.println("Error validating Spanish language: " + e.getMessage());
        }
    }
}