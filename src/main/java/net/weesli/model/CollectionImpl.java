package net.weesli.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import net.weesli.enums.CollectionActionType;
import net.weesli.http.DatabaseClient;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.weesli.util.RequestUtil.*;

public record CollectionImpl(Connection connection, String name, ObjectMapper mapper) implements Collection {

    private static boolean connected = true;
    private static DatabaseClient client;

    public CollectionImpl(Connection connection, String name, ObjectMapper mapper) {
        this.mapper = mapper;
        this.connection = connection;
        this.name = name;
        client = new DatabaseClient(String.format("http://%s:%s/databases/%s/collections/%s/",
                connection.getUriDetails().getHost(),
                connection.getUriDetails().getPort(),
                connection.getDatabase(),
                name
        ));
    }

    public Collection connect() {
        if (client.isClosed()){ // if the client is closed, create a new one
            client = new DatabaseClient(String.format("http://%s:%s/databases/%s/collections/%s/",
                    connection.getUriDetails().getHost(),
                    connection.getUriDetails().getPort(),
                    connection.getDatabase(),
                    name
            ));
        }
        try {
            HttpResponse<String> response = send(client, this, connection, CollectionActionType.CONNECTION, new RequestDetails(Map.of(), ""));
            if (response.statusCode() != 200) {
                connected = false;
                throw new RuntimeException("Connection failed: " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Connection failed: " + e.getMessage(), e);
        }
        return this;
    }

    @SneakyThrows
    @Override
    public DatabaseObject insertOrUpdate(String object, String id) {
        ObjectNode node = mapper.createObjectNode();
        if (id != null) {
            node.put("id", id);
        }
        node.put("data", object);

        RequestDetails details = new RequestDetails(Map.of(), node.toString());

        try {
            HttpResponse<String> response = send(client, this, connection, CollectionActionType.INSERTORUPDATE, details);
            if (response.statusCode() == 200) {
                JsonNode json = mapper.readTree(response.body());
                return new DatabaseObject(json.get("message").asText(), this);
            } else {
                throw new RuntimeException("HTTP error: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            throw new RuntimeException("Insert or update failed: " + e.getMessage(), e);
        }
    }

    @SneakyThrows
    @Override
    public boolean delete(String object) {
        ObjectNode node = mapper.createObjectNode();
        node.put("id", object);
        RequestDetails details = new RequestDetails(Map.of(), node.toString());
        try {
            HttpResponse<String> response = send(client, this, connection, CollectionActionType.DELETE, details);
            if (response.statusCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Delete failed: " + e.getMessage(), e);
        }
        return false;
    }

    @SneakyThrows
    @Override
    public List<DatabaseObject> findAll() {
        RequestDetails details = new RequestDetails(Map.of(), "");
        try {
            HttpResponse<String> response = send(client, this, connection, CollectionActionType.FINDALL, details);
            if (response.statusCode() != 200) {
                throw new RuntimeException("Find all failed: " + response.body());
            }
            JsonNode jsonNode = mapper.readTree(response.body());
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
        RequestDetails details = new RequestDetails(Map.of(), node.toString());
        try {
            HttpResponse<String> response = send(client, this, connection, CollectionActionType.FINDBYID, details);
            if (response.statusCode() != 200) {
                throw new RuntimeException("Find by ID failed: " + response.body());
            }
            JsonNode jsonNode = mapper.readTree(response.body());
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
        RequestDetails details = new RequestDetails(Map.of(), detailsNode.toString());
        try {
            HttpResponse<String> response = send(client, this, connection, CollectionActionType.FIND, details);
            if (response.statusCode() != 200) {
                String message = response.body();
                if (message.contains("Data not found")) {
                    return List.of();
                } else {
                    throw new RuntimeException("Find failed: " + message);
                }
            }
            String data = response.body();
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
            client.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to close the database client: " + e.getMessage(), e);
        }
    }
}
