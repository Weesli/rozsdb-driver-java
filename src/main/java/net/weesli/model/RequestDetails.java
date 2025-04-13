package net.weesli.model;

import java.util.Map;

public record RequestDetails(Map<String, String> headers, String body, String ...paths) {
    public boolean hasHeader(String headerName) {
        return headers.containsKey(headerName);
    }

    public boolean hasPath(String path) {
        return paths.length > 0 && paths[0].equals(path);
    }

    public boolean hasQueryParams(Map<String, String> queryParams) {
        return queryParams.keySet().stream().allMatch(headers::containsKey);
    }

    public boolean hasBody() {
        return body!= null &&!body.isEmpty();
    }

    public boolean hasPathOrQueryParams(String path, Map<String, String> queryParams) {
        return hasPath(path) || hasQueryParams(queryParams);
    }

    public boolean hasHeaderAndBody(String headerName, String body) {
        return hasHeader(headerName) && hasBody() && headers.get(headerName).equals(body);
    }
}
