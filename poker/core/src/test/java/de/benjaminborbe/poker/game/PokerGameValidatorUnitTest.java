package de.benjaminborbe.poker.game;

import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class PokerGameValidatorUnitTest {

	@Test
	public void testId() throws Exception {
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		PokerGameValidator pokerGameValidator = new PokerGameValidator(validationConstraintValidator);
		final PokerGameBean game = createValidGame();
		assertThat(pokerGameValidator.validate(game), is(notNullValue()));
		assertThat(pokerGameValidator.validate(game).size(), is(0));
		game.setId(null);
		assertThat(pokerGameValidator.validate(game).size(), is(1));
		game.setId(new PokerGameIdentifier(null));
		assertThat(pokerGameValidator.validate(game).size(), is(1));
		game.setId(new PokerGameIdentifier(""));
		assertThat(pokerGameValidator.validate(game).size(), is(1));
		game.setId(new PokerGameIdentifier("game123"));
		assertThat(pokerGameValidator.validate(game).size(), is(0));
	}

	@Test
	public void testName() throws Exception {
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		PokerGameValidator pokerGameValidator = new PokerGameValidator(validationConstraintValidator);
		final PokerGameBean game = createValidGame();
		assertThat(pokerGameValidator.validate(game), is(notNullValue()));
		assertThat(pokerGameValidator.validate(game).size(), is(0));
		game.setName(null);
		assertThat(pokerGameValidator.validate(game).size(), is(1));
		game.setName("");
		assertThat(pokerGameValidator.validate(game).size(), is(1));
		game.setName("GameA");
		assertThat(pokerGameValidator.validate(game).size(), is(0));
	}

	@Test
	public void testStartCredit() throws Exception {
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		PokerGameValidator pokerGameValidator = new PokerGameValidator(validationConstraintValidator);
		final PokerGameBean game = createValidGame();
		assertThat(pokerGameValidator.validate(game), is(notNullValue()));
		assertThat(pokerGameValidator.validate(game).size(), is(0));
		game.setStartCredits(null);
		assertThat(pokerGameValidator.validate(game).size(), is(1));
		game.setStartCredits(0l);
		assertThat(pokerGameValidator.validate(game).size(), is(1));
		game.setStartCredits(-1l);
		assertThat(pokerGameValidator.validate(game).size(), is(1));
		game.setStartCredits(1l);
		assertThat(pokerGameValidator.validate(game).size(), is(0));
	}

	private PokerGameBean createValidGame() {
		PokerGameBean pokerGameBean = new PokerGameBean();
		pokerGameBean.setId(new PokerGameIdentifier("123"));
		pokerGameBean.setName("gameA");
		pokerGameBean.setStartCredits(100000l);
		return pokerGameBean;
	}
}
