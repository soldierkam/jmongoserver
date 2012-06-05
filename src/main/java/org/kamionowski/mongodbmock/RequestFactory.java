/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock;

import org.apache.mina.core.buffer.IoBuffer;
import org.kamionowski.mongodbmock.msg.MongoRequest;

import java.io.IOException;

/**
 * @author soldier
 */
public interface RequestFactory {

    MongoRequest build(IoBuffer buffer) throws IOException;

    boolean isCompleteRequest(IoBuffer buffer) throws IOException;
}
