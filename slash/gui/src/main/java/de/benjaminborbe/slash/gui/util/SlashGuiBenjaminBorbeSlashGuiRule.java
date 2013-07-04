package de.benjaminborbe.slash.gui.util;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SlashGuiBenjaminBorbeSlashGuiRule implements SlashGuiRule {

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ExtHttpServiceMock extHttpServiceMock;

	@Inject
	public SlashGuiBenjaminBorbeSlashGuiRule(final Logger logger, AuthenticationService authenticationService, final ExtHttpServiceMock extHttpServiceMock) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.extHttpServiceMock = extHttpServiceMock;
	}

	@Override
	public Collection<SlashGuiRuleResult> getTarget(final HttpServletRequest request) {
		List<SlashGuiRuleResult> result = new ArrayList<>();
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (authenticationService.isLoggedIn(sessionIdentifier)) {
				if (extHttpServiceMock.hasServletPath("/search")) {
					result.add(new SlashGuiRuleResult(100, request.getContextPath() + "/search"));
				}
			}
		} catch (final AuthenticationServiceException e) {
			logger.warn(e.getClass().getName());
		}
		if (extHttpServiceMock.hasServletPath("/portfolio")) {
			result.add(new SlashGuiRuleResult(50, "/portfolio"));
		}
		return result;
	}
}
