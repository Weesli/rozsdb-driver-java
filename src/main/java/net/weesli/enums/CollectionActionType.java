package net.weesli.enums;

public enum CollectionActionType {
    INSERTORUPDATE,
    DELETE,
    FINDALL,
    FINDBYID,
    FIND,
    CONNECTION;

    private final String actionType;
    private CollectionActionType() {
        this.actionType = name().toLowerCase();
    }

    public String getChannel() {
        return actionType;
    }

}
