package net.weesli.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DatabaseClient {
    private static final long AUTO_CLOSE_TIMEOUT = 60 *10 * 1000;
    private static final long INACTIVITY_CHECK_INTERVAL = 60 *1000;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(3))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .executor(executorService)
            .build();

    private final String baseUrl;
    private long lastRequestTime = System.currentTimeMillis();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public DatabaseClient(String baseUrl) {
        this.baseUrl = baseUrl;
        startAutoCloseTimer();
    }

    private Map<String, String> buildHeaders(Map<String, String> headers) {
        return headers;
    }

    private HttpRequest buildRequest(String endpoint, String jsonBody, Map<String, String> headers, String method) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint));

        headers.forEach(requestBuilder::header);

        if ("POST".equalsIgnoreCase(method)) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody));
        } else if ("GET".equalsIgnoreCase(method)) {
            requestBuilder.GET();
        }

        return requestBuilder.build();
    }

    public HttpResponse<String> post(String endpoint, String jsonBody, Map<String, String> headers) throws IOException, InterruptedException {
        lastRequestTime = System.currentTimeMillis();
        HttpRequest request = buildRequest(endpoint, jsonBody, headers, "POST");
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> get(String endpoint, Map<String, String> headers) throws IOException, InterruptedException {
        lastRequestTime = System.currentTimeMillis();
        HttpRequest request = buildRequest(endpoint, null, headers, "GET");
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public void close() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to close the client: " + e.getMessage(), e);
        }
    }

    private void startAutoCloseTimer() {
        scheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRequestTime >= AUTO_CLOSE_TIMEOUT) {
                close();
                scheduler.shutdown();
            }
        }, 0, INACTIVITY_CHECK_INTERVAL, TimeUnit.MILLISECONDS);
    }

    public boolean isClosed() {
        return executorService.isShutdown();
    }
}
