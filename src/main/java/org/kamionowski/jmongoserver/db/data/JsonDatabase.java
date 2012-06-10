package org.kamionowski.jmongoserver.db.data;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.kamionowski.jmongoserver.db.Collection;
import org.kamionowski.jmongoserver.db.Database;
import org.kamionowski.jmongoserver.db.admin.CommandCollection;
import org.kamionowski.jmongoserver.db.system.SystemNamespaceCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: soldier
 * Date: 03.06.12
 * Time: 17:51
 */
public class JsonDatabase implements Database {

    private static final Logger log = LoggerFactory.getLogger(JsonDatabase.class);

    private final Map<String, Collection> collections = new HashMap<String, Collection>();
    private final String name;

    public JsonDatabase(String name) {
        this.name = name;
        register(new SystemNamespaceCollection(this));
        register(new CommandCollection(this));
    }

    private void register(Collection collection) {
        Validate.isTrue(collections.containsKey(collection.getName()) == false);
        collections.put(collection.getName(), collection);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<BSONObject> query(IoSession session, String collectionName, BSONObject query, BSONObject selector) {
        log.debug("Query " + this.getName() + "." + collectionName + " for " + query);
        Collection collection = findCollection(collectionName, false);
        if (collection == null) {
            throw new RuntimeException("Cannot find in db " + this.getName() + " collection named " + collectionName);
        }
        return collection.query(session, query, selector);
    }

    @Override
    public void insert(IoSession session, String collectionName, List<BSONObject> documents) {
        Collection collection = findCollection(collectionName, true);
        for (BSONObject document : documents) {
            collection.insert(session, document);
        }
    }

    Collection findCollection(String name, boolean createIfNotExists) {
        Collection collection = collections.get(name);

        if (collection == null && createIfNotExists) {
            createCollection(name);
            collection = collections.get(name);
        }
        return collection;
    }

    @Override
    public Collection getCollection(String name) {
        return findCollection(name, false);
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
