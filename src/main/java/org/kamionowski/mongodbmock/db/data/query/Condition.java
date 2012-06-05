package org.kamionowski.mongodbmock.db.data.query;

import org.bson.BSONObject;

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
