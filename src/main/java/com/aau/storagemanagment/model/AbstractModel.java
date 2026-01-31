package com.aau.storagemanagment.model;

import java.util.Map;

/**
 * Base abstract class for domain models.
 *
 * Concrete model classes should provide a stable identifier via {@link #getIdentifier()}
 * and supply a simple map representation via {@link #toMap()} for debugging/logging or
 * lightweight serialization.
 */
public abstract class AbstractModel {

    /**
     * Return a stable identifier for this model instance (for example the DB id or unique key).
     */
    public abstract Object getIdentifier();

    /**
     * Return a simple map of field names to values. Implementations should include the primary
     * fields that represent the model. This default implementation returns an empty map and
     * can be overridden by subclasses.
     */
    public Map<String, Object> toMap() {
        return java.util.Collections.emptyMap();
    }

    @Override
    public String toString() {
        Object id = getIdentifier();
        return String.format("%s[id=%s]", this.getClass().getSimpleName(), id == null ? "null" : id.toString());
    }
}
