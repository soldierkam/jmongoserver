package org.kamionowski.mongodbmock.db.data.query;

import org.bson.BSONObject;
import org.bson.types.BasicBSONList;

import java.util.List;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 22:20
 */
public class OrCondition extends AbstractCondition {

    private final List<Condition> conditions;

    public OrCondition(Condition parent, BasicBSONList query) {
        super(parent);
        conditions = parser.parseList(parent, query);
    }

    @Override
    public boolean filter(Object document) {
        for(Condition condition : conditions){
            if(condition.filter(document)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String fieldName() {
        return parentCondition.fieldName();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toStringLine("OR {"));
        boolean first = true;
        for(Condition condition : conditions){
            sb.append(condition.toString());
        }
        sb.append(toStringLine("}"));
        return sb.toString();
    }
}
