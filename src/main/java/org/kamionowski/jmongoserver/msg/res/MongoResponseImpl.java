/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.msg.res;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.buffer.IoBuffer;
import org.bson.BSONObject;
import org.kamionowski.jmongoserver.msg.MessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.List;

/**
 * @author soldier
 */
public class MongoResponseImpl extends AbstractMongoResponse {
    private static final Logger log = LoggerFactory.getLogger(MongoResponseImpl.class);

    private final int responseFlags;  // bit vector - see details below
    private final long cursorID;       // cursor id if client needs to do get more's
    private final int startingFrom;   // where in the cursor this reply is starting
    private final int numberReturned; // number of documents in the reply
    private final List<BSONObject> documents;      // documents

    private MongoResponseImpl(Builder builder) {
        super(builder.getHeader());
        this.responseFlags = builder.getResponseFlags();
        this.cursorID = builder.getCursorID();
        this.startingFrom = builder.getStartingFrom();
        this.documents = builder.getDocuments();
        this.numberReturned = documents.size();
        header.setMessageLength(this.size());
    }

    public static final class Builder {
        private MessageHeader header;
        private int responseFlags;  // bit vector - see details below
        private long cursorID;       // cursor id if client needs to do get more's
        private int startingFrom;   // where in the cursor this reply is starting
        private List<BSONObject> documents;      // documents

        public Builder setHeader(MessageHeader header) {
            this.header = header;
            return this;
        }

        public Builder setCursorID(long cursorID) {
            this.cursorID = cursorID;
            return this;
        }

        public Builder setDocuments(List<BSONObject> documents) {
            this.documents = documents;
            return this;
        }

        public Builder setResponseFlags(int responseFlags) {
            this.responseFlags = responseFlags;
            return this;
        }

        public Builder setStartingFrom(int startingFrom) {
            this.startingFrom = startingFrom;
            return this;
        }

        public MongoResponseImpl build() {
            return new MongoResponseImpl(this);
        }

        public long getCursorID() {
            return cursorID;
        }

        public List<BSONObject> getDocuments() {
            return documents;
        }

        public int getResponseFlags() {
            return responseFlags;
        }

        public int getStartingFrom() {
            return startingFrom;
        }

        public MessageHeader getHeader() {
            return header;
        }
    }

    @Override
    public IoBuffer write() {
        final int size = size();
        Validate.isTrue(size == this.header.getMessageLength());
        IoBuffer buffer = IoBuffer.allocate(size);
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.setAutoExpand(false);

        this.header.write(buffer);
        buffer.putInt(responseFlags);
        buffer.putLong(cursorID);
        buffer.putInt(startingFrom);
        buffer.putInt(numberReturned);
        if (numberReturned > 0) {
            buffer.put(this.encode(documents));
        }
        return buffer;
    }

    private int bodySize() {
        final int bitsPerByte = 8;
        final int docSize = documents == null ? 0 : this.encode(documents).length;
        return docSize + 3 * Integer.SIZE / bitsPerByte + 1 * Long.SIZE / bitsPerByte;
    }

    public int size() {
        int headerSize = this.header.headerSize();
        int bodySize = this.bodySize();
        log.trace("body: " + bodySize + " header: " + headerSize);
        return headerSize + bodySize;
    }

    public final static int RES_FLAG_CURSOR_NOT_FOUND = 1; //Set when getMore is called but the cursor id is not valid at the server. Returned with zero results.
    public final static int RES_FLAG_QUERY_FAILURE = 1 << 1; //Set when query failed. Results consist of one document containing an "$err" field describing the failure.
    public final static int RES_FLAG_SHARD_CONFIG_STALE = 1 << 2; //Drivers should ignore this. Only mongos will ever see this set, in which case, it needs to update config from the server.
    public final static int RES_FLAG_AWAIT_CAPABLE = 1 << 3; //Set when the server supports the AwaitData Query option. If it doesn't, a client should sleep a little between getMore's of a Tailable cursor. Mongod version 1.6 supports AwaitData and thus always sets AwaitCapable.
}
