/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderAdapter;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.kamionowski.jmongoserver.msg.req.MongoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteOrder;

/**
 * @author soldier
 */
public class WireProtocoleDecoder extends MessageDecoderAdapter {

    private final static Logger log = LoggerFactory.getLogger(WireProtocoleDecoder.class);

    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        log.trace("Decodable " + session);
        in.order(ByteOrder.LITTLE_ENDIAN);
        RequestFactory factory = RequestFactoryImpl.getInstance();
        try {
            if (factory.isCompleteRequest(in)) {
                return OK;
            } else {
                return NEED_DATA;
            }
        } catch (IOException exc) {
            log.error("Cannot decode", exc);
            return NOT_OK;
        }
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        log.trace("Decode " + session);
        in.order(ByteOrder.LITTLE_ENDIAN);
        MongoRequest req = getRequest(in);
        out.write(req);
        return OK;
    }

    private MongoRequest getRequest(IoBuffer buffer) throws IOException {
        RequestFactory factory = RequestFactoryImpl.getInstance();
        return factory.build(buffer);
    }

}
