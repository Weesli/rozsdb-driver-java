package net.weesli.util;

import lombok.SneakyThrows;
import net.weesli.enums.CollectionActionType;
import net.weesli.http.DatabaseClient;
import net.weesli.interfaces.Collection;
import net.weesli.interfaces.Connection;
import net.weesli.model.RequestDetails;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {

    @SneakyThrows
    public static HttpResponse<String> send(DatabaseClient client, Collection collection, Connection connection, CollectionActionType type, RequestDetails requestDetails){
        return requestBuilder(client, connection,type, requestDetails);
    }

    @SneakyThrows
    private static HttpResponse<String> requestBuilder(DatabaseClient client, Connection connection, CollectionActionType type, RequestDetails details){
        String typeName = typeConverter(type);
        Map<String,String> headers = new HashMap<>();
        headers.put("admin", connection.getUriDetails().getUser() + "=" + connection.getUriDetails().getPassword());
        headers.putAll(details.headers());
        if (typeName.equals("GET")){
            return client.get(getPath(type), headers);
        }else {
            return client.post(getPath(type), (details.hasBody() ? details.body() : ""), headers);
        }
    }

    private static String typeConverter(CollectionActionType type){
        if (type.equals(CollectionActionType.CONNECTION)){
            return "GET";
        }else {
            return "POST";
        }
    }

    private static String getPath(CollectionActionType type){
        StringBuilder builder = new StringBuilder();
        switch (type){
            case INSERTORUPDATE -> {
                builder.append("insertorupdate");
            }
            case DELETE -> {
                builder.append("delete");
            }
            case FIND -> {
                builder.append("find");
            }
            case FINDALL -> {
                builder.append("findall");
            }
            case FINDBYID -> {
                builder.append("findbyid");
            }
        }
        return builder.toString();
    }

}
