package net.weesli.model;

import net.weesli.enums.CollectionActionType;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static net.weesli.util.RequestUtil.*;
public record CollectionImpl(Connection connection, String name) implements Collection {

    @Override
    public DatabaseObject insertOrUpdate(String object) {
        AtomicReference<DatabaseObject> status = new AtomicReference<>();
        send(connection,CollectionActionType.INSERTORUPDATE, object, (response -> {
            status.set(new DatabaseObject(response.message(), this));
        }));
        return status.get();
    }

    @Override
    public DatabaseObject delete(String object) {
        AtomicReference<DatabaseObject> status = new AtomicReference<>();
        send(connection,CollectionActionType.DELETE, object, (response -> {
            status.set(new DatabaseObject(response.message(), this));
        }));
        return status.get();
    }

    @Override
    public List<DatabaseObject> findAll() {
        AtomicReference<List<DatabaseObject>> result = new AtomicReference<>(new ArrayList<>());
        send(connection,CollectionActionType.FINDALL, null, (response -> {
            List<String> list = new ArrayList<>(Collections.singleton(response.message()));
            result.set(list.stream().map(object -> new DatabaseObject(object, this)).collect(Collectors.toList()));
        }));
        return new ArrayList<>(result.get());
    }

    @Override
    public DatabaseObject findById(String id) {
        AtomicReference<DatabaseObject> object = new AtomicReference<>();
        send(connection,CollectionActionType.FINDBYID, id, (response -> {
            object.set(new DatabaseObject(response.message(), this));
        }));
        return object.get();
    }

    @Override
    public List<DatabaseObject> find(String where, String value) {
        AtomicReference<List<DatabaseObject>> result = new AtomicReference<>(new ArrayList<>());
        send(connection,CollectionActionType.FIND, Map.of(where, value), (response -> {
            List<String> list = new ArrayList<>(Collections.singleton(response.message()));
            result.set(list.stream().map(object -> new DatabaseObject(object, this)).collect(Collectors.toList()));
        }));
        return new ArrayList<>(result.get());
    }
}
