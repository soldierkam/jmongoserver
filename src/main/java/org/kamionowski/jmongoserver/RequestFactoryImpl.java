/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver;

import org.apache.mina.core.buffer.IoBuffer;
import org.kamionowski.jmongoserver.msg.MessageHeader;
import org.kamionowski.jmongoserver.msg.req.MongoRequest;
import org.kamionowski.jmongoserver.msg.req.insert.InsertRequest;
import org.kamionowski.jmongoserver.msg.req.query.QueryRequest;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author soldier
 */
public class RequestFactoryImpl implements RequestFactory {

    private Map<MessageHeader.Operation, RequestBuilder> builders = new EnumMap<MessageHeader.Operation, RequestBuilder>(MessageHeader.Operation.class);

    @Override
    public MongoRequest build(IoBuffer buffer) throws IOException {
        MessageHeader header = new MessageHeader(buffer);
        RequestBuilder builder = builders.get(header.getOperation());
        if (builder == null) {
            throw new IllegalStateException("Cannot find builder for operation " + header.getOperation());
        }
        return builder.build(header, buffer);
    }

    @Override
    public boolean isCompleteRequest(IoBuffer buffer) throws IOException {
        int bytesInBuffer = buffer.remaining();
        int requestSize = buffer.getInt();
        buffer.position(0);
        return bytesInBuffer >= requestSize;
    }

    private RequestFactoryImpl() {
        registerBuilder(new QueryRequest.Builder());
        registerBuilder(new InsertRequest.Builder());
    }

    private static final RequestFactoryImpl instance = new RequestFactoryImpl();

    public static RequestFactoryImpl getInstance() {
        return instance;
    }

    private void registerBuilder(RequestBuilder builder) {
        RequestBuilder current = builders.get(builder.supportedOperation());
        if (current != null) {
            throw new IllegalStateException("Builder for " + builder.supportedOperation() + " already registered (" + current.getClass() + ")s");
        }
        builders.put(builder.supportedOperation(), builder);
    }
}
