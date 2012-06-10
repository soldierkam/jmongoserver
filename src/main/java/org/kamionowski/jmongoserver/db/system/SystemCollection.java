package org.kamionowski.jmongoserver.db.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.kamionowski.jmongoserver.db.Collection;
import org.kamionowski.jmongoserver.db.Database;
import org.kamionowski.jmongoserver.db.data.BsonVisitator;
import org.kamionowski.jmongoserver.db.data.DocumentSet;
import org.kamionowski.jmongoserver.db.data.QueryVisitator;

import java.util.List;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 19:45
 */
abstract class SystemCollection implements Collection {
    private final static Log LOG = LogFactory.getLog(SystemCollection.class);
    protected final Database database;

    public SystemCollection(Database database) {
        this.database = database;
    }

    @Override
    public abstract String getName();

    @Override
    public String getNamespace() {
        return database.getName() + "." + this.getName();
    }

    @Override
    public List<BSONObject> query(IoSession session, BSONObject query, BSONObject selector) {
        LOG.debug("Query: " + query);
        QueryVisitator queryVisitator = new QueryVisitator(query, selector);
        visit(queryVisitator);
        return queryVisitator.getDocuments();
    }

    private void visit(BsonVisitator visitator) {
        for (BSONObject obj : data()) {
            visitator.visit(obj);
        }
    }

    @Override
    public void insert(IoSession session, BSONObject document) {
        throw new UnsupportedOperationException();
    }

    protected abstract DocumentSet data();
}
