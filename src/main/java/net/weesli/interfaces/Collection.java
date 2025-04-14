package net.weesli.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.weesli.model.DatabaseObject;

import java.util.List;

/**
 * An interface for managing database collections in an object-oriented manner.
 * Collection provides methods to perform CRUD operations (Create, Read, Update, Delete)
 * on database objects within a specific collection.
 *
 * Use <code>CollectionSelector</code> or <code>ConnectionSelector</code> to obtain a Collection instance.
 *
 * <p><b>Important:</b> Always call <pre>{@code connect()}</pre> before performing operations
 * and <pre>{@code close()}</pre> when finished.</p>
 *
 * <p>Example:</p>
 * <pre>{@code
 * Collection collection = CollectionSelector.getCollection(connection, "users").connect();
 * // perform operations...
 * collection.close();
 * }</pre>
 *
 * @author Weesli
 * @version 1.0
 */

public interface Collection {
    /**
     * Inserts a new object or updates an existing one in the collection.
     *
     * @param object The object to insert or update as a JSON string
     * @param id The unique identifier for the object. If the ID exists, the object will be updated;
     *           if the ID doesn't exist, a new object will be created with this ID
     * @return The inserted or updated object as a DatabaseObject
     */
    DatabaseObject insertOrUpdate(String object, String id);

    /**
     * Deletes an object from the collection.
     *
     * @param object The object to delete as a JSON string or object ID
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(String object);

    /**
     * Retrieves all objects from the collection.
     *
     * @return A list containing all DatabaseObjects in the collection
     */
    List<DatabaseObject> findAll();

    /**
     * Finds and returns an object by its unique identifier.
     *
     * @param id The unique identifier of the object to find
     * @return The DatabaseObject if found, or null if not found
     */
    DatabaseObject findById(String id);

    /**
     * Searches for objects that match the specified criteria.
     *
     * @param where The field name to search in
     * @param value The value to search for
     * @return A list of DatabaseObjects that match the search criteria
     */
    List<DatabaseObject> find(String where, String value);

    /**
     * Gets the name of this collection.
     *
     * @return The collection name
     */
    String name();

    /**
     * Establishes a connection to the collection.
     * Must be called before performing any operations.
     *
     * @return This Collection instance for chaining
     */
    Collection connect();

    /**
     * Closes the connection to the collection.
     * Should be called when done with operations to free resources.
     */
    void close();

    /**
     * Gets the ObjectMapper instance used by this collection.
     * Useful for JSON serialization/deserialization operations.
     *
     * @return The ObjectMapper instance
     */
    ObjectMapper mapper();
}