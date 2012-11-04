package de.benjaminborbe.navigation.gui.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;

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

	@Inject
	public NavigationGuiWidgetImpl(final Logger logger, final NavigationService navigationService) {
		this.logger = logger;
		this.navigationService = navigationService;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		logger.trace("render");
		final ListWidget widgets = new ListWidget();
		final UlWidget ul = new UlWidget();
		ul.addAttribute("id", "navi");
		for (final NavigationEntry navigationEntry : sort(navigationService.getNavigationEntries())) {
			ul.add(new LinkWidget(buildUrl(request, navigationEntry.getURL()), navigationEntry.getTitle()));
		}
		widgets.add(ul);
		widgets.render(request, response, context);
	}

	protected List<NavigationEntry> sort(final Collection<NavigationEntry> all) {
		final List<NavigationEntry> result = new ArrayList<NavigationEntry>(all);
		Collections.sort(result, new NavigationEntryComparator());
		return result;
	}

	protected URL buildUrl(final HttpServletRequest request, final String url) throws MalformedURLException {
		if (url.indexOf("/") == 0) {
			return new URL(request.getScheme() + "://" + request.getServerName() + url);
		}
		else {
			return new URL(url);
		}
	}

}
