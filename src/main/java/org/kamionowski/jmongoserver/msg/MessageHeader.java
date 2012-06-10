/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kamionowski.jmongoserver.msg;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.buffer.IoBuffer;
import org.bson.BasicBSONDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author soldier
 */
public class MessageHeader extends BasicBSONDecoder {

    private final static Logger log = LoggerFactory.getLogger(MessageHeader.class);

    int messageLength; // total message size, including this
    int requestID;     // identifier for this message
    int responseTo;    // requestID from the original request
    //   (used in reponses from db)
    int opCode;        // request type - see table below
    boolean writeable = false;

    public enum Operation {
        OP_REPLY(1),    //Reply to a client request. responseTo is set
        OP_MSG(1000),   //generic msg command followed by a string
        OP_UPDATE(2001),    //update document
        OP_INSERT(2002),    //insert new document
        RESERVED(2003),     //formerly used for OP_GET_BY_OID
        OP_QUERY(2004),     //query a collection
        OP_GET_MORE(2005),  //Get more data from a query. See Cursors
        OP_DELETE(2006),    //Delete documents
        OP_KILL_CURSORS(2007);  //Tell database client is done with a cursor

        private int code;

        private Operation(int code) {
            this.code = code;
        }

        public static Operation getOperation(int code) {
            for (Operation o : values()) {
                if (o.code == code) {
                    return o;
                }
            }
            throw new IllegalArgumentException("Wrong code number: " + code);
        }
    }

    public MessageHeader(IoBuffer buffer) {
        messageLength = buffer.getInt();
        log.info("Read query: " + buffer.limit() + 4 + "/" + this.messageLength);
        requestID = buffer.getInt();
        responseTo = buffer.getInt();
        opCode = buffer.getInt();
        writeable = false;
    }

    public MessageHeader(int responseTo) {
        this.opCode = Operation.OP_REPLY.code;
        this.responseTo = responseTo;
        writeable = true;
    }

    public void setMessageLength(int messageLength) {
        Validate.isTrue(writeable);
        this.messageLength = messageLength;
    }

//    protected Input getStream(){
//        return this._in;
//    }

    public int getMessageLength() {
        return messageLength;
    }

    public int getOpCode() {
        return opCode;
    }

    public Operation getOperation() {
        return Operation.getOperation(opCode);
    }

    public int getRequestID() {
        return requestID;
    }

    public int getResponseTo() {
        return responseTo;
    }

    public int headerSize() {
        final int bitsPerByte = 8;
        return 4 * Integer.SIZE / bitsPerByte;
    }

    public void write(IoBuffer buffer) {
        buffer.putInt(this.messageLength);
        buffer.putInt(this.requestID);
        buffer.putInt(this.responseTo);
        buffer.putInt(this.opCode);
    }

    public int bodySize() {
        return this.messageLength - this.headerSize();
    }

    @Override
    public String toString() {
        return "MessageHeader{" + "messageLength=" + messageLength + ", requestID=" + requestID + ", responseTo=" + responseTo + ", opCode=" + opCode + '}';
    }


}
