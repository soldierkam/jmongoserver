package org.kamionowski.mongodbmock.db.server;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 19:26
 */
public class DbInfo {
    private final boolean empty;
    private final String name;
    private final int sizeOnDisk;

    public DbInfo(boolean empty, String name, int sizeOnDisk) {
        this.empty = empty;
        this.name = name;
        this.sizeOnDisk = sizeOnDisk;
    }

    public boolean isEmpty() {
        return empty;
    }

    public String getName() {
        return name;
    }

    public int getSizeOnDisk() {
        return sizeOnDisk;
    }
}
