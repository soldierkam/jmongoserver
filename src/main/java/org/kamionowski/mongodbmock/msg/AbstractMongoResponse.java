/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.mongodbmock.msg;

import org.bson.BSONObject;
import org.bson.BasicBSONEncoder;
import org.bson.io.BasicOutputBuffer;

import java.util.List;

/**
 * @author soldier
 */
public abstract class AbstractMongoResponse extends BasicBSONEncoder implements MongoResponse {

    protected MessageHeader header;

    public AbstractMongoResponse(MessageHeader header) {
        this.header = header;
    }


    public byte[] encode(List<BSONObject> documents) {
        BasicOutputBuffer buf = new BasicOutputBuffer();
        set(buf);
        for (BSONObject o : documents) {
            putObject(o);
        }
        done();
        return buf.toByteArray();
    }
}
