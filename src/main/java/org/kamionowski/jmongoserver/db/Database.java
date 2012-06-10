/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.db;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;

import java.util.List;
import java.util.Set;

/**
 * @author soldier
 */
public interface Database {
    String getName();

    List<BSONObject> query(IoSession session, String collectionName, BSONObject query, BSONObject selector);

    void insert(IoSession session, String collectionName, List<BSONObject> documents);

    List<BSONObject> update(IoSession session, String collectionName, BSONObject update, BSONObject selector);

    void createCollection(String name);

    Set<String> getCollectionsNames();

    Collection getCollection(String name);
}
