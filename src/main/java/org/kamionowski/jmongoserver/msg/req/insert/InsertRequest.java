package org.kamionowski.jmongoserver.msg.req.insert;

import org.apache.mina.core.buffer.IoBuffer;
import org.bson.BSONObject;
import org.kamionowski.jmongoserver.RequestBuilder;
import org.kamionowski.jmongoserver.msg.MessageHeader;
import org.kamionowski.jmongoserver.msg.req.AbstractMongoCollectionRequest;
import org.kamionowski.jmongoserver.msg.req.MongoRequest;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * User: soldier
 * Date: 04.06.12
 * Time: 19:19
 */
public class InsertRequest extends AbstractMongoCollectionRequest {

    private List<BSONObject> documents;
    private final static MessageHeader.Operation OPERATION = MessageHeader.Operation.OP_INSERT;

    public InsertRequest(MessageHeader header, IoBuffer buffer) throws IOException {
        super(header, OPERATION, buffer);
        readBody();
    }

    @Override
    protected void readBody() throws IOException {
        super.readBody();
        documents = new LinkedList<>();
        while (this.buffer.position() < this.header.getMessageLength()) {
            documents.add(readObject(buffer.asInputStream()));
        }
    }

    public static class Builder implements RequestBuilder {
        @Override
        public MongoRequest build(MessageHeader header, IoBuffer buffer) throws IOException {
            return new InsertRequest(header, buffer);
        }

        @Override
        public MessageHeader.Operation supportedOperation() {
            return OPERATION;
        }
    }

    public List<BSONObject> getDocuments() {
        return documents;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("InsertRequest");
        sb.append("{documents=").append(documents);
        sb.append('}');
        return sb.toString();
    }

    /**
     * If set, the database will not stop processing a bulk insert if one fails (eg due to duplicate IDs). This makes bulk
     * insert behave similarly to a series of single inserts, except lastError will be set if any insert fails, not just
     * the last one. If multiple errors occur, only the most recent will be reported by getMessage. (new in 1.9.1)
     */
    public static final int FLAG_CONTINUE_ON_ERROR = 1 << 0;
}
