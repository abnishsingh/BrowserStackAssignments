package org.browserstack.utility;

import org.apache.commons.io.FileUtils;
import org.browserstack.conf.ConfigurationManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;


public class ImageDownloader {

    private ConfigurationManager config;

    public ImageDownloader() {
        this.config = ConfigurationManager.getInstance();
    }

    public void downloadImage(String imageUrl, String articleTitle) {
        try {
            String fileName = articleTitle.replaceAll("[^a-zA-Z0-9]", "_") + ".jpg";
            URL url = new URL(imageUrl);
            File outputFile = new File("images/" + fileName);
            FileUtils.copyURLToFile(url, outputFile);
            System.out.println("Downloaded image: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}