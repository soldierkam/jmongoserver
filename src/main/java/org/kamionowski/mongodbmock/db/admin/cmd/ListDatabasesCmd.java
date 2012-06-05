package org.kamionowski.mongodbmock.db.admin.cmd;

import org.apache.mina.core.session.IoSession;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BasicBSONList;
import org.kamionowski.mongodbmock.db.admin.Command;
import org.kamionowski.mongodbmock.db.admin.CommandCollection;
import org.kamionowski.mongodbmock.db.server.DbInfo;
import org.kamionowski.mongodbmock.db.server.ServerMgr;

import java.util.Set;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 19:19
 */
public class ListDatabasesCmd implements Command {

    private final CommandCollection collection;

    public ListDatabasesCmd(CommandCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "listDatabases";
    }

    @Override
    public BSONObject doCmd(IoSession session, BSONObject obj) {
        Set<DbInfo> info = ServerMgr.getInstance().info();
        BSONObject result = new BasicBSONObject();
        int totalSize = 0;
        BasicBSONList list = new BasicBSONList();
        for (DbInfo dbInfo : info) {
            BSONObject db = new BasicBSONObject(3);
            db.put("name", dbInfo.getName());
            db.put("sizeOnDisk", dbInfo.getSizeOnDisk());
            db.put("empty", dbInfo.isEmpty());
            list.add(db);
            totalSize += dbInfo.getSizeOnDisk();
        }
        result.put("databases", list);
        result.put("totalSize", totalSize);
        return result;
    }
}
