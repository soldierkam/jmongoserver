package org.kamionowski.mongodbmock.db.data.query;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.Code;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

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
            if (value instanceof Code) {
                return test((Code) value, (BSONObject) document);
            }else if(innerCondition != null){
                return innerCondition.filter(document);
            } else {
                Object valueInDoc = ((BSONObject) document).get(fieldName);
                return ObjectUtils.equals(valueInDoc, value);
            }
        } else {
            return false;
        }

    }

    private boolean test(Code function, BSONObject document) {
        // Creates and enters a Context. The Context stores information
        // about the execution environment of a script.
        Context cx = Context.enter();
        try {
            // Initialize the standard objects (Object, Function, etc.)
            // This must be done before scripts can be executed. Returns
            // a globalScope object that we use in later calls.
            Scriptable globalScope = cx.initStandardObjects();
            ScriptableObject.putProperty(globalScope, "document", new BsonObjectJs(document));

            Scriptable jsObject = cx.newObject(globalScope);
            Function functionJs = cx.compileFunction(jsObject, function.getCode(), function.toString(), 1, null);
            ScriptableObject.putProperty(jsObject, "_method", functionJs);
            ScriptableObject.putProperty(globalScope, "_obj", jsObject);
            String callMethodJs = "_obj._method.call(document)";

            // Now evaluate the string we've colected.
            Object result = cx.evaluateString(jsObject, callMethodJs, "<cmd>", 1, null);

            // Convert the result to a string and print it.
            return Context.toBoolean(result);
        } finally {
            // Exit from the context.
            Context.exit();
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

    private static class BsonObjectJs implements Scriptable {

        private final BSONObject document;

        private BsonObjectJs(BSONObject document) {
            this.document = document;
        }

        @Override
        public String getClassName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(String name, Scriptable start) {
            Object value = document.get(name);
            if(value instanceof BSONObject){
                return new BsonObjectJs((BSONObject)value);
            }
            return value;
        }

        @Override
        public Object get(int index, Scriptable start) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean has(String name, Scriptable start) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean has(int index, Scriptable start) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void put(String name, Scriptable start, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void put(int index, Scriptable start, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void delete(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void delete(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Scriptable getPrototype() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPrototype(Scriptable prototype) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Scriptable getParentScope() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setParentScope(Scriptable parent) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object[] getIds() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getDefaultValue(Class<?> hint) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasInstance(Scriptable instance) {
            throw new UnsupportedOperationException();
        }
    }
}
