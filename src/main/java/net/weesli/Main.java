package net.weesli;

import net.weesli.connection.CollectionSelector;
import net.weesli.connection.ConnectionSelector;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;

public class Main {

    public static void main(String[] args) {
        Connection connection = ConnectionSelector.getConnection("rozsdb:localhost:1212:example:root:");
        Collection collection = CollectionSelector.getCollection(connection, "example");
    }
}
