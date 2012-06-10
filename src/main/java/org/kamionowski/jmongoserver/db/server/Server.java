/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.db.server;

import org.kamionowski.jmongoserver.db.Database;

import java.util.Set;

/**
 * @author soldier
 */
public interface Server {
    Database getDatabase(String databaseName);

    void dropDatabase(String databaseName);

    Database createDatabase(String databaseName);

    Set<String> dbNames();

    Set<DbInfo> info();
}
