package net.weesli.model;

import com.google.gson.Gson;
import lombok.Getter;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;
public record ConnectionImpl(UriDetails uriDetails, String database, Gson gson) implements Connection {
    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public Collection getCollection(String name) {
        return
                new CollectionImpl(
                        this,
                        name
                );
    }

    @Override
    public UriDetails getUriDetails() {
        return uriDetails;
    }

}
