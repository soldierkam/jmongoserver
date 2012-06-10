package org.kamionowski.jmongoserver.db.data.query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.BSONObject;
import org.bson.types.BasicBSONList;
import org.bson.types.Code;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * User: soldier
 * Date: 06.06.12
 * Time: 17:11
 */
public class WhereCondition extends AbstractCondition {

    private final static Log LOG = LogFactory.getLog(ValueCondition.class);

    private final Code value;

    public WhereCondition(Condition parent, Object value) {
        super(parent);
        if (value instanceof Code) {
            this.value = (Code) value;
        } else {
            throw new UnsupportedOperationException("TODO: ma to sens?");
        }
    }

    @Override
    public boolean filter(Object document) {
        return test((Code) value, (BSONObject) document);
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
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public String toString() {
        return toStringLine("+IF '" + value.getCode().replace("\n", " ") + "'");
    }

    private static class BsonObjectJs implements Scriptable {

        private final Object document;

        private BsonObjectJs(Object document) {
            this.document = document;
        }

        @Override
        public String getClassName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(String name, Scriptable start) {
            if (document instanceof BSONObject) {
                Object value = ((BSONObject) document).get(name);
                if (value instanceof BSONObject) {
                    return new BsonObjectJs((BSONObject) value);
                }
                return value;
            }
            return null;
        }

        @Override
        public Object get(int index, Scriptable start) {
            if (document instanceof BasicBSONList) {
                BasicBSONList array = (BasicBSONList) document;
                if (index >= array.size()) {
                    return null;
                }
                return array.get(index);
            } else {
                //TODO: how works array operator for object?
                throw new UnsupportedOperationException();
            }
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
