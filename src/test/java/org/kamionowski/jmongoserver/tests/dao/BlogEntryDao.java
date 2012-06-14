package org.kamionowski.jmongoserver.tests.dao;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;
import org.bson.types.ObjectId;
import org.kamionowski.jmongoserver.tests.domain.BlogEntry;

/**
 * User: soldier
 * Date: 14.06.12
 * Time: 21:14
 */
public class BlogEntryDao extends BasicDAO<BlogEntry, ObjectId> {
    public BlogEntryDao(Mongo mongo, Morphia morphia) {
        super(mongo, morphia, "BlogEntry");
    }
}
