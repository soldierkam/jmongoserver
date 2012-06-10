package org.kamionowski.jmongoserver.db.data.query;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

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
        if (value instanceof BSONObject) {
            this.value = null;
            this.innerCondition = this.parser.parse(this, (BSONObject) value);
        } else {
            this.value = value;
            this.innerCondition = null;
        }
    }

    @Override
    public boolean filter(Object document) {
        //TODO: implement inner condition
        if (document instanceof BasicBSONObject) {
            if (innerCondition != null) {
                return innerCondition.filter(document);
            } else {
                Object valueInDoc = ((BSONObject) document).get(fieldName);
                return ObjectUtils.equals(valueInDoc, value);
            }
        } else {
            return false;
        }

    }

    @Override
    public String fieldName() {
        return fieldName;
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
