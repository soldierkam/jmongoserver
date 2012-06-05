package org.kamionowski.mongodbmock;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.kamionowski.mongodbmock.msg.MongoResponse;

/**
 * User: soldier
 * Date: 03.06.12
 * Time: 22:22
 */
public class WireProtocolCodecFactory extends DemuxingProtocolCodecFactory{
    public WireProtocolCodecFactory() {
        addMessageDecoder(WireProtocoleDecoder.class);
        addMessageEncoder(MongoResponse.class, WireProtocoleEncoder.class);
    }
}
