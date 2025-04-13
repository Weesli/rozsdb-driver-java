package net.weesli;

import lombok.SneakyThrows;
import net.weesli.connection.ConnectionSelector;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;
import net.weesli.model.DatabaseObject;

import java.util.List;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        Connection connection = ConnectionSelector.getConnection("rozsdb:localhost:1212:example:root:");
        Collection collection = connection.getCollection("users").connect();

        for (int i = 0; i < 5; i++) {
            List<DatabaseObject> found = collection.findAll();
            found.get(0).asWith(User.class);
        }

        long startTime = System.nanoTime();
        List<DatabaseObject> objects = collection.findAll();
        objects.get(0).asWith(User.class);
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("System execution time: " + elapsedTime / 1_000_000 + "ms");
    }
}
