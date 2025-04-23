package net.weesli.client;


import com.fasterxml.jackson.databind.JsonNode;
import net.weesli.enums.CollectionActionType;
import net.weesli.interfaces.Collection;
import net.weesli.model.UriDetails;
import net.weesli.provider.ObjectMapperProvider;
import net.weesli.util.ChannelUtil;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DatabaseClient {

    private final Socket socket;
    private final UriDetails uriDetails;

    public DatabaseClient(UriDetails uriDetails) {
        this.uriDetails = uriDetails;
        this.socket = new Socket();
        try {
            this.socket.connect(new InetSocketAddress(uriDetails.getHost(),uriDetails.getPort()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close() throws Exception {
        this.socket.close();
    }

    public boolean isClosed() {
        return this.socket.isClosed();
    }

    public byte[] send(CollectionActionType actionType, Collection collection, String data) throws Exception {
        JsonNode node = ChannelUtil.getJson(Map.of(
                "action", ChannelUtil.getChannel(actionType),
                "database", uriDetails.getDatabase(),
                "collection", collection.name(),
                "user", uriDetails.getUser() + "=" + uriDetails.getPassword(),
                "object", ObjectMapperProvider.getInstance().readTree(data)
        ));

        socket.setSoTimeout(30000);

        OutputStream outputStream = socket.getOutputStream();
        byte[] jsonBytes = node.toString().getBytes(StandardCharsets.UTF_8);

        outputStream.write(ByteBuffer.allocate(4).putInt(jsonBytes.length).array());
        outputStream.write(jsonBytes);
        outputStream.flush();

        return getByteArrayResponse();
    }

    private byte[] getByteArrayResponse() throws IOException {
        InputStream inputStream = socket.getInputStream();

        byte[] lenBytes = new byte[4];
        int readBytes = inputStream.read(lenBytes);
        if (readBytes < 4) {
            return new byte[0];
        }
        int length = ByteBuffer.wrap(lenBytes).getInt();

        byte[] data = new byte[length];
        int totalRead = 0;
        while (totalRead < length) {
            int bytesRead = inputStream.read(data, totalRead, length - totalRead);
            if (bytesRead == -1) break;
            totalRead += bytesRead;
        }

        if (totalRead != length) {
            throw new IOException("Expected " + length + " bytes, but only " + totalRead + " bytes read");
        }
        return data;
    }

}
