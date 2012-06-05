/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock;

import org.kamionowski.mongodbmock.msg.MongoRequest;

/**
 *
 * @author soldier
 */
public class CursorNotFoundException extends RequestAwareException{

    public CursorNotFoundException(MongoRequest request) {
        super(request);
    }

    public CursorNotFoundException(String message, MongoRequest request) {
        super(message, request);
    }

    public CursorNotFoundException(String message, Throwable cause, MongoRequest request) {
        super(message, cause, request);
    }

    public CursorNotFoundException(Throwable cause, MongoRequest request) {
        super(cause, request);
    }
}
