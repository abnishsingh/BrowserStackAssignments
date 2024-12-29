package org.browserstack.scraper;

import org.browserstack.conf.ConfigurationManager;
import org.browserstack.dto.Article;
import org.browserstack.utility.WebsiteValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class ArticleScraper {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private ConfigurationManager config;

    public ArticleScraper(WebDriver driver) {
        this.driver = driver;
        this.config = ConfigurationManager.getInstance();
        int timeoutSeconds = config.getIntProperty("browser.timeout.seconds");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    public List<Article> scrapeArticles() {
        return getFirstFiveArticles();
    }


    private List<Article> getFirstFiveArticles() {
        List<Article> articles = new ArrayList<>();
        List<WebElement> articleElements = driver.findElements(By.cssSelector("article")).subList(0, 5);
        for (WebElement articleElement : articleElements) {
            String title = articleElement.findElement(By.cssSelector("h2")).getText();
            String content = articleElement.findElement(By.cssSelector("p")).getText();
            String imageUrl = null;
            try {
                imageUrl = articleElement.findElement(By.cssSelector("img")).getAttribute("src");
            } catch (NoSuchElementException e) {
            }
            articles.add(new Article(title, content, imageUrl));
            System.out.println("Title: " + title);
            System.out.println("Content: " + content + "\n");
        }
        return articles;
    }
}