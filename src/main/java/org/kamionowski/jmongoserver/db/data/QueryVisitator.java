package org.kamionowski.jmongoserver.db.data;

import org.bson.BSONObject;
import org.kamionowski.jmongoserver.db.data.query.Condition;
import org.kamionowski.jmongoserver.db.data.query.QueryParser;
import org.kamionowski.jmongoserver.db.data.query.QueryParserImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 20:22
 */
public class QueryVisitator implements BsonVisitator {

    private final static Logger log = LoggerFactory.getLogger(QueryVisitator.class);
    private final BSONObject query;
    private final BSONObject select;
    private final Condition condition;
    private final List<BSONObject> documents;

    public QueryVisitator(BSONObject query, BSONObject select) {
        this.query = query;
        this.documents = new LinkedList<BSONObject>();
        this.select = select;
        this.condition = parseQuery();
    }

    public QueryVisitator(BSONObject query) {
        this.query = query;
        this.documents = new LinkedList<BSONObject>();
        this.select = null;
        this.condition = parseQuery();
    }

    private Condition parseQuery() {
        QueryParser parser = QueryParserImpl.getInstance();
        Condition condition = parser.parse(null, this.query);
        log.info("Condition: \n" + condition);
        return condition;
    }

    @Override
    public void visit(BSONObject obj) {
        if (condition.filter(obj)) {
            documents.add(obj);
        }
    }

    public List<BSONObject> getDocuments() {
        return documents;
    }

    public long count() {
        return documents.size();
    }
}

