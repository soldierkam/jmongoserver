/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.db.admin;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.kamionowski.mongodbmock.db.Collection;
import org.kamionowski.mongodbmock.db.Database;
import org.kamionowski.mongodbmock.db.data.JsonCollection;
import org.kamionowski.mongodbmock.db.system.SystemNamespaceCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author soldier
 */
public class AdminDatabase implements Database{

    private static final Logger log = LoggerFactory.getLogger(AdminDatabase.class);

    Map<String, Collection> collections = new HashMap<String, Collection>();
    
    public AdminDatabase() {
        register(new CommandCollection(this));
        register(new SystemNamespaceCollection(this));
    }
    
    private void register(Collection collection){
        Validate.isTrue( collections.containsKey(collection.getName()) == false);
        collections.put(collection.getName(), collection);
    }
    
    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public List<BSONObject> query(IoSession session, String collectionName, BSONObject query, BSONObject selector) {
        log.debug("Query " + this.getName() + "." + collectionName + " for " + query);
        Collection collection = findCollection(collectionName);
        if(collection == null){
            throw new RuntimeException("Cannot find in db " + this.getName() + " collection named " + collectionName);
        }
        return collection.query(session, query, selector);
    }

    @Override
    public void insert(IoSession session, String collectionName, List<BSONObject> documents) {
        throw new UnsupportedOperationException();
    }

    Collection findCollection(String name){
        Collection collection = collections.get(name);
        if(collection != null){
            return collection;
        }
//        if(!name.contains(".")){
//            return null;
//        }
//        String collectionPrefix = name.substring(0, name.indexOf("."));
//        for(String collectionName : this.collections.keySet()){
//            if(collectionName.startsWith(collectionPrefix)){
//                return this.collections.get(collectionName);
//            }
//        }
        return null;
    }

    @Override
    public void createCollection(String name) {
        log.debug("Creating collection " + name);
        register(new JsonCollection(this, name));
    }


    @Override
    public Set<String> getCollectionsNames() {
        return this.collections.keySet();
    }
}
