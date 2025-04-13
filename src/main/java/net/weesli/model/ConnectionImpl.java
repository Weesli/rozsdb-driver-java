package net.weesli.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;

public record ConnectionImpl(UriDetails uriDetails, String database, ObjectMapper mapper) implements Connection {
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

}
