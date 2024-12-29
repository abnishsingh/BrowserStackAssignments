package org.browserstack.service;

import org.browserstack.conf.ConfigurationManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslatorService {

    private ConfigurationManager config;
    public TranslatorService() {
        this.config = ConfigurationManager.getInstance();
    }

    public static String translate(String langFrom, String langTo, String text) throws IOException {
        // Have used google script here - ref - https://stackoverflow.com/questions/8147284/how-to-use-google-translate-api-in-my-java-application
        String urlStr = "https://script.google.com/macros/s/AKfycbzjT_8E1Vziw2t56ZjNSAIqD8gOfS4CB5cDVCiKyoiStJu1iooJzyvOmG-_bzdylaQ-9Q/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}