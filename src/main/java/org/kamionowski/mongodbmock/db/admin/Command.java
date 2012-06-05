/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.db.admin;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;

/**
 *
 * @author soldier
 */
public interface Command {
    String getName();
    BSONObject doCmd(IoSession session, BSONObject obj);
}
