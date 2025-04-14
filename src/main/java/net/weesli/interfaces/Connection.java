package net.weesli.interfaces;

import net.weesli.model.UriDetails;

/**
 * Represents a connection to a RozsDB database instance.
 * <p>
 * Provides access to collections within the connected database and exposes
 * metadata about the connection, such as the database name and URI details.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * Connection connection = ConnectionSelector.getConnection("rozsdb:localhost:1212:example:root:");
 * String dbName = connection.getDatabase();
 * Collection users = connection.getCollection("users");
 * users.connect();
 * // Perform operations...
 * users.close();
 * }</pre>
 *
 * @see Collection
 * @see UriDetails
 * @see net.weesli.connection.ConnectionSelector
 *
 * @author Weesli
 * @version 1.0
 */
public interface Connection {

    /**
     * Returns the name of the currently connected RozsDB database.
     *
     * @return the database name
     */
    String getDatabase();

    /**
     * Retrieves a {@link Collection} instance by name from the connected database.
     * <p>
     * Always call <pre>{@code connect()}</pre> on the returned collection before performing any operations,
     * and <pre>{@code close()}</pre> once you're done.
     * </p>
     *
     * @param name the name of the collection to retrieve
     * @return the corresponding Collection instance
     */
    Collection getCollection(String name);

    /**
     * Returns the parsed details of the RozsDB connection URI.
     *
     * @return a {@link UriDetails} object representing the connection metadata
     */
    UriDetails getUriDetails();
}
