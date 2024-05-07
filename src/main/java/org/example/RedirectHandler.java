package org.example;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class RedirectHandler implements UrlHandler {
    private final UrlShortenerService urlShortenerService;

    public RedirectHandler(UrlShortenerService service) {
        this.urlShortenerService = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String shortKey = exchange.getRequestURI().getPath().substring(1); // remove leading '/'
        String originalUrl = urlShortenerService.getOriginalUrl(shortKey);

        if (originalUrl != null) {
            exchange.getResponseHeaders().set("Location", originalUrl);
            exchange.sendResponseHeaders(302, -1); // Redirect
        } else {
            String response = "URL not found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
