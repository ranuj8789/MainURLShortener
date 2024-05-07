package org.example;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ThreadedHttpServer {
    public static void main(String[] args) throws IOException, IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.setExecutor(Executors.newFixedThreadPool(50));
        UrlShortenerService urlShortenerService = new UrlShortenerService();
        server.createContext("/shorten", new ShortenUrlHandler(urlShortenerService));
        server.createContext("/", new RedirectHandler(urlShortenerService));
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");
    }
}
