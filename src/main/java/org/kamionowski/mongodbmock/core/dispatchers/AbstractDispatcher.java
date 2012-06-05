package org.kamionowski.mongodbmock.core.dispatchers;

import org.apache.commons.lang.Validate;
import org.kamionowski.mongodbmock.core.Dispatcher;
import org.kamionowski.mongodbmock.db.server.Server;
import org.kamionowski.mongodbmock.msg.MongoRequest;

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
