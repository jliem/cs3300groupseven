package colab.common.identity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An IdentitySet is a set of Identifiable objects.  It is backed by
 * a hash map with Identifiers as keys, allowing easy retrieval from
 * the set using the identifier.
 *
 * @param <K> the type of identifier used for V
 * @param <V> the type of the contents of the set
 */
public class IdentitySet<K extends Identifier, V extends Identifiable<K>>
        implements Set<V>, Serializable {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    /** The backing map where all of the data is kept. */
    private Map<K, V> map;

    /**
     * Constructs an empty identity set.
     */
    public IdentitySet() {
        map = new HashMap<K, V>();
    }

    /**
     * Retrieves the value in the set identified by the given identifier.
     *
     * @param id the identifier of the desired object
     * @return the object with the given identifier, if it exists, or null
     */
    public final V get(final K id) {
        return map.get(id);
    }

    /** {@inheritDoc} */
    public final boolean add(final V obj) {
        K id = obj.getId();
        boolean changed = !(map.containsKey(id) && map.get(id) == obj);
        if (changed) {
            map.put(id, obj);
        }
        return changed;
    }

    /** {@inheritDoc} */
    public final boolean addAll(final Collection<? extends V> collection) {
        boolean changed = false;
        for (V obj : collection) {
            changed |= add(obj);
        }
        return changed;
    }

    /** {@inheritDoc} */
    public final void clear() {
        map.clear();
    }

    /** {@inheritDoc} */
    public final boolean contains(final Object obj) {
        if (!(obj instanceof Identifiable)) {
            return false;
        }
        Identifier id = ((Identifiable) obj).getId();
        return map.containsKey(id);
    }

    /** {@inheritDoc} */
    public final boolean containsAll(final Collection<?> collection) {
        for (Object obj : collection) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    public final boolean isEmpty() {
        return map.isEmpty();
    }

    /** {@inheritDoc} */
    public final Iterator<V> iterator() {
        return map.values().iterator();
    }

    /** {@inheritDoc} */
    public final boolean remove(final Object obj) {
        if (!(obj instanceof Identifiable)) {
            return false;
        }
        return map.remove(((Identifiable) obj).getId()) != null;
    }

    /**
     * Removes the element with the given identifier from the set.
     *
     * @param identifier the identifier of the element to remove
     * @return the element which was removed
     */
    public final V removeId(final K identifier) {
        return map.remove(identifier);
    }

    /** {@inheritDoc} */
    public final boolean removeAll(final Collection<?> collection) {
        boolean changed = false;
        for (Object obj : collection) {
            changed |= remove(obj);
        }
        return changed;
    }

    /** {@inheritDoc} */
    public final boolean retainAll(final Collection<?> arg0) {
        // TODO Not implemented
        throw new UnsupportedOperationException();
    }

    /** {@inheritDoc} */
    public final int size() {
        return map.size();
    }

    /** {@inheritDoc} */
    public final Object[] toArray() {
        return map.values().toArray();
    }

    /** {@inheritDoc} */
    public final <T> T[] toArray(final T[] arg0) {
        return map.values().toArray(arg0);
    }

}
