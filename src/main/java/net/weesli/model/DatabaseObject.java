package net.weesli.model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.airlift.compress.zstd.ZstdDecompressor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.weesli.ZSTDCompressorProvider;
import net.weesli.interfaces.Collection;
import net.weesli.mapper.ObjectMapperProvider;

import java.util.Base64;


public class DatabaseObject {
    @Getter private String id;
    @Setter@Getter private String object;
    private Collection collection;  // reference to the collection object where this object is stored.

    @SneakyThrows
    public DatabaseObject(String str, Collection collection) {
        // Decode the base64 string
        byte[] decodedBytes = Base64.getDecoder().decode(str);
        byte[] decompressedBytes = new byte[str.length() * 10];
        ZSTDCompressorProvider.getDecompressor().decompress(decodedBytes, 0, decodedBytes.length, decompressedBytes, 0, decompressedBytes.length);
        String decompressed = new String(decompressedBytes);
        ObjectMapper mapper = collection.mapper();
        ObjectNode node = (ObjectNode) mapper.readTree(decompressed);
        this.id = node.get("$id").toString();
        node.remove("$id");
        this.object = node.toString();
        this.collection = collection;
    }


    public DatabaseObject(Collection collection, String id, String object) {
        this.collection = collection;  // reference to the collection object where this object is stored.
        this.id = id;
        this.object = object;
    }

    // get as object with type
    public <T> T asWith(Class<T> type) {
        try {
            ObjectMapper mapper = collection.mapper();
            JsonNode node = mapper.readTree(this.object);
            return mapper.treeToValue(node, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean save(){
        try {
            DatabaseObject status = collection.insertOrUpdate(this.object, this.id);
            this.id = status.getId();  // update the id of the object in the local database.
            this.object = status.getObject();  // update the object in the local database.
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public  boolean delete(){
        boolean status = collection.delete(this.object);
        if (status){
            this.id = null;  // update the id of the object in the local database.
            this.object = null;  // update the object in the local database.
        }
        return status;
    }

}
