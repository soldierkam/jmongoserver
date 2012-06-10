package org.kamionowski.jmongoserver.db.admin.cmd;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.kamionowski.jmongoserver.db.LastError;
import org.kamionowski.jmongoserver.db.admin.Command;
import org.kamionowski.jmongoserver.db.admin.CommandCollection;

/**
 * User: soldier
 * Date: 04.06.12
 * Time: 21:37
 */
public class GetLastErrorCmd implements Command {

    private final CommandCollection collection;

    public GetLastErrorCmd(CommandCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "getlasterror";
    }

    @Override
    public BSONObject doCmd(IoSession session, BSONObject obj) {
        LastError lastError = new LastError(session);
        BSONObject result = new BasicBSONObject(1);
        result.put("err", lastError.getMessage());
        result.put("n", lastError.getN());
        result.put("ok", lastError.getMessage() == null);
        return result;
    }
}
