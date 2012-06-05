/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.msg;

import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * @author soldier
 */
public interface MongoResponse {
    
    IoBuffer write();    
}
