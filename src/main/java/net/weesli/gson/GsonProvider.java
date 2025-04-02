package net.weesli.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;

public class GsonProvider {

    public static Gson gson;

    public  static void appendGson(JsonSerializer<?>...args) {
        GsonBuilder builder = new GsonBuilder();
        for (Object obj : args) {
            builder.registerTypeAdapter(obj.getClass(), obj);
        }
        gson = builder.create();
    }


}
