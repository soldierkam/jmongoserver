package org.kamionowski.jmongoserver.db.data;

import org.bson.BSONObject;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 20:14
 */
public interface BsonVisitator {
    void visit(BSONObject obj);
}
