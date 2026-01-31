package com.aau.storagemanagment.model;

import java.util.Map;


public abstract class AbstractModel {
    public abstract Object getIdentifier();
    public Map<String, Object> toMap() {
        return java.util.Collections.emptyMap();
    }
    @Override
    public String toString() {
        Object id = getIdentifier();
        return String.format("%s[id=%s]", this.getClass().getSimpleName(), id == null ? "null" : id.toString());
    }
}
