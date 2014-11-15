package de.benjaminborbe.tools.fifo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Fifo<T> {

	private final LinkedList<T> data = new LinkedList<T>();

	public Fifo() {
	}

	public void add(final T value) {
		data.add(value);
	}

	public T get() throws FifoIndexOutOfBoundsException {
		return get(0);
	}

	public T get(final int index) throws FifoIndexOutOfBoundsException {
		synchronized (data) {
			if (index >= data.size() || index < 0)
				throw new FifoIndexOutOfBoundsException("no such element");
			return data.get(index);
		}
	}

	public int size() {
		return data.size();
	}

	public void remove() throws FifoIndexOutOfBoundsException {
		synchronized (data) {
			if (data.size() == 0)
				throw new FifoIndexOutOfBoundsException("can't remove element of empty fifo");
			data.removeFirst();
		}
	}

	public List<T> first(final int amount) throws FifoIndexOutOfBoundsException {
		if (amount > data.size() || amount < 0)
			throw new FifoIndexOutOfBoundsException("no such element");
		return data.subList(0, amount);
	}

	public List<T> last(final int amount) throws FifoIndexOutOfBoundsException {
		synchronized (data) {
			if (amount > data.size() || amount < 0)
				throw new FifoIndexOutOfBoundsException("no such element");
			final List<T> list = new ArrayList<T>(amount);
			for (final T element : data.subList(data.size() - amount, data.size())) {
				list.add(element);
			}
			Collections.reverse(list);
			return list;
		}
	}
}
