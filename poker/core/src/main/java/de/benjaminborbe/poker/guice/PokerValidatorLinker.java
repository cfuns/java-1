package de.benjaminborbe.poker.guice;

import de.benjaminborbe.poker.game.PokerGameValidator;
import de.benjaminborbe.poker.player.PokerPlayerValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

import javax.inject.Inject;

public class PokerValidatorLinker {

	@Inject
	public static void link(
		final ValidatorRegistry validatorRegistry,
		final PokerGameValidator pokerGameValidator,
		final PokerPlayerValidator pokerPlayerValidator
	) {
		validatorRegistry.register(pokerGameValidator);
		validatorRegistry.register(pokerPlayerValidator);
	}
}
