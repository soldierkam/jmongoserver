/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.msg;

/**
 *
 * @author soldier
 */
public interface MongoRequest {
 
    MessageHeader getHeader();
    
    int getId();
}
