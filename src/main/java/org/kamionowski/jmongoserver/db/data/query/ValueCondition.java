package org.kamionowski.jmongoserver.db.data.query;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import java.util.Set;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 21:04
 */
public class ValueCondition extends AbstractCondition {

    private final static Log LOG = LogFactory.getLog(ValueCondition.class);

    private final String fieldName;
    private final Object value;
    private final Condition innerCondition;

    public ValueCondition(Condition parent, String fieldName, Object value) {
        super(parent);
        this.fieldName = fieldName;
        if (value instanceof BSONObject && containsInnerCondition((BSONObject) value)) {
            this.value = null;
            this.innerCondition = this.parser.parse(this, (BSONObject) value);
        } else {
            this.value = value;
            this.innerCondition = null;
        }
    }

    private boolean containsInnerCondition(BSONObject object) {
        Set<String> keys = object.keySet();
        for (String key : keys) {
            if (key.startsWith("$")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean filter(Object document) {
        if (document instanceof BasicBSONObject) {
            if (innerCondition != null) {
                return innerCondition.filter(document);
            } else {
                Value valueInDoc = fetchValueInDoc((BSONObject) document);
                if (valueInDoc.isExists()) {
                    return ObjectUtils.equals(this.value, valueInDoc.get());
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

    }

    @Override
    public String fieldName() {
        return fieldName;
    }

    private Value fetchValueInDoc(BSONObject document) {
        String[] pathElems = fieldName.split("\\.");
        Value value = new Value(document);
        for (String elem : pathElems) {
            Object o = value.get();
            if (!(o instanceof BSONObject)) {
                value = Value.NOT_EXISTS;
            }
            BSONObject bson = (BSONObject) o;
            if (bson.containsField(elem)) {
                value = new Value(bson.get(elem));
            } else {
                value = Value.NOT_EXISTS;
            }
        }
        return value;
    }

    @Override
    public String toString() {
        if (this.value == null) {
            StringBuilder sb = new StringBuilder(toStringLine("+IF '" + fieldName + "':"));
            sb.append(this.innerCondition.toString());
            return sb.toString();
        }
        return toStringLine("+IF '" + fieldName + "'=='" + value + "'");
    }
}
