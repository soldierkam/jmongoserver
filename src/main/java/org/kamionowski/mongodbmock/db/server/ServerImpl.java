/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.db.server;

import org.apache.commons.lang.Validate;
import org.kamionowski.mongodbmock.db.Database;
import org.kamionowski.mongodbmock.db.admin.AdminDatabase;
import org.kamionowski.mongodbmock.db.test.TestDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author soldier
 */
class ServerImpl implements Server {

    private Map<String, Database> databases = new HashMap<String, Database>();
    
    protected ServerImpl() {
        register(new AdminDatabase());
        register(new TestDatabase());
    }
    
    private void register(Database db){
        Validate.isTrue(databases.containsKey(db.getName()) == false);
        databases.put(db.getName(), db);
    }

    @Override
    public Database getDatabase(String databaseName){
        return databases.get(databaseName);
    }

    @Override
    public Set<String> dbNames() {
        return databases.keySet();
    }

    @Override
    public Set<DbInfo> info() {
        Set<DbInfo> info = new HashSet<DbInfo>();
        for(Database db : this.databases.values()){
            info.add(new DbInfo(false, db.getName(), 1));//todo: get db info
        }
        return info;
    }
}
