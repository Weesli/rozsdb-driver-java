package net.weesli.util;

import net.weesli.exception.UriFormatException;
import net.weesli.model.UriDetails;

public class UriDecoder {

    public static UriDetails decode(String encodedString) throws UriFormatException {
        if (!encodedString.startsWith("rozsdb:")){
            throw new UriFormatException("Invalid Uri format, uri must be start with rozsdb");
        }
        try {
            return new UriDetails(encodedString);
        }catch (Exception e){
            throw new UriFormatException("Invalid Uri format!");
        }
    }
}
