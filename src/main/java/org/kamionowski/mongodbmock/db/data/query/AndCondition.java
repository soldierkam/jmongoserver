package org.kamionowski.mongodbmock.db.data.query;

import org.bson.BSONObject;

import java.util.List;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 23:15
 */
public class AndCondition extends AbstractCondition {

    private final List<Condition> conditions;

    public AndCondition(Condition parent, BSONObject query) {
        super(parent);
        this.conditions = parser.parseList(this, query);
    }

    @Override
    public boolean filter(Object document) {
        for(Condition condition : conditions){
            if(!condition.filter(document)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String fieldName() {
        return parentCondition.fieldName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toStringLine("AND {"));
        boolean first = true;
        for(Condition condition : conditions){
            sb.append(condition.toString());
        }
        sb.append(toStringLine("}"));
        return sb.toString();
    }
}
