package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class UrlShortenerClient {

    private final HttpClient client;
    private final String serverUrl;

    // Constructor that allows HttpClient injection for greater flexibility and testability
    public UrlShortenerClient(String serverUrl, HttpClient client) {
        this.serverUrl = serverUrl;
        this.client = client; // Use the provided HttpClient
    }

    public String shortenUrl(String originalUrl) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/shorten"))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(originalUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new RuntimeException("Failed to shorten URL: " + response.statusCode() + " - " + response.body());
        }
    }

    public void followRedirect(String shortenedUrl) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(shortenedUrl))
                .GET()
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        System.out.println("Following redirect for shortened URL...");
        System.out.println("Expected URL was: " + request.uri().getHost() + request.uri().getPath());
        System.out.println("Redirect status code: " + response.statusCode());
        if (response.statusCode() == 302) {
            System.out.println("Redirected URL: " + response.headers().firstValue("Location").orElse("No location header found"));
        }
    }
}
