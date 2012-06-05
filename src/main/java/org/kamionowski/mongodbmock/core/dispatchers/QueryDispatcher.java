/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.core.dispatchers;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.kamionowski.mongodbmock.db.Database;
import org.kamionowski.mongodbmock.db.server.Server;
import org.kamionowski.mongodbmock.msg.MessageHeader;
import org.kamionowski.mongodbmock.msg.MongoResponse;
import org.kamionowski.mongodbmock.msg.MongoResponseImpl;
import org.kamionowski.mongodbmock.msg.query.QueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * @author soldier
 */
public class QueryDispatcher extends AbstractDispatcher<QueryRequest>{

    private static final Logger log = LoggerFactory.getLogger(QueryDispatcher.class);

    public QueryDispatcher(Server server) {
        super(server);
    }
    
    @Override
    public void dispatch(QueryRequest request, IoSession session) {
        Validate.notNull(request);
        log.debug("Dispatching " + request);
        BSONObject query = request.getQuery();
        Database db = server.getDatabase(request.getDatabaseName());
        if(db == null){
            throw new RuntimeException("Cannot find db: " + request.getDatabaseName());
        }
        List<BSONObject> results = db.query(session, request.getCollectionName(), query, request.getReturnFieldSelector());
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
    public Class<QueryRequest> supportedRequest() {
        return QueryRequest.class;
    }
    
}
