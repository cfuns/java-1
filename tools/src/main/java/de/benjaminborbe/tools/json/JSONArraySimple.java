package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JSONArraySimple implements JSONArray {

	private final List<Object> values = new ArrayList<Object>();

	@Override
	public void writeJSONString(final Writer out) throws IOException {
		writeJSONString(values, out);
	}

	public static void writeJSONString(final List<Object> values, final Writer out) throws IOException {
		if (values == null) {
			out.append("null");
			return;
		}
		out.append('[');
		boolean first = true;
		for (final Object value : values) {
			if (first) {
				first = false;
			}
			else {
				out.append(',');
			}
			JSONValueSimple.writeJSONString(value, out);
		}
		out.append(']');
	}

	@Override
	public boolean add(final Object e) {
		return values.add(e);
	}

	@Override
	public void add(final int index, final Object element) {
		values.add(index, element);
	}

	@Override
	public boolean addAll(final Collection<? extends Object> c) {
		return values.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends Object> c) {
		return values.addAll(index, c);
	}

	@Override
	public void clear() {
		values.clear();
	}

	@Override
	public boolean contains(final Object o) {
		return values.contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return values.containsAll(c);
	}

	@Override
	public Object get(final int index) {
		return values.get(index);
	}

	@Override
	public int indexOf(final Object o) {
		return values.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Iterator<Object> iterator() {
		return values.iterator();
	}

	@Override
	public int lastIndexOf(final Object o) {
		return values.lastIndexOf(o);
	}

	@Override
	public ListIterator<Object> listIterator() {
		return values.listIterator();
	}

	@Override
	public ListIterator<Object> listIterator(final int index) {
		return values.listIterator(index);
	}

	@Override
	public boolean remove(final Object o) {
		return values.remove(o);
	}

	@Override
	public Object remove(final int index) {
		return values.remove(index);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return values.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return values.removeAll(c);
	}

	@Override
	public Object set(final int index, final Object element) {
		return values.set(index, element);
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public List<Object> subList(final int fromIndex, final int toIndex) {
		return values.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return values.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return values.toArray(a);
	}

}
