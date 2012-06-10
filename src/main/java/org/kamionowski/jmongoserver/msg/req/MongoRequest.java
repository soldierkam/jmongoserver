/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.msg.req;

import org.kamionowski.jmongoserver.msg.MessageHeader;

/**
 * @author soldier
 */
public interface MongoRequest {

    MessageHeader getHeader();

    int getId();
}
