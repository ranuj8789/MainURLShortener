package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UrlShortenerServiceTest {

    @Test
    public void testShortenUrl() {
        UrlShortenerService service = new UrlShortenerService();
        String originalUrl = "https://www.example.com";
        String shortKey = service.shortenUrl(originalUrl);

        assertNotNull(shortKey, "Short key should not be null");
        assertTrue(shortKey.length() == 8, "Short key should be 8 characters long");
        assertEquals(originalUrl, service.getOriginalUrl(shortKey), "Retrieved URL should match the original");
    }

    @Test
    public void testGetOriginalUrl() {
        UrlShortenerService service = new UrlShortenerService();
        String originalUrl = "https://www.example.com";
        String shortKey = service.shortenUrl(originalUrl);

        // Test getting the correct URL back with the short key
        String retrievedUrl = service.getOriginalUrl(shortKey);
        assertEquals(originalUrl, retrievedUrl, "The retrieved URL should match the original URL");

        // Test getting null for a non-existent key
        assertNull(service.getOriginalUrl("nonexistentkey"), "Should return null for non-existent keys");
    }
}
