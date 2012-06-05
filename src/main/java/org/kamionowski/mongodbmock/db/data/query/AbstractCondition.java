package org.kamionowski.mongodbmock.db.data.query;

import org.apache.commons.lang.StringUtils;

/**
 * User: soldier
 * Date: 28.05.12
 * Time: 22:46
 */
public abstract class AbstractCondition implements Condition {

    protected final Condition parentCondition;
    protected final QueryParser parser;
    private String spaces = null;

    protected AbstractCondition(Condition parent) {
        this.parentCondition = parent;
        parser = QueryParserImpl.getInstance();
    }

    protected final String toStringLine(String msg){
        if(spaces == null){
            spaces = StringUtils.repeat(" ", level() * 5);
        }
        return spaces + msg + '\n';
    }

    @Override
    public int level() {
        if(parentCondition == null){
            return 0;
        }
        return parentCondition.level() + 1;
    }
}
