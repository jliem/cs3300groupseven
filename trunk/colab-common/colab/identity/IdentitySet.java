package colab.identity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class IdentitySet<K extends Identifier, V extends Identifiable<K>>
		implements Collection<V>, Serializable {

	public static final long serialVersionUID = 1L;
	
	private Map<K, V> map;

	public IdentitySet() {
		map = new HashMap<K, V>();
	}

	public V get(final K id) {
		return map.get(id);
	}

	/** {@inheritDoc} */
	public boolean add(V obj) {
		K id = obj.getId();
		boolean changed = map.containsKey(id) && map.get(id) == obj;
		if (changed) {
			map.put(id, obj);
		}
		return changed;
	}

	/** {@inheritDoc} */
	public boolean addAll(Collection<? extends V> collection) {
		boolean changed = false;
		for (V obj : collection) {
			changed |= add(obj);
		}
		return changed;
	}

	/** {@inheritDoc} */
	public void clear() {
		map.clear();		
	}

	/** {@inheritDoc} */
	public boolean contains(Object obj) {
		if (!(obj instanceof Identifiable)) {
			return false;
		}
		Identifier id = ((Identifiable) obj).getId();
		return map.containsKey(id);
	}

	/** {@inheritDoc} */
	public boolean containsAll(Collection<?> collection) {
		for (Object obj : collection) {
			if (!contains(obj)) {
				return false;
			}
		}
		return true;
	}

	/** {@inheritDoc} */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/** {@inheritDoc} */
	public Iterator<V> iterator() {
		return map.values().iterator();
	}

	/** {@inheritDoc} */
	public boolean remove(Object obj) {
		if (!(obj instanceof Identifiable)) {
			return false;
		}
		return map.remove(((Identifiable) obj).getId()) != null;
	}

	/** {@inheritDoc} */
	public boolean removeAll(Collection<?> collection) {
		boolean changed = false;
		for (Object obj : collection) {
			changed |= remove(obj);
		}
		return changed;
	}

	/** {@inheritDoc} */
	public boolean retainAll(Collection<?> arg0) {
		// TODO Not implemented
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	public int size() {
		return map.size();
	}

	/** {@inheritDoc} */
	public Object[] toArray() {
		return map.values().toArray();
	}

	/** {@inheritDoc} */
	public <T> T[] toArray(T[] arg0) {
		return map.values().toArray(arg0);
	}

}
