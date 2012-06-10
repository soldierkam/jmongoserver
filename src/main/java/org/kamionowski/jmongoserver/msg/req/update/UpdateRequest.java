package org.kamionowski.jmongoserver.msg.req.update;

import org.apache.mina.core.buffer.IoBuffer;
import org.bson.BSONObject;
import org.kamionowski.jmongoserver.msg.MessageHeader;
import org.kamionowski.jmongoserver.msg.req.AbstractMongoCollectionRequest;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: soldier
 * Date: 07.06.12
 * Time: 17:22
 */
public class UpdateRequest extends AbstractMongoCollectionRequest {

    private int flags;              // bit vector. see below
    private BSONObject selector;           // the query to select the document
    private BSONObject update;             // specification of the update to perform

    public UpdateRequest(MessageHeader header, IoBuffer buffer) throws IOException {
        super(header, MessageHeader.Operation.OP_UPDATE, buffer);
    }

    @Override
    protected void readBody() throws IOException {
        super.readBody();
        flags = buffer.getInt();
        InputStream stream = buffer.asInputStream();
        selector = readObject(stream);
        update = readObject(stream);
    }

    public int getZero() {
        return super.getFlags();
    }

    public BSONObject getSelector() {
        return selector;
    }

    public BSONObject getUpdate() {
        return update;
    }

    @Override
    public int getFlags() {
        return this.flags;
    }

    public final static int UPSERT = 1 << 0;     //If set, the database will insert the supplied object into the collection if no matching document is found.
    public final static int MULITUPDATE = 1 << 1;     //If set, the database will update all matching objects in the collection. Otherwise only updates first matching doc.
}
