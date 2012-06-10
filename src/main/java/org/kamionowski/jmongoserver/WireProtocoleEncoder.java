/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.kamionowski.jmongoserver.msg.res.MongoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author soldier
 */
public class WireProtocoleEncoder implements MessageEncoder<MongoResponse> {

    private final static Logger log = LoggerFactory.getLogger(WireProtocoleEncoder.class);

    @Override
    public void encode(IoSession session, MongoResponse res, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buffer = res.write();
        buffer.flip();
        out.write(buffer);
    }

}
