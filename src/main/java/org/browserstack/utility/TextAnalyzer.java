package org.browserstack.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextAnalyzer {
    public Map<String, Integer> analyzeWordFrequency(List<String> texts) {
        Map<String, Integer> wordCount = new HashMap<>();
        
        for (String text : texts) {
            String[] words = text.toLowerCase().split("\\s+");
            for (String word : words) {
                word = word.replaceAll("[^a-zA-Z]", "");
                if (!word.isEmpty()) {
                    wordCount.merge(word, 1, Integer::sum);
                }
            }
        }
        return wordCount;
    }

    public void printRepeatedWords(Map<String, Integer> wordFrequency) {
        System.out.println("\nRepeated words (appearing more than twice):");
        wordFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() > 2)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue() + " times"));
    }
}
