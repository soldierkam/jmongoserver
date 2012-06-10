package org.kamionowski.jmongoserver.db.admin.cmd;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.kamionowski.jmongoserver.db.Collection;
import org.kamionowski.jmongoserver.db.admin.Command;
import org.kamionowski.jmongoserver.db.admin.CommandCollection;

/**
 * User: soldier
 * Date: 05.06.12
 * Time: 20:02
 */
public class CountCmd implements Command {
    private final CommandCollection collection;

    public CountCmd(CommandCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "count";
    }

    @Override
    public BSONObject doCmd(IoSession session, BSONObject obj) {
        String collectionName = obj.get("count").toString();
        Collection c = collection.getDb().getCollection(collectionName);
        BSONObject result = new BasicBSONObject();
        result.put("ok", 1);
        if (c == null) {
            result.put("missing", true);
            result.put("n", 0);
        } else {
            result.put("n", c.count((BSONObject) obj.get("query")));
        }
        return result;
    }
}
