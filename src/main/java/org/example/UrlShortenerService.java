package org.example;

import java.util.HashMap;
import java.util.UUID;

public class UrlShortenerService {
    private final HashMap<String, String> urlMap = new HashMap<>();

    public synchronized String shortenUrl(String originalUrl) {
        String shortKey = UUID.randomUUID().toString().substring(0, 8);
        urlMap.put(shortKey, originalUrl);
        return shortKey;
    }

    public synchronized String getOriginalUrl(String shortKey) {
        return urlMap.get(shortKey);
    }
}
