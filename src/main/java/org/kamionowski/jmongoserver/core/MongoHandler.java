/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.core;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.kamionowski.jmongoserver.CursorNotFoundException;
import org.kamionowski.jmongoserver.RequestAwareException;
import org.kamionowski.jmongoserver.db.server.Server;
import org.kamionowski.jmongoserver.db.server.ServerMgr;
import org.kamionowski.jmongoserver.msg.MessageHeader;
import org.kamionowski.jmongoserver.msg.req.MongoRequest;
import org.kamionowski.jmongoserver.msg.req.RequestDispatcher;
import org.kamionowski.jmongoserver.msg.req.RequestDispatcherImpl;
import org.kamionowski.jmongoserver.msg.res.MongoResponse;
import org.kamionowski.jmongoserver.msg.res.MongoResponseImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author soldier
 */
public class MongoHandler implements IoHandler {

    private final static Logger log = LoggerFactory.getLogger(MongoHandler.class);
    private final Server server;
    private final RequestDispatcher dispatcher;

    public MongoHandler() {
        this.server = ServerMgr.getInstance();
        this.dispatcher = new RequestDispatcherImpl(server);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        log.error("Exception in session" + session, cause);
        if (!(cause instanceof RequestAwareException)) {
            log.error("Unsupported exception " + cause.getClass(), cause);
            log.info("Force close connection to " + session.getRemoteAddress());
            session.close(false);
            return;
        }
        RequestAwareException exc = (RequestAwareException) cause;
        MongoRequest request = exc.getRequest();
        int flags = 0;
        if (cause instanceof CursorNotFoundException) {
            flags = MongoResponseImpl.RES_FLAG_CURSOR_NOT_FOUND;
        } else {
            throw new IllegalArgumentException("Unhandled exception type", cause);
        }
        MongoResponse response = new MongoResponseImpl.Builder()
                .setCursorID(0)//TODO
                .setDocuments(null)
                .setHeader(new MessageHeader(request.getId()))
                .setStartingFrom(0)
                .build();

        session.write(response);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        MongoRequest req = (MongoRequest) message;
        dispatcher.dispatch(req, session);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        log.trace("Sent msg of class " + message.getClass());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
    }
}
