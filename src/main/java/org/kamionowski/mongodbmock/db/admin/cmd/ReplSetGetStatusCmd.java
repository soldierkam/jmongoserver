package org.kamionowski.mongodbmock.db.admin.cmd;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.kamionowski.mongodbmock.db.admin.Command;
import org.kamionowski.mongodbmock.db.admin.CommandCollection;

/**
 * User: soldier
 * Date: 26.05.12
 * Time: 10:30
 */
public class ReplSetGetStatusCmd implements Command {

    private final CommandCollection collection;

    public ReplSetGetStatusCmd(CommandCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "replSetGetStatus";
    }

    @Override
    public BSONObject doCmd(IoSession session, BSONObject obj) {
        Object forShell = obj.get("forShell");
        boolean isForShell = forShell != null && Integer.valueOf(1).equals(forShell);
        //todo: REP
        BSONObject response = new BasicBSONObject(2);
        response.put("errmsg", "not running with --replSet");
        response.put("ok", 0);
        return response;
    }
}
