package org.kamionowski.jmongoserver.tests;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mysema.query.mongodb.morphia.MorphiaQuery;
import com.mysema.query.types.path.EntityPathBase;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.kamionowski.jmongoserver.JMongoServer;
import org.kamionowski.jmongoserver.tests.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Unit test for simple App.
 */
public abstract class BaseTest {

    private final static Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected static Mongo mongo;
    protected static JMongoServer server;
    protected static Datastore ds;
    private static Morphia morphia;

    @BeforeClass
    public static void initMongo() throws Exception {
        log.info("Init test server");
        final int port = Math.max(1025, RandomUtils.JVM_RANDOM.nextInt() % 15000);
        server = new JMongoServer(port);
        server.listen();
        mongo = new Mongo("127.0.0.1:" + port);
        morphia = new Morphia();
        morphia.map(Person.class);
    }

    @AfterClass
    public static void destroMongo() {
        log.info("Dispose test server");
        mongo.close();
        server.dispose();
        mongo = null;
        server = null;
        ds = null;
        morphia = null;
    }

    @Before
    public void createDs() {
        ds = morphia.createDatastore(mongo, "test");
    }

    @After
    public void clearDs() {
        mongo.getDB("test").dropDatabase();
    }

    <T> MorphiaQuery<T> createQuery(EntityPathBase<T> entity) {
        return new MorphiaQuery<T>(morphia, ds, entity);
    }
}
