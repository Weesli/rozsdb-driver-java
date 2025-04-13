package net.weesli.http;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DatabaseClient {
    private static final MediaType JSON = MediaType.get("application/json");
    private final OkHttpClient client;
    private final String baseUrl;

    public DatabaseClient(String baseUrl) {
        this.baseUrl = baseUrl;

        this.client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                .build();
    }

    public Response post(String endpoint, String jsonBody, Map<String, String> headers) throws IOException {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Headers.Builder headersBuilder = new Headers.Builder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headersBuilder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()
                .headers(headersBuilder.build())
                .url(baseUrl + endpoint)
                .post(body)
                .build();
        return client.newCall(request).execute();
    }

    public Response get(String endpoint, Map<String, String> headers) throws IOException {
        Headers.Builder headersBuilder = new Headers.Builder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headersBuilder.add(entry.getKey(), entry.getValue());
        }
        Request request = new Request.Builder()
                .headers(headersBuilder.build())
                .url(baseUrl + endpoint)
                .get()
                .build();
        return client.newCall(request).execute();
    }

}
