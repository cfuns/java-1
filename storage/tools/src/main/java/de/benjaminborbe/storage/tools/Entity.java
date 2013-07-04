package de.benjaminborbe.storage.tools;

import de.benjaminborbe.api.Identifier;

import java.io.Serializable;

public interface Entity<I extends Identifier<?>> extends Serializable {

	I getId();

	void setId(I id);
}
