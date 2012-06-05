package org.kamionowski.mongodbmock.db.data;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.kamionowski.mongodbmock.db.Collection;
import org.kamionowski.mongodbmock.db.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 19:05
 */
public class JsonCollection implements Collection {

    private final static Logger log = LoggerFactory.getLogger(JsonCollection.class);
    private final String name;
    private final Database db;
    private final DocumentSet documents;

    public JsonCollection(Database db, String name) {
        this.db = db;
        this.name = name;
        this.documents = DocumentSet.unique();
    }

    @Override
    public String getNamespace() {
        return db.getName() + "." + this.getName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void insert(IoSession session, BSONObject document) {
        this.documents.add(document);
    }

    @Override
    public List<BSONObject> query(IoSession session, BSONObject query, BSONObject selector) {
        log.debug("Query: " + query);
        QueryVisitator queryVisitator = new QueryVisitator(query, selector);
        for(BSONObject document : documents){
            queryVisitator.visit(document);
        }
        return queryVisitator.getDocuments();
    }
}
