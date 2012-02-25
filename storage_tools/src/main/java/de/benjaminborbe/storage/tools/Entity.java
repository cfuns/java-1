package de.benjaminborbe.storage.tools;

import java.io.Serializable;

import de.benjaminborbe.api.Identifier;

public interface Entity<I extends Identifier<?>> extends Serializable {

	I getId();

	void setId(I id);
}
