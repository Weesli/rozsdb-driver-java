package net.weesli.connection;

import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;

/**
 * Utility class for selecting collections from a database connection.
 * <p>
 * This class provides a static method to retrieve a {@link Collection} instance
 * from a given {@link Connection} by specifying the collection name.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * Connection connection = ConnectionSelector.getConnection("rozsdb:localhost:1212:example:root:");
 * Collection users = CollectionSelector.getCollection(connection, "users");
 * users.connect();
 * // Perform operations...
 * users.close();
 * }</pre>
 *
 * @author Weesli
 * @version 1.0
 */
public class CollectionSelector {

    /**
     * Returns a {@link Collection} instance by name using the provided {@link Connection}.
     * <p>
     * Always ensure to call <pre>{@code connect()}</pre> on the returned collection
     * before performing operations, and <pre>{@code close()}</pre> afterwards.
     * </p>
     *
     * @param connection the active database connection
     * @param collectionName the name of the collection to retrieve
     * @return the selected Collection instance
     */
    public static Collection getCollection(Connection connection, String collectionName) {
        return connection.getCollection(collectionName);
    }
}
