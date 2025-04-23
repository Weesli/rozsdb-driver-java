package net.weesli;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.weesli.connection.ConnectionSelector;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;
import net.weesli.model.DatabaseObject;
import net.weesli.provider.ObjectMapperProvider;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            Connection connection = ConnectionSelector.getConnection("rozsdb:localhost:1212:example:root:");
            Collection collection = connection.getCollection("users").connect();
            for (int i = 0; i < 10; i++) {
                ObjectNode node = ObjectMapperProvider.getInstance().createObjectNode();
                node.put("name", "John Doe");
                node.put("age", 30);
                DatabaseObject object = collection.findById("zpjL8poKjghA");
            }
            long start = System.nanoTime();
            DatabaseObject object = collection.findById("zpjL8poKjghA");
            System.out.println(object.getObject());
            long end = System.nanoTime();

            double elapsedMs = (end - start) / 1_000_000.0;
            System.out.printf("Time: %.3f ms%n", elapsedMs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
