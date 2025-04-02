package net.weesli.interfaces;

import net.weesli.model.ResponseStatus;

@FunctionalInterface
public interface RequestConsumer {
    void accept(ResponseStatus response);
}
