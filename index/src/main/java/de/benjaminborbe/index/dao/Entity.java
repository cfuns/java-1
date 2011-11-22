package de.benjaminborbe.index.dao;

import java.io.Serializable;

public interface Entity extends Serializable {

	Long getId();

	void setId(final Long id);
}
