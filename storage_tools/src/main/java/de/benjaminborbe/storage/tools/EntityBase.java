package de.benjaminborbe.storage.tools;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import de.benjaminborbe.api.Identifier;

public class EntityBase<I extends Identifier<?>> implements Entity<I> {

	private static final long serialVersionUID = 1587927407230424165L;

	private I id;

	@Override
	public I getId() {
		return id;
	}

	@Override
	public void setId(final I id) {
		this.id = id;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (id == null)
			return false;
		if (!(other instanceof EntityBase))
			return false;
		if (!getClass().equals(other.getClass()))
			return false;

		@SuppressWarnings("unchecked")
		final EntityBase<I> rhs = (EntityBase<I>) other;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	@Override
	public int hashCode() {
		// you pick a hard-coded, randomly chosen, non-zero, odd number
		// ideally different for each class
		return new HashCodeBuilder(17, 37).append(id).toHashCode();
	}
}
