package de.benjaminborbe.storage.tools;

import java.io.Serializable;

public interface Entity<T> extends Serializable {

	T getId();

	void setId(final T id);
}
