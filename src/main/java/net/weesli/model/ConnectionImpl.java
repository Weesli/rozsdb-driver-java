package net.weesli.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weesli.client.DatabaseClient;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;

public record ConnectionImpl(UriDetails uriDetails, String database, ObjectMapper mapper, DatabaseClient client) implements Connection {

    public ConnectionImpl(UriDetails uriDetails, String database, ObjectMapper mapper, DatabaseClient client) {
        this.uriDetails = uriDetails;
        this.database = database;
        this.mapper = mapper;
        this.client = client;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public Collection getCollection(String name) {
        return
                new CollectionImpl(
                        this,
                        name,
                        this.mapper
                );
    }

    @Override
    public UriDetails getUriDetails() {
        return uriDetails;
    }

    @Override
    public DatabaseClient getDatabaseClient() {
        return client;
    }
}
