/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.core;

import org.apache.mina.core.session.IoSession;
import org.kamionowski.mongodbmock.msg.MongoRequest;

/**
 *
 * @author soldier
 */
public interface RequestDispatcher {

    void dispatch(MongoRequest req, IoSession session);
}
