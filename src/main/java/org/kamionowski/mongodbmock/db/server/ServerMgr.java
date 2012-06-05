package org.kamionowski.mongodbmock.db.server;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 19:10
 */
public class ServerMgr {
    private final static Server server = new ServerImpl();

    public static Server getInstance(){
        return server;
    }
}
