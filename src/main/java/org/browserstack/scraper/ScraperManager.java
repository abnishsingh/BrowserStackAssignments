package org.browserstack.scraper;

import org.bouncycastle.util.encoders.Translator;
import org.browserstack.conf.ConfigurationManager;
import org.browserstack.dto.Article;
//import org.browserstack.service.TranslationService;
import org.browserstack.service.TranslatorService;
import org.browserstack.utility.ImageDownloader;
import org.browserstack.utility.TextAnalyzer;
import org.browserstack.utility.WebsiteValidator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.browserstack.service.TranslatorService.translate;

public class ScraperManager {
    private final WebDriver driver;
    private final ArticleScraper articleScraper;
    private final TextAnalyzer textAnalyzer;
    private final ImageDownloader imageDownloader;
    private final WebsiteValidator websiteValidator;

    private WebDriverWait wait;
    private ConfigurationManager config;

    public ScraperManager() {
        this.driver = new ChromeDriver();
        this.config = ConfigurationManager.getInstance();
        this.websiteValidator = new WebsiteValidator(driver);
        this.articleScraper = new ArticleScraper(driver);
        this.textAnalyzer = new TextAnalyzer();
        this.imageDownloader = new ImageDownloader();
    }

    public static void acceptCookies(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement acceptButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='didomi-notice-agree-button']"))
            );
            acceptButton.click();
        } catch (Exception e) {
            System.err.println("Failed to accept cookies: " + e.getMessage());
        }
    }

    private void navigateToOpinionSection() {
        String opinionUrl = config.getProperty("website.opinion.url");
        driver.get(opinionUrl);
        int timeoutSeconds = config.getIntProperty("browser.timeout.seconds");
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    public void execute() {
        try {
            navigateToOpinionSection(); //go to Opinion page
            acceptCookies(driver); //accept cookie
            websiteValidator.validateSpanishLanguage(); //Verify Spanish Language
            List<Article> articles = articleScraper.scrapeArticles(); //scrape first 5 Articles
            List<String> translatedHeaders = translateHeaders(articles); //translate the articles
            analyzeHeaders(translatedHeaders); // analyze translated headers
            downloadImages(articles); //image articles in local machine
        } finally {
            driver.quit();
        }
    }

    public List<String> translateHeaders(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            return Collections.emptyList();
        }
        return articles.stream()
                .map(article -> {
                    try {
                        System.out.println(translate("es", "en", article.getTitle()));
                        return translate("es", "en", article.getTitle());
                    } catch (IOException e) {
                        System.err.println("Error translating title: " + article.getTitle() + " - " + e.getMessage());
                        return "Translation Error";
                    }
                })
                .collect(Collectors.toList());
    }

    private void analyzeHeaders(List<String> headers) {
        Map<String, Integer> wordFrequency = textAnalyzer.analyzeWordFrequency(headers);
        textAnalyzer.printRepeatedWords(wordFrequency);
    }

    private void downloadImages(List<Article> articles) {
        articles.forEach(article -> {
            if (article.getImageUrl() != null) {
                imageDownloader.downloadImage(article.getImageUrl(), article.getTitle());
            }
        });
    }
}