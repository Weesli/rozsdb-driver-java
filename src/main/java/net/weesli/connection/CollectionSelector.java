package net.weesli.connection;

import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;

/**
 * Select to any collection in database
 */
public class CollectionSelector {

    /**
     * Select to any collection in database
     * @param collectionName
     */
    public static Collection getCollection(Connection connection, String collectionName){
        return connection
                .getCollection(collectionName);
    }
}
