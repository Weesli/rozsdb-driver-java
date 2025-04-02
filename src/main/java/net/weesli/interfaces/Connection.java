package net.weesli.interfaces;

import net.weesli.model.UriDetails;

public interface Connection {
    String getDatabase();
    Collection getCollection(String name);
    UriDetails getUriDetails();
}
