package org.example;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class ShortenUrlHandler implements UrlHandler {
    private final UrlShortenerService urlShortenerService;

    public ShortenUrlHandler(UrlShortenerService service) {
        this.urlShortenerService = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String originalUrl = new String(exchange.getRequestBody().readAllBytes());
            String shortKey = urlShortenerService.shortenUrl(originalUrl);
            String response = "http://localhost:8080/" + shortKey;
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }
}
