package org.kamionowski.jmongoserver.db.data.query;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 21:01
 */
public interface Condition {

    boolean filter(Object document);

    String fieldName();

    int level();
}
