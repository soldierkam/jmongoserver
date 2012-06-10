/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.msg.req;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.session.IoSession;
import org.kamionowski.jmongoserver.db.server.Server;
import org.kamionowski.jmongoserver.msg.req.insert.InsertDispatcher;
import org.kamionowski.jmongoserver.msg.req.query.QueryDispatcher;
import org.kamionowski.jmongoserver.msg.req.update.UpdateDispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * @author soldier
 */
public class RequestDispatcherImpl implements RequestDispatcher {

    Map<Class<? extends MongoRequest>, Dispatcher> dispatchers = new HashMap<Class<? extends MongoRequest>, Dispatcher>();

    public RequestDispatcherImpl(Server server) {
        add(new QueryDispatcher(server));
        add(new InsertDispatcher(server));
        add(new UpdateDispatcher(server));
    }

    private void add(Dispatcher dispatcher) {
        Validate.isTrue(dispatchers.put(dispatcher.supportedRequest(), dispatcher) == null);
    }

    public void dispatch(MongoRequest req, IoSession session) {
        Dispatcher dispatcher = dispatchers.get(req.getClass());
        Validate.notNull(dispatcher, "Cannot find dispatcher for " + req.getClass());
        dispatcher.dispatch(req, session);
    }

}
