package org.kamionowski.mongodbmock.msg;

import org.apache.commons.lang.Validate;
import org.apache.mina.core.buffer.IoBuffer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: soldier
 * Date: 04.06.12
 * Time: 19:27
 */
public class AbstractMongoCollectionRequest extends AbstractMongoRequest{

    protected int flags;
    protected String fullCollectionName;

    public AbstractMongoCollectionRequest(MessageHeader header, MessageHeader.Operation operation, IoBuffer buffer) throws IOException {
        super(header, operation, buffer);
    }

    @Override
    protected void readBody() throws IOException {
        flags = buffer.getInt();
        fullCollectionName = getCString();

    }

    public String getFullCollectionName() {
        return fullCollectionName;
    }

    private static Pattern collectionPattern = Pattern.compile("([^.]+).(.*)");

    public String getDatabaseName(){
        Matcher m = collectionPattern.matcher(fullCollectionName);
        Validate.isTrue(m.matches());
        return m.group(1);
    }

    public String getCollectionName(){
        Matcher m = collectionPattern.matcher(fullCollectionName);
        Validate.isTrue(m.matches());
        return m.group(2);
    }



    public int getFlags() {
        return flags;
    }
}
