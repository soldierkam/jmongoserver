/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.db.admin;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.kamionowski.mongodbmock.db.Collection;
import org.kamionowski.mongodbmock.db.Database;
import org.kamionowski.mongodbmock.db.admin.cmd.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author soldier
 */
public class CommandCollection implements Collection {

    private final static Logger log = LoggerFactory.getLogger(CommandCollection.class);
    Map<String, Command> commands = new HashMap<String, Command>();
    private final Database db;

    public CommandCollection(Database db) {
        this.db = db;
        register(new WhatsMyURICmd(this));
        register(new ReplSetGetStatusCmd(this));
        register(new CreateCollectionCmd(this));
        register(new ListDatabasesCmd(this));
        register(new GetLastErrorCmd(this));
    }

    private void register(Command cmd) {
        Validate.isTrue(commands.containsKey(cmd.getName()) == false);
        commands.put(cmd.getName(), cmd);
    }

    @Override
    public String getName() {
        return "$cmd";
    }

    @Override
    public String getNamespace() {
        return db.getName() + "." + this.getName();
    }

    @Override
    public List<BSONObject> query(IoSession session, BSONObject query, BSONObject selector) {
        Validate.notNull(query);
        Set<String> keys = query.keySet();
        Validate.isTrue(keys.size() >= 1);
        Iterator<String> cmdName = keys.iterator();
        Command cmd = null;
        do {
            cmd = commands.get(cmdName.next());
        } while (cmd == null && cmdName.hasNext());
        Validate.notNull(cmd, "Cannot find command: " + StringUtils.join(keys.iterator(), ','));
        return Arrays.asList(cmd.doCmd(session, query));
    }

    @Override
    public void insert(IoSession session, BSONObject document) {
        throw new UnsupportedOperationException();
    }

    public Database getDb() {
        return db;
    }
}
