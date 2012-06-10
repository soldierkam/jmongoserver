/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.msg.req;

import org.apache.mina.core.session.IoSession;

/**
 * @author soldier
 */
public interface Dispatcher<T extends MongoRequest> {
    void dispatch(T req, IoSession session);

    Class<T> supportedRequest();
}
