package de.benjaminborbe.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UtilPasswordGeneratorImpl implements UtilPasswordGenerator {

	private final Logger logger;

	@Inject
	public UtilPasswordGeneratorImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String generatePassword(final int length, final UtilPasswordCharacter... characters) {
		logger.debug("generatePassword with lenght: " + length);
		final List<Character> cs = combine(characters);
		final StringBuffer result = new StringBuffer();
		final Random r = new Random();
		for (int i = 0; i < length; ++i) {
			result.append(cs.get(r.nextInt(cs.size())));
		}
		return result.toString();
	}

	protected List<Character> combine(final UtilPasswordCharacter... characters) {
		final List<Character> cs = new ArrayList<Character>();
		for (final UtilPasswordCharacter character : characters) {
			cs.addAll(toCharacter(character.getCharacters()));
		}
		return cs;
	}

	protected Collection<Character> toCharacter(final char... characters) {
		final Character[] cs = ArrayUtils.toObject(characters);
		return Arrays.asList(cs);
	}
}
