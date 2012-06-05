package org.kamionowski.mongodbmock.db.data.query;

import org.bson.BSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 23:25
 */
public class QueryParserImpl implements QueryParser {

    private static final Logger log = LoggerFactory.getLogger(QueryParserImpl.class);

    private final static QueryParser instance = new QueryParserImpl();

    public static QueryParser getInstance() {
        return instance;
    }

    @Override
    public Condition parse(Condition currentCondition, BSONObject query) {
        Set<String> keys = query.keySet();
        if (keys.size() == 1) {
            String key = keys.iterator().next();
            Object value = query.get(key);
            return createCondition(currentCondition, key, value);
        } else {
            return new AndCondition(currentCondition, query);
        }
    }

    @Override
    public List<Condition> parseList(Condition currentCondition, BSONObject query) {
        List<Condition> conditions = new LinkedList<Condition>();
        for (String key : query.keySet()) {
            conditions.add(createCondition(currentCondition, key, query.get(key)));
        }
        return conditions;
    }

    private Condition createCondition(Condition currentCondition, String key, Object value) {
        log.info("key: " + key + " value: " + value.getClass().getName());
        switch (key) {
            case "$gt":
                return new GreaterCondition(currentCondition, value);
            case "$gte":
                return new GreaterCondition(currentCondition, value);
            case "$lt":
                return new LessCondition(currentCondition, value);
            case "$lte":
                return new LessCondition(currentCondition, value);
        }
        if (key.startsWith("$")) {
            throw new UnsupportedOperationException(key);
        }
        return new ValueCondition(currentCondition, key, value);
    }
}
