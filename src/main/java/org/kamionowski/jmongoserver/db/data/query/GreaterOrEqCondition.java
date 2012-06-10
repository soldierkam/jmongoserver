package org.kamionowski.jmongoserver.db.data.query;

/**
 * User: soldier
 * Date: 31.05.12
 * Time: 23:57
 */
public class GreaterOrEqCondition extends AbstractComparableCondition {
    public GreaterOrEqCondition(Condition parent, Object value) {
        super(parent, value);
    }

    @Override
    protected boolean compare(Comparable left, Comparable right) {
        return left.compareTo(right) >= 0;
    }

    @Override
    public String toString() {
        return toStringLine("+IF '" + fieldName() + "' >= '" + value + "'");
    }
}
