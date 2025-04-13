package net.weesli.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weesli.model.DatabaseObject;

import java.util.List;

public interface Collection {
    DatabaseObject insertOrUpdate(String object, String id);
    boolean delete(String object);
    List<DatabaseObject> findAll();
    DatabaseObject findById(String id);
    List<DatabaseObject> find(String where, String value);

    String name();

    Collection connect();
    ObjectMapper mapper();
}
