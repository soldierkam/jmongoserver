package org.kamionowski.mongodbmock.db.data.query;

/**
 * User: soldier
 * Date: 31.05.12
 * Time: 23:43
 */
public class LessCondition extends AbstractComparableCondition {

    public LessCondition(Condition parent, Object value) {
        super(parent, value);
    }

    @Override
    protected boolean compare(Comparable left, Comparable right) {
        return left.compareTo(right) < 0;
    }

    @Override
    public String toString() {
        return toStringLine("+IF '" + fieldName() + "' < '" + value +"'");
    }
}
