package org.kamionowski.mongodbmock.db.system;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.kamionowski.mongodbmock.db.Database;
import org.kamionowski.mongodbmock.db.data.DocumentSet;

import java.util.Set;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 20:12
 */
public class SystemNamespaceCollection extends SystemCollection {
    public SystemNamespaceCollection(Database database) {
        super(database);
    }

    @Override
    public String getName() {
        return "system.namespaces";
    }

    @Override
    protected DocumentSet data() {
        String dbName = this.database.getName();
        Set<String> names = this.database.getCollectionsNames();
        DocumentSet documents = DocumentSet.nonUnique();
        for (String name : names) {
            BSONObject document = new BasicBSONObject(1);
            document.put("name", dbName + "." + name);
            documents.add(document);
        }
        return documents;
    }

}
