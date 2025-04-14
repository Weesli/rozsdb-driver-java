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
 * Utility class for creating a {@link Connection} to the database from a URI.
 * <p>
 * This class provides a static method to parse a database URI and create a
 * fully configured {@link Connection} instance. Optional custom Jackson
 * {@link JsonSerializer} instances can also be registered for object mapping.
 * </p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * Connection connection = ConnectionSelector.getConnection(
 *     "rozsdb:localhost:1212:example:root:",
 *     new MyCustomSerializer()
 * );
 * }</pre>
 *
 *
 * @author Weesli
 * @version 1.0
 */
public class ConnectionSelector {

    /**
     * Creates a {@link Connection} to the database using the given URI.
     * <p>
     * The URI is decoded into a {@link UriDetails} object. Any provided
     * {@link JsonSerializer} instances are added to the Jackson module used
     * by the system-wide {@link ObjectMapperProvider}.
     * </p>
     *
     * @param uri the full database connection URI
     * @param gson optional custom {@link JsonSerializer} instances to register
     * @return a configured {@link Connection} instance
     */
    @SneakyThrows
    public static Connection getConnection(String uri, JsonSerializer<?>... gson) {
        UriDetails uriDetails = UriDecoder.decode(uri);
        SimpleModule module = new SimpleModule();
        for (JsonSerializer<?> serializer : gson) {
            module.addSerializer(serializer);
        }
        ObjectMapperProvider.addModule(module);
        return new ConnectionImpl(uriDetails, uriDetails.getDatabase(), ObjectMapperProvider.getInstance());
    }
}
