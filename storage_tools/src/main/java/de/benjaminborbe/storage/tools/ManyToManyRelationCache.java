package de.benjaminborbe.storage.tools;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

import de.benjaminborbe.api.Identifier;

public class ManyToManyRelationCache<A extends Identifier<?>, B extends Identifier<?>> implements ManyToManyRelation<A, B> {

	private final Map<String, Boolean> data = new HashMap<String, Boolean>();

	@Inject
	public ManyToManyRelationCache() {
		super();
	}

	@Override
	public void add(final A identifierA, final B identifierB) {
		data.put(identifierA + "-" + identifierB, Boolean.TRUE);
	}

	@Override
	public void remove(final A identifierA, final B identifierB) {
		data.remove(identifierA + "-" + identifierB);
	}

	@Override
	public boolean exists(final A identifierA, final B identifierB) {
		return data.containsKey(identifierA + "-" + identifierB) && Boolean.TRUE.equals(data.get(identifierA + "-" + identifierB));
	}

}
