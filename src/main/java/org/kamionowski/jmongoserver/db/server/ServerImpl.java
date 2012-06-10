/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.db.server;

import org.apache.commons.lang.Validate;
import org.kamionowski.jmongoserver.db.Database;
import org.kamionowski.jmongoserver.db.admin.AdminDatabase;
import org.kamionowski.jmongoserver.db.data.JsonDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author soldier
 */
class ServerImpl implements Server {

    private Map<String, Database> databases = new HashMap<String, Database>();

    protected ServerImpl() {
        register(new AdminDatabase());
        register(new JsonDatabase("test"));
    }

    private void register(Database db) {
        Validate.isTrue(databases.containsKey(db.getName()) == false);
        databases.put(db.getName(), db);
    }

    @Override
    public Database getDatabase(String databaseName) {
        return databases.get(databaseName);
    }

    @Override
    public Database createDatabase(String databaseName) {
        register(new JsonDatabase(databaseName));
        return getDatabase(databaseName);
    }

    @Override
    public void dropDatabase(String databaseName) {
        databases.remove(databaseName);
    }

    @Override
    public Set<String> dbNames() {
        return databases.keySet();
    }

    @Override
    public Set<DbInfo> info() {
        Set<DbInfo> info = new HashSet<DbInfo>();
        for (Database db : this.databases.values()) {
            info.add(new DbInfo(false, db.getName(), 1));//todo: get db info
        }
        return info;
    }
}
