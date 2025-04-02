package net.weesli.model;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import net.weesli.interfaces.Collection;

import static net.weesli.gson.GsonProvider.gson;

public class DatabaseObject {
    @Getter private String id;
    @Setter@Getter private String object;
    private Collection collection;  // reference to the collection object where this object is stored.

    public DatabaseObject(String str, Collection collection){
        JsonObject jsonObject = JsonParser.parseString(str).getAsJsonObject();
        this.id = jsonObject.get("id").getAsString();
        this.object = jsonObject.get("object").getAsString();
        this.collection = collection;  // reference to the collection object where this object is stored.
    }

    public DatabaseObject(Collection collection, String id, String object) {
        this.collection = collection;  // reference to the collection object where this object is stored.
        this.id = id;
        this.object = object;
    }

    // get as object with type
    public <T> T get(Class<?> type) {
        return (T) gson.fromJson(this.object, type);
    }

    public boolean save(){
        try {
            DatabaseObject status = collection.insertOrUpdate(this.object);
            this.id = status.getId();  // update the id of the object in the local database.
            this.object = status.getObject();  // update the object in the local database.
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public  boolean delete(){
        try {
            DatabaseObject status = collection.delete(this.object);
            this.id = status.getId();  // update the id of the object in the local database.
            this.object = status.getObject();  // update the object in the local database.
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
        return true;
    }

}
