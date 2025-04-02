package net.weesli.connection;

import com.google.gson.JsonSerializer;
import lombok.SneakyThrows;
import net.weesli.gson.GsonProvider;
import net.weesli.interfaces.Connection;
import net.weesli.model.ConnectionImpl;
import net.weesli.model.UriDetails;
import net.weesli.util.UriDecoder;

import static net.weesli.gson.GsonProvider.appendGson;

/**
 * Create a new connection to database
 */
public class ConnectionSelector {
    @SneakyThrows
    public static Connection getConnection(String uri, JsonSerializer<?> ...gson) {
        UriDetails uriDetails = UriDecoder.decode(uri);
        appendGson(gson);
        return new ConnectionImpl(uriDetails, uriDetails.getDatabase(), GsonProvider.gson);
    }
}
