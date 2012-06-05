package org.kamionowski.mongodbmock.db.data.query;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 23:18
 */
public class GreaterCondition extends AbstractComparableCondition {

    public GreaterCondition(Condition parent, Object value) {
        super(parent, value);
    }

    @Override
    protected boolean compare(Comparable left, Comparable right) {
        return left.compareTo(right) > 0;
    }

    @Override
    public String toString() {
        return toStringLine("+IF '" + fieldName() + "' > '" + value +"'");
    }
}
