package org.kamionowski.jmongoserver.db.data;

import org.bson.BSONObject;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: soldier
 * Date: 04.06.12
 * Time: 22:02
 */
public class DocumentSet extends HashSet<BSONObject> {

    final boolean checkU;
    private final Set<ObjectId> ids = new HashSet<>();

    private DocumentSet(boolean checkU) {
        this.checkU = checkU;
    }

    public static DocumentSet unique() {
        return new DocumentSet(true);
    }

    public static DocumentSet nonUnique() {
        return new DocumentSet(false);
    }

    @Override
    public boolean add(BSONObject o) {
        if (checkU) {
            Object id = o.get("_id");
            if (id == null || !(id instanceof ObjectId)) {
                throw new IllegalArgumentException("Invalid id: id = " + id);
            }
            ObjectId objectId = (ObjectId) id;
            if (ids.contains(objectId)) {
                throw new IllegalArgumentException("Already exists: " + objectId);
            }
        }
        return super.add(o);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends BSONObject> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
}
