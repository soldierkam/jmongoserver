package org.kamionowski.jmongoserver.msg.req.insert;

import org.apache.mina.core.session.IoSession;
import org.kamionowski.jmongoserver.db.Database;
import org.kamionowski.jmongoserver.db.server.Server;
import org.kamionowski.jmongoserver.msg.req.AbstractDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: soldier
 * Date: 04.06.12
 * Time: 19:19
 */
public class InsertDispatcher extends AbstractDispatcher<InsertRequest> {

    private static final Logger log = LoggerFactory.getLogger(InsertDispatcher.class);

    public InsertDispatcher(Server server) {
        super(server);
    }

    @Override
    public void dispatch(InsertRequest req, IoSession session) {
        log.info("Request: " + req);
        Database db = server.getDatabase(req.getDatabaseName());
        if (db == null) {
            db = server.createDatabase(req.getDatabaseName());
        }
        db.insert(session, req.getCollectionName(), req.getDocuments());
    }

    @Override
    public Class<InsertRequest> supportedRequest() {
        return InsertRequest.class;
    }
}
