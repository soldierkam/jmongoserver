package org.kamionowski.jmongoserver.msg.req;

import org.apache.commons.lang.Validate;
import org.kamionowski.jmongoserver.db.server.Server;

/**
 * User: soldier
 * Date: 04.06.12
 * Time: 20:54
 */
public abstract class AbstractDispatcher<T extends MongoRequest> implements Dispatcher<T> {

    protected Server server;

    public AbstractDispatcher(Server server) {
        Validate.notNull(server);
        this.server = server;
    }
}
