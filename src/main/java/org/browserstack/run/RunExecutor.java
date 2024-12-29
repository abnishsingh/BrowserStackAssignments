package org.browserstack.run;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.browserstack.scraper.ScraperManager;
import org.browserstack.utility.WebsiteValidator;
import org.openqa.selenium.WebDriver;

public class RunExecutor {

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        ScraperManager scraperManager = new ScraperManager();
        scraperManager.execute();
    }
}