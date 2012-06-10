package org.kamionowski.jmongoserver.db.data.query;

import org.bson.BSONObject;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 23:34
 */
public class TypeCondition extends AbstractCondition {

    private static enum BsonType {
        DOUBLE(1),
        STRING(2),
        OBJECT(3),
        ARRAY(4),
        BINARY_DATA(5),
        OBJECT_ID(7),
        BOOLEAN(8),
        DATE(9),
        NULL(10),
        REGEXP(11),
        JS_CODE(13),
        SYMBOL(14),
        JS_CODE_WITH_SCOPE(15),
        INTEGER(16),
        TIMESTAMP(17),
        LONG(18),
        MIN(255),
        MAX(127);
        private final int v;

        private BsonType(int v) {
            this.v = v;
        }

        public int getV() {
            return v;
        }
    }


    public TypeCondition(Condition parent, BSONObject query) {
        super(parent);


    }

    @Override
    public String fieldName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean filter(Object document) {
        return false;

    }
}
