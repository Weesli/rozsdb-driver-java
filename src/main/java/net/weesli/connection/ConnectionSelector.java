package net.weesli.connection;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import net.weesli.interfaces.Connection;
import net.weesli.mapper.ObjectMapperProvider;
import net.weesli.model.ConnectionImpl;
import net.weesli.model.UriDetails;
import net.weesli.util.UriDecoder;

/**
 * Create a new connection to database
 */
public class ConnectionSelector {
    @SneakyThrows
    public static Connection getConnection(String uri, JsonSerializer<?>...gson) {
        UriDetails uriDetails = UriDecoder.decode(uri);
        SimpleModule module = new SimpleModule();
        for (JsonSerializer<?> serializer : gson) {
            module.addSerializer(serializer);
        }
        ObjectMapperProvider.addModule(module);
        return new ConnectionImpl(uriDetails, uriDetails.getDatabase(), ObjectMapperProvider.getInstance());
    }
}
