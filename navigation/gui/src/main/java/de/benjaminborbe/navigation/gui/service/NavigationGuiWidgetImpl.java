package de.benjaminborbe.navigation.gui.service;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationService;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class NavigationGuiWidgetImpl implements NavigationWidget {

	private final class NavigationEntryComparator extends ComparatorBase<NavigationEntry, String> {

		@Override
		public String getValue(final NavigationEntry o) {
			return o.getTitle();
		}
	}

	private final Logger logger;

	private final NavigationService navigationService;

	private final UrlUtil urlUtil;

	private final AuthenticationService authenticationService;

	@Inject
	public NavigationGuiWidgetImpl(
		final Logger logger,
		final NavigationService navigationService,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService
	) {
		this.logger = logger;
		this.navigationService = navigationService;
		this.urlUtil = urlUtil;
		this.authenticationService = authenticationService;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("render");
		final ListWidget widgets = new ListWidget();
		final UlWidget ul = new UlWidget();
		ul.addId("navi");
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			for (final NavigationEntry navigationEntry : sort(navigationService.getNavigationEntries())) {
				if (navigationEntry.isVisible(sessionIdentifier)) {
					ul.add(new LinkWidget(urlUtil.buildUrl(request, navigationEntry.getURL()), navigationEntry.getTitle()));
				}
			}
		} catch (final AuthenticationServiceException e) {
			logger.warn(e.getClass().getName(), e);
		}
		widgets.add(ul);
		widgets.render(request, response, context);
	}

	protected List<NavigationEntry> sort(final Collection<NavigationEntry> all) {
		final List<NavigationEntry> result = new ArrayList<NavigationEntry>(all);
		Collections.sort(result, new NavigationEntryComparator());
		return result;
	}

}
