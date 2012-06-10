package org.kamionowski.jmongoserver.db.admin.cmd;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.kamionowski.jmongoserver.db.Database;
import org.kamionowski.jmongoserver.db.admin.Command;
import org.kamionowski.jmongoserver.db.admin.CommandCollection;
import org.kamionowski.jmongoserver.db.server.ServerMgr;

/**
 * User: soldier
 * Date: 07.06.12
 * Time: 00:05
 */
public class DropDbCmd implements Command {

    private final Database database;

    public DropDbCmd(CommandCollection collection) {
        this.database = collection.getDb();
    }

    @Override
    public String getName() {
        return "dropDatabase";
    }

    @Override
    public BSONObject doCmd(IoSession session, BSONObject obj) {
        ServerMgr.getInstance().dropDatabase(database.getName());
        BSONObject result = new BasicBSONObject(2);
        result.put("ok", 1);
        result.put("dropped", database.getName());
        return result;
    }
}
