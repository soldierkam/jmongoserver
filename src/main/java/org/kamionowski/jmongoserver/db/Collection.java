/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.db;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;

import java.util.List;

/**
 * @author soldier
 */
public interface Collection {
    String getName();

    String getNamespace();

    List<BSONObject> query(IoSession session, BSONObject query, BSONObject selector);

    void insert(IoSession session, BSONObject document);

    long count(BSONObject query);
}
