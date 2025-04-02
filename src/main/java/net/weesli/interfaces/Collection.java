package net.weesli.interfaces;

import net.weesli.model.DatabaseObject;
import net.weesli.model.ResponseStatus;

import java.util.List;

public interface Collection {
    DatabaseObject insertOrUpdate(String object);
    DatabaseObject  delete(String object);
    List<DatabaseObject> findAll();
    DatabaseObject findById(String id);
    List<DatabaseObject> find(String where, String value);

    String name();
}
