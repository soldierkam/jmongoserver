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
public class RequestAwareException extends RuntimeException {

    private final MongoRequest request;

    public RequestAwareException(Throwable cause, MongoRequest request) {
        super(cause);
        this.request = request;
    }

    public RequestAwareException(String message, Throwable cause, MongoRequest request) {
        super(message, cause);
        this.request = request;
    }

    public RequestAwareException(String message, MongoRequest request) {
        super(message);
        this.request = request;
    }

    public RequestAwareException(MongoRequest request) {
        this.request = request;
    }

    public MongoRequest getRequest() {
        return request;
    }
}
