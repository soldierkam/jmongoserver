/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.db.server;

import org.kamionowski.mongodbmock.db.Database;

import java.util.Set;

/**
 *
 * @author soldier
 */
public interface Server {
    Database getDatabase(String databaseName);
    Set<String> dbNames();
    Set<DbInfo> info();
}
