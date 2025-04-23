package net.weesli.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import net.weesli.enums.CollectionActionType;
import net.weesli.exception.ConnectionException;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record CollectionImpl(Connection connection, String name, ObjectMapper mapper) implements Collection {

    private static boolean connected = true;

    public CollectionImpl(Connection connection, String name, ObjectMapper mapper) {
        this.mapper = mapper;
        this.connection = connection;
        this.name = name;
    }

    public Collection connect() {
        try {
            byte[] response = connection.getDatabaseClient().send(CollectionActionType.CONNECTION, this,"");
            JsonNode json = mapper.readTree(response);
            String status = json.get("status").asText();
            if (!status.equals("success")) {
                throw new ConnectionException(json.get("message").asText());
            }
        } catch (Exception e) {
            throw new RuntimeException("Connection failed: " + e.getMessage(), e);
        }
        return this;
    }
    @Override
    public DatabaseObject insertOrUpdate(String object, String id) {
        ObjectNode node = mapper.createObjectNode();
        if (id != null) {
            node.put("id", id);
        }
        node.put("data", object);
        try {
            byte[] response = connection.getDatabaseClient().send(CollectionActionType.INSERTORUPDATE, this, node.toString());
            JsonNode json = mapper.readTree(new String(response));
            String status = json.get("status").asText();
            if (!status.equals("success")) {
                throw new RuntimeException("Insert or update failed: " + json.get("message").asText());
            }
            return new DatabaseObject(json.get("message").asText(), this);
        } catch (Exception e) {
            throw new RuntimeException("Insert or update failed: " + e.getMessage(), e);
        }
    }

    @SneakyThrows
    @Override
    public boolean delete(String object) {
        ObjectNode node = mapper.createObjectNode();
        node.put("id", object);
        try {
            byte[] response = connection.getDatabaseClient().send(CollectionActionType.DELETE,this, node.toString());
            JsonNode json = mapper.readTree(response);
            String status = json.get("status").asText();
            if (!status.equals("success")) {
                throw new RuntimeException("Delete failed: " + json.get("message").asText());
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: " + e.getMessage(), e);
        }
    }

    @SneakyThrows
    @Override
    public List<DatabaseObject> findAll() {
        try {
            byte[] response = connection.getDatabaseClient().send(CollectionActionType.FINDALL, this,"");
            JsonNode jsonNode = mapper.readTree(response);
            String status = jsonNode.get("status").asText();
            if (!status.equals("success")) {
                throw new RuntimeException("Find all failed: " + jsonNode.get("message").asText());
            }
            List<String> list = List.of(jsonNode.get("message").asText().replaceAll("[\\[\\]\"]", "").split(","));
            return list.stream().map(src -> new DatabaseObject(src.trim(), this)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Find all failed: " + e.getMessage(), e);
        }
    }

    @Override
    public DatabaseObject findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        ObjectNode node = mapper.createObjectNode();
        node.put("id", id);
        try {
            byte[] response = connection.getDatabaseClient().send(CollectionActionType.FINDBYID,this, node.toString());
            JsonNode jsonNode = mapper.readTree(response);
            String status = jsonNode.get("status").asText();
            if (!status.equals("success")) {
                throw new RuntimeException("Find by ID failed: " + jsonNode.get("message").asText());
            }
            return new DatabaseObject(jsonNode.get("message").asText(), this);
        } catch (Exception e) {
            throw new RuntimeException("Find by ID failed: " + e.getMessage(), e);
        }
    }

    @SneakyThrows
    @Override
    public List<DatabaseObject> find(String where, String value) {
        ObjectNode detailsNode = mapper.createObjectNode();
        detailsNode.put("field", where);
        detailsNode.put("value", value);
        try {

            byte[] response = connection.getDatabaseClient().send(CollectionActionType.FIND, this, detailsNode.toString());
            JsonNode jsonNode = mapper.readTree(response);
            String status = jsonNode.get("status").asText();
            if (!status.equals("success")) {
                throw new RuntimeException("Find failed: " + jsonNode.get("message").asText());
            }
            String data = jsonNode.get("message").asText();
            List<String> list = Stream.of(data.replaceAll("[\\[\\]\"]", "").split(","))
                    .map(String::trim)
                    .toList();
            return list.stream().map(object -> new DatabaseObject(object, this)).collect(Collectors.toList());
        }catch (Exception e) {
            throw new RuntimeException("Find failed: " + e.getMessage(), e);
        }
    }

    public static boolean isConnected() {
        return connected;
    }

    @Override
    public void close() {
        try {
            connection.getDatabaseClient().close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close the database connection.getDatabaseClient(): " + e.getMessage(), e);
        }
    }
}
