package org.kamionowski.mongodbmock.core.dispatchers;

import org.apache.mina.core.session.IoSession;
import org.kamionowski.mongodbmock.db.Database;
import org.kamionowski.mongodbmock.db.server.Server;
import org.kamionowski.mongodbmock.msg.insert.InsertRequest;
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
        log.info("Request: "  + req);
        Database db = server.getDatabase(req.getDatabaseName());
        db.insert(session, req.getCollectionName(), req.getDocuments());
    }

    @Override
    public Class<InsertRequest> supportedRequest() {
        return InsertRequest.class;
    }
}
