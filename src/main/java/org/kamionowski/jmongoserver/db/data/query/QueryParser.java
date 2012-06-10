package org.kamionowski.jmongoserver.db.data.query;

import org.bson.BSONObject;

import java.util.List;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 22:56
 */
public interface QueryParser {

    Condition parse(Condition currentCondition, BSONObject query);

    List<Condition> parseList(Condition currentCondition, BSONObject query);
}
