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
import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
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

	private final Logger logger;

	private final NavigationService navigationService;

	private final UrlUtil urlUtil;

	private final AuthenticationService authenticationService;

	private final ThreadRunner threadRunner;

	@Inject
	public NavigationGuiWidgetImpl(
		final Logger logger,
		final NavigationService navigationService,
		final UrlUtil urlUtil,
		final AuthenticationService authenticationService,
		final ThreadRunner threadRunner
	) {
		this.logger = logger;
		this.navigationService = navigationService;
		this.urlUtil = urlUtil;
		this.authenticationService = authenticationService;
		this.threadRunner = threadRunner;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("render");
		final ListWidget widgets = new ListWidget();
		final UlWidget ul = new UlWidget();
		ul.addId("navi");
		try {
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			for (final NavigationEntry navigationEntry : getNavigationEntries(sessionIdentifier)) {
				ul.add(new LinkWidget(urlUtil.buildUrl(request, navigationEntry.getURL()), navigationEntry.getTitle()));
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

	private Collection<NavigationEntry> getNavigationEntries(final SessionIdentifier sessionIdentifier) {

		final List<ThreadResult<NavigationEntry>> threadResults = new ArrayList<ThreadResult<NavigationEntry>>();
		final List<Thread> threads = new ArrayList<Thread>();
		for (final NavigationEntry navigationEntry : navigationService.getNavigationEntries()) {
			final ThreadResult<NavigationEntry> threadResult = new ThreadResult<NavigationEntry>();
			threadResults.add(threadResult);
			threads.add(threadRunner.run("navigationEntryIsVisible", new NavigationIsVisable(navigationEntry, sessionIdentifier, threadResult)));
		}

		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {

			}
		}

		final List<NavigationEntry> navigationEntries = new ArrayList<NavigationEntry>();
		for (final ThreadResult<NavigationEntry> threadResult : threadResults) {
			final NavigationEntry navigationEntry = threadResult.get();
			if (navigationEntry != null) {
				navigationEntries.add(navigationEntry);
			}
		}
		return sort(navigationEntries);
	}

	private static class NavigationIsVisable implements Runnable {

		private final NavigationEntry navigationEntry;

		private final SessionIdentifier sessionIdentifier;

		private final ThreadResult<NavigationEntry> threadResult;

		public NavigationIsVisable(
			final NavigationEntry navigationEntry,
			final SessionIdentifier sessionIdentifier,
			final ThreadResult<NavigationEntry> threadResult
		) {
			this.navigationEntry = navigationEntry;
			this.sessionIdentifier = sessionIdentifier;
			this.threadResult = threadResult;
		}

		@Override
		public void run() {
			if (navigationEntry.isVisible(sessionIdentifier)) {
				threadResult.set(navigationEntry);
			}
		}
	}

	private final class NavigationEntryComparator extends ComparatorBase<NavigationEntry, String> {

		@Override
		public String getValue(final NavigationEntry o) {
			return o.getTitle();
		}
	}
}
