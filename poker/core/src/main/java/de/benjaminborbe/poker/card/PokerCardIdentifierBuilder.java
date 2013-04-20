package de.benjaminborbe.poker.card;

import org.slf4j.Logger;

import javax.inject.Inject;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.poker.api.PokerCardColor;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerCardValue;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class PokerCardIdentifierBuilder implements IdentifierBuilder<String, PokerCardIdentifier> {

	private final ParseUtil parseUtil;

	private final Logger logger;

	@Inject
	public PokerCardIdentifierBuilder(final Logger logger, final ParseUtil parseUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	public PokerCardIdentifier buildIdentifier(final String value) {
		final String[] parts = value.split(PokerCardIdentifier.SEPERATOR);
		if (parts.length == 2) {
			try {
				final PokerCardValue b = parseUtil.parseEnum(PokerCardValue.class, parts[1]);
				final PokerCardColor a = parseUtil.parseEnum(PokerCardColor.class, parts[0]);
				return new PokerCardIdentifier(a, b);
			}
			catch (final ParseException e) {
				logger.warn(e.getClass().getName(), e);
			}
		}
		else {
			logger.warn("parts-lenth != 2");
		}
		return null;
	}
}
