package de.benjaminborbe.poker.gui.servlet;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.lib.validation.ValidationResultImpl;
import de.benjaminborbe.poker.api.PokerPlayerDto;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PokerGuiPlayerCreator {

	private final ParseUtil parseUtil;

	private final PokerService pokerService;

	@Inject
	public PokerGuiPlayerCreator(final ParseUtil parseUtil, final PokerService pokerService) {
		this.parseUtil = parseUtil;
		this.pokerService = pokerService;
	}

	public void createPlayer(
		final SessionIdentifier sessionIdentifier,
		final String name,
		final String creditsString,
		final Collection<UserIdentifier> owners
	) throws PokerServiceException, ValidationException, LoginRequiredException, PermissionDeniedException {
		final List<ValidationError> errors = new ArrayList<ValidationError>();
		long credits = 0;
		try {
			credits = parseUtil.parseLong(creditsString);
		} catch (final ParseException e) {
			errors.add(new ValidationErrorSimple("illegal credits"));
		}
		if (!errors.isEmpty()) {
			throw new ValidationException(new ValidationResultImpl(errors));
		} else {
			final PokerPlayerDto playerDto = new PokerPlayerDto();
			playerDto.setName(name);
			playerDto.setAmount(credits);
			playerDto.setOwners(owners);
			pokerService.createPlayer(sessionIdentifier, playerDto);
		}
	}
}
