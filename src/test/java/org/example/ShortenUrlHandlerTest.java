package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortenUrlHandlerTest {

    @Mock
    private HttpExchange exchange;
    @Mock
    private UrlShortenerService urlShortenerService;

    private ShortenUrlHandler handler;
    private ByteArrayOutputStream responseStream;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        handler = new ShortenUrlHandler(urlShortenerService);
        responseStream = new ByteArrayOutputStream();

        when(exchange.getResponseBody()).thenReturn(responseStream);
        when(exchange.getRequestHeaders()).thenReturn(new Headers());
    }

    @Test
    public void testHandlePostRequest() throws IOException {
        String originalUrl = "https://example.com";
        String shortKey = "abc123";
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(originalUrl.getBytes()));
        when(urlShortenerService.shortenUrl(originalUrl)).thenReturn(shortKey);

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, ("http://localhost:8080/" + shortKey).getBytes().length);
        assertEquals("http://localhost:8080/" + shortKey, responseStream.toString());
        verify(urlShortenerService).shortenUrl(originalUrl);
    }

    @Test
    public void testHandleNonPostRequest() throws IOException {
        when(exchange.getRequestMethod()).thenReturn("GET");

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(405, -1);
        assertEquals("", responseStream.toString(), "Response body should be empty for non-POST requests");
    }
}
