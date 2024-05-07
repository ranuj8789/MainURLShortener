package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreadedHttpServerTest {

    private static Thread serverThread;
    private static final HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    public static void setUp() {
        // Start server in a separate thread
        serverThread = new Thread(() -> {
            try {
                ThreadedHttpServer.main(new String[]{});
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        try {
            // Allow some time for the server to start
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void testShortenUrlHandler() throws Exception {
        String testUrl = "http://localhost:8080/shorten";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(testUrl))
                .POST(HttpRequest.BodyPublishers.ofString("https://www.example.com"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        System.out.println("Response from /shorten: " + response.body());
    }

    @Test
    public void testRedirectHandler() throws Exception {
        String testUrl = "http://localhost:8080/nonexistent";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(testUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Should return 404 for non-existent shortened URL");
    }

    @AfterAll
    public static void tearDown() {
        serverThread.interrupt();
    }
}
