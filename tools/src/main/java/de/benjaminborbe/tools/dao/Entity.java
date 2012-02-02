package de.benjaminborbe.tools.dao;

import java.io.Serializable;

public interface Entity<T> extends Serializable {

	T getId();

	void setId(final T id);
}
