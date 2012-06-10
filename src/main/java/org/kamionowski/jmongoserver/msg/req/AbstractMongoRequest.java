/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.msg.req;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.buffer.IoBuffer;
import org.bson.BasicBSONDecoder;
import org.kamionowski.jmongoserver.msg.MessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author soldier
 */
public abstract class AbstractMongoRequest extends BasicBSONDecoder implements MongoRequest {

    private final static Logger log = LoggerFactory.getLogger(AbstractMongoRequest.class);

    protected MessageHeader header;
    private final MessageHeader.Operation operation;
    protected IoBuffer buffer;

    public AbstractMongoRequest(MessageHeader header, MessageHeader.Operation operation, IoBuffer buffer) throws IOException {
        this.header = header;
        this.operation = operation;
        this.buffer = buffer;
        Validate.isTrue(this.operation.equals(header.getOperation()));
    }

    public MessageHeader getHeader() {
        return header;
    }

    abstract protected void readBody() throws IOException;

    protected final String getCString() {
        ByteBuffer tmpBuffer = ByteBuffer.allocate(100);
        while (true) {
            byte b = buffer.get();
            if (b == 0) {
                break;
            }
            tmpBuffer.put(b);
        }
        log.debug("Decode string from " + tmpBuffer.position() + " bytes");
        try {
            return new String(tmpBuffer.array(), 0, tmpBuffer.position(), "ASCII");
        } catch (UnsupportedEncodingException exc) {
            throw new IllegalArgumentException(exc);
        }
    }

    @Override
    public int getId() {
        return this.getHeader().getRequestID();
    }

}
