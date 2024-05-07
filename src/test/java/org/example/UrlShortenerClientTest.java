package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UrlShortenerClientTest {

    @Mock
    private HttpClient mockHttpClient;

    private UrlShortenerClient client;

    @BeforeEach
    void setUp() {
        client = new UrlShortenerClient("http://localhost:8080", mockHttpClient);
    }

    @Test
    void testShortenUrl() throws Exception {
        String originalUrl = "https://www.example.com";
        String shortenedResponse = "http://localhost:8080/abc123";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(shortenedResponse);

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        String result = client.shortenUrl(originalUrl);
        assertEquals(shortenedResponse, result);
    }

    @Test
    void testFollowRedirect() throws Exception {
        String shortenedUrl = "http://localhost:8080/abc123";
        HttpResponse<Void> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(302);
        when(mockResponse.headers()).thenReturn(HttpHeaders.of(Map.of("Location", List.of("https://www.example.com")), (x, y) -> true));

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        assertDoesNotThrow(() -> client.followRedirect(shortenedUrl));
    }
}
