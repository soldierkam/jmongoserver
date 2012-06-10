/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.msg.req.query;

import org.apache.mina.core.buffer.IoBuffer;
import org.bson.BSONObject;
import org.kamionowski.jmongoserver.RequestBuilder;
import org.kamionowski.jmongoserver.msg.MessageHeader;
import org.kamionowski.jmongoserver.msg.MessageHeader.Operation;
import org.kamionowski.jmongoserver.msg.req.AbstractMongoCollectionRequest;
import org.kamionowski.jmongoserver.msg.req.MongoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author soldier
 */
public class QueryRequest extends AbstractMongoCollectionRequest {

    private final static Logger log = LoggerFactory.getLogger(QueryRequest.class);

    private int numberToSkip;
    private int numberToReturn;
    private BSONObject query; // query object.  See below for details.
    private BSONObject returnFieldSelector; // Optional. Selector indicating the fields
    //  to return.  See below for details.

    private final static Operation OPERATION = MessageHeader.Operation.OP_QUERY;

    public QueryRequest(MessageHeader header, IoBuffer buffer) throws IOException {
        super(header, OPERATION, buffer);
        readBody();
    }

    public static class Builder implements RequestBuilder {
        @Override
        public MongoRequest build(MessageHeader header, IoBuffer buffer) throws IOException {
            return new QueryRequest(header, buffer);
        }

        @Override
        public Operation supportedOperation() {
            return OPERATION;
        }
    }

    @Override
    protected void readBody() throws IOException {
        super.readBody();
        numberToSkip = buffer.getInt();
        numberToReturn = buffer.getInt();
        InputStream stream = buffer.asInputStream();
        query = readObject(stream);
        if (this.buffer.position() < this.header.getMessageLength()) {
            returnFieldSelector = readObject(buffer.asInputStream());
        }
    }


    public int getNumberToReturn() {
        return numberToReturn;
    }

    public int getNumberToSkip() {
        return numberToSkip;
    }

    public BSONObject getQuery() {
        return query;
    }

    public BSONObject getReturnFieldSelector() {
        return returnFieldSelector;
    }

    @Override
    public String toString() {
        return "QueryRequest{" + "flags=" + getFlags() + ", collectionName=" + getFullCollectionName() + ", numberToSkip=" + numberToSkip + ", numberToReturn=" + numberToReturn + ", query=" + query + ", returnFieldSelector=" + returnFieldSelector + '}';
    }

    public static final int FLAG_TAILABLE_CURSOR = 1 << 1;
    public static final int FLAG_SLAVE_OK = 1 << 2;
    public static final int FLAG_OPLOG_REPLAY = 1 << 3;
    public static final int FLAG_NO_CURSOR_TIMEOUT = 1 << 4;
    public static final int FLAG_AWAIT_DATA = 1 << 5;
    public static final int FLAG_EXHAUST = 1 << 6;
    public static final int FLAG_PARTIAL = 1 << 7;
}
