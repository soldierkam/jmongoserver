package org.kamionowski.jmongoserver.db.admin.cmd;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.kamionowski.jmongoserver.db.admin.Command;
import org.kamionowski.jmongoserver.db.admin.CommandCollection;

/**
 * User: soldier
 * Date: 05.06.12
 * Time: 19:53
 */
public class IsMasterCmd implements Command {

    private final CommandCollection collection;

    public IsMasterCmd(CommandCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "isMaster";
    }

    @Override
    public BSONObject doCmd(IoSession session, BSONObject obj) {
        BSONObject result = new BasicBSONObject(3);
        result.put("ok", 1);
        result.put("ismaster", true);
        result.put("maxBsonObjectSize", 16777216);//TODO:max size?
        return result;
    }
}
