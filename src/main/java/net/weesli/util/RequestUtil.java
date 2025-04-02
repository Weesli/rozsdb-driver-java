package net.weesli.util;

import lombok.SneakyThrows;
import net.weesli.enums.CollectionActionType;
import net.weesli.interfaces.Connection;
import net.weesli.interfaces.RequestConsumer;
import net.weesli.model.ResponseStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestUtil {

    @SneakyThrows
    public static void send(Connection connection, CollectionActionType type, Object object, RequestConsumer consumer){
        String uri = "http://" + connection.getUriDetails().getHost() + ":" + connection.getUriDetails().getPort() + "/databases" + connection.getUriDetails().getDatabase();
        HttpRequest request = requestBuilder(connection,type, object, uri);
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        consumer.accept(new ResponseStatus(response.statusCode(), response.body()));
        client.close();
    }

    private static HttpRequest requestBuilder(Connection connection, CollectionActionType type, Object object, String uri){
        String typeName = typeConverter(type);
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.setHeader("admin", connection.getUriDetails().getUser() + "=" + connection.getUriDetails().getPassword());
        if (typeName.equals("GET")){
            builder.GET();
        }else {
            builder.POST(HttpRequest.BodyPublishers.ofString(object.toString()));
        }
        builder.uri(URI.create(uri));
        return builder.build();
    }

    private static String typeConverter(CollectionActionType type){
        if (type.equals(CollectionActionType.INSERTORUPDATE)){
            return "POST";
        }else {
            return "GET";
        }
    }

}
