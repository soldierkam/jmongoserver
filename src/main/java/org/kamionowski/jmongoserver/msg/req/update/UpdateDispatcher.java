package org.kamionowski.jmongoserver.msg.req.update;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.kamionowski.jmongoserver.db.Database;
import org.kamionowski.jmongoserver.db.server.Server;
import org.kamionowski.jmongoserver.msg.MessageHeader;
import org.kamionowski.jmongoserver.msg.req.AbstractDispatcher;
import org.kamionowski.jmongoserver.msg.res.MongoResponse;
import org.kamionowski.jmongoserver.msg.res.MongoResponseImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: soldier
 * Date: 07.06.12
 * Time: 17:35
 */
public class UpdateDispatcher extends AbstractDispatcher<UpdateRequest> {

    private static final Logger log = LoggerFactory.getLogger(UpdateDispatcher.class);

    public UpdateDispatcher(Server server) {
        super(server);
    }

    @Override
    public void dispatch(UpdateRequest request, IoSession session) {
        Validate.notNull(request);
        log.debug("Dispatching " + request);
        BSONObject update = request.getUpdate();
        BSONObject selector = request.getSelector();
        Database db = server.getDatabase(request.getDatabaseName());
        if (db == null) {
            db = server.createDatabase(request.getDatabaseName());
        }

        List<BSONObject> results = db.update(session, request.getCollectionName(), update, selector);
        MongoResponse response = new MongoResponseImpl.Builder()
                .setCursorID(0)//TODO
                .setDocuments(results)
                .setHeader(new MessageHeader(request.getId()))
                .setResponseFlags(0)
                .setStartingFrom(0)
                .build();
        session.write(response);
    }

    @Override
    public Class<UpdateRequest> supportedRequest() {
        return UpdateRequest.class;
    }

}
