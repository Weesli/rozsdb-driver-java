package net.weesli.model;

import lombok.Getter;

@Getter
public class UriDetails {
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public UriDetails(String uri) {
        String[] parts = uri.split(":");
        if (parts.length > 6) {
            throw new IllegalArgumentException("Invalid URI format");
        }
        host = parts[1];
        port = Integer.parseInt(parts[2]);
        database = parts[3];
        user  = parts[4];
        password = (parts.length == 5 ? "" : parts[5]);
    }
}
