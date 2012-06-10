package org.kamionowski.jmongoserver.db.data.query;

import org.bson.BSONObject;

/**
 * User: soldier
 * Date: 31.05.12
 * Time: 23:57
 */
public abstract class AbstractComparableCondition extends AbstractCondition {

    protected Object value;

    public AbstractComparableCondition(Condition parent, Object value) {
        super(parent);
        this.value = value;
    }

    @Override
    public boolean filter(Object document) {
        BSONObject doc = (BSONObject) document;
        Object value = doc.get(fieldName());
        if (value instanceof Comparable) {
            return compare((Comparable) value, (Comparable) this.value);
        } else {
            return false;
        }
    }

    protected abstract boolean compare(Comparable left, Comparable right);

    @Override
    public String fieldName() {
        return parentCondition.fieldName();
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }
}
