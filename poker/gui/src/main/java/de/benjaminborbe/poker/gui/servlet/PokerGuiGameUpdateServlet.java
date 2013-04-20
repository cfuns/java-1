package de.benjaminborbe.poker.gui.servlet;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.gui.PokerGuiConstants;
import de.benjaminborbe.poker.gui.util.PokerGuiLinkFactory;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputHiddenWidget;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.ValidationExceptionWidget;
import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class PokerGuiGameUpdateServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Poker - Game - Update";

	private final PokerService pokerService;

	private final PokerGuiLinkFactory pokerGuiLinkFactory;

	private final AuthenticationService authenticationService;

	@Inject
	public PokerGuiGameUpdateServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final CacheService cacheService,
		final PokerService pokerService,
		final PokerGuiLinkFactory pokerGuiLinkFactory) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.pokerService = pokerService;
		this.pokerGuiLinkFactory = pokerGuiLinkFactory;
		this.authenticationService = authenticationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String id = request.getParameter(PokerGuiConstants.PARAMETER_GAME_ID);
			final String name = request.getParameter(PokerGuiConstants.PARAMETER_GAME_NAME);
			final String referer = request.getParameter(PokerGuiConstants.PARAMETER_REFERER);

			final PokerGameIdentifier pokerGameIdentifier = pokerService.createGameIdentifier(id);
			final PokerGame game = pokerService.getGame(pokerGameIdentifier);

			if (id != null && name != null) {
				try {
					updateGame(pokerGameIdentifier, name);

					if (referer != null) {
						throw new RedirectException(referer);
					} else {
						throw new RedirectException(pokerGuiLinkFactory.gameListUrl(request));
					}
				} catch (final ValidationException e) {
					widgets.add("update game failed!");
					widgets.add(new ValidationExceptionWidget(e));
				}
			}

			final FormWidget form = new FormWidget();
			form.addFormInputWidget(new FormInputHiddenWidget(PokerGuiConstants.PARAMETER_REFERER).addDefaultValue(buildRefererUrl(request)));
			form.addFormInputWidget(new FormInputHiddenWidget(PokerGuiConstants.PARAMETER_GAME_ID).addValue(game.getId()));
			form.addFormInputWidget(new FormInputTextWidget(PokerGuiConstants.PARAMETER_GAME_NAME).addLabel("Name:").addDefaultValue(game.getName()));
			form.addFormInputWidget(new FormInputSubmitWidget("update"));
			widgets.add(form);

			return widgets;
		} catch (final PokerServiceException e) {
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private void updateGame(final PokerGameIdentifier id, final String name) throws PokerServiceException, ValidationException {
		final PokerGameDto dto = new PokerGameDto();
		dto.setId(id);
		dto.setName(name);
		pokerService.updateGame(dto);
	}

	@Override
	protected void doCheckPermission(final HttpServletRequest request) throws ServletException, IOException,
		PermissionDeniedException, LoginRequiredException {
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			pokerService.expectPokerAdminPermission(sessionIdentifier);
		} catch (final AuthenticationServiceException | PokerServiceException e) {
			throw new PermissionDeniedException(e);
		}
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}
}
