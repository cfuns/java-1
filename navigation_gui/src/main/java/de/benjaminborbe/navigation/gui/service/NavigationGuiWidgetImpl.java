package de.benjaminborbe.navigation.gui.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationService;
import de.benjaminborbe.navigation.api.NavigationWidget;

@Singleton
public class NavigationGuiWidgetImpl implements NavigationWidget {

	private final class NavigationEntryComparator implements Comparator<NavigationEntry> {

		@Override
		public int compare(final NavigationEntry o1, final NavigationEntry o2) {
			return o1.getTitle().compareTo(o2.getTitle());
		}
	}

	private final Logger logger;

	private final NavigationService navigationService;

	@Inject
	public NavigationGuiWidgetImpl(final Logger logger, final NavigationService navigationService) {
		this.logger = logger;
		this.navigationService = navigationService;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.debug("render");
		final PrintWriter out = response.getWriter();
		out.println("<ul>");
		for (final NavigationEntry navigationEntry : sort(navigationService.getNavigationEntries())) {
			out.println("<li>");
			out.println("<a href=\"" + buildUrl(request, navigationEntry.getURL()).toExternalForm() + "\">" + navigationEntry.getTitle() + "</a>");
			out.println("</li>");
		}
		out.println("</ul>");
	}

	protected List<NavigationEntry> sort(final Collection<NavigationEntry> all) {
		final List<NavigationEntry> result = new ArrayList<NavigationEntry>(all);
		Collections.sort(result, new NavigationEntryComparator());
		return result;
	}

	protected URL buildUrl(final HttpServletRequest request, final String url) throws MalformedURLException {
		if (url != null && url.indexOf("/") == 0) {
			return new URL("http://bb" + url);
		}
		else {
			return new URL(url);
		}
	}

}
