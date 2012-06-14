package org.kamionowski.jmongoserver.db.data.query;

/**
 * User: soldier
 * Date: 13.06.12
 * Time: 21:56
 */
public class Value {
    final boolean exists;
    final Object value;

    public static final Value NOT_EXISTS = new Value(false);

    private Value(boolean exists) {
        this.exists = exists;
        this.value = null;
    }

    public Value(Object value) {
        this.exists = true;
        this.value = value;
    }

    public boolean isExists() {
        return exists;
    }

    public Object get() {
        return value;
    }
}
