package org.kamionowski.jmongoserver.db.admin.cmd;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.kamionowski.jmongoserver.db.admin.Command;
import org.kamionowski.jmongoserver.db.admin.CommandCollection;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 18:59
 */
public class CreateCollectionCmd implements Command {

    private final CommandCollection collection;

    public CreateCollectionCmd(CommandCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public BSONObject doCmd(IoSession session, BSONObject obj) {
        Object val = obj.get("create");
        if (!(val instanceof String)) {
            throw new RuntimeException("TODO");//TODO:error handler
        }
        String collectionName = (String) val;
        this.collection.getDb().createCollection(collectionName);
        BSONObject result = new BasicBSONObject(1);
        result.put("ok", 1);
        return result;
    }
}
