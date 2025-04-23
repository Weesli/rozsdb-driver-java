package net.weesli.util;

import com.fasterxml.jackson.databind.JsonNode;
import net.weesli.enums.CollectionActionType;
import net.weesli.provider.ObjectMapperProvider;

import java.util.Map;

public class ChannelUtil {

    public static JsonNode getJson(Map<String, Object> map) {
        return ObjectMapperProvider.getInstance().valueToTree(map);
    }

    public static String getChannel(CollectionActionType type){
        return switch (type) {
            case FIND -> "find";
            case FINDALL -> "findall";
            case INSERTORUPDATE -> "insertorupdate";
            case DELETE -> "delete";
            case CONNECTION -> "connection";
            case FINDBYID -> "findbyid";
        };
    }
}
