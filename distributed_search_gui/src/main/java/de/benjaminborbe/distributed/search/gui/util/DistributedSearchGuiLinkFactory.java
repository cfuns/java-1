package de.benjaminborbe.distributed.search.gui.util;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.search.gui.DistributedSearchGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class DistributedSearchGuiLinkFactory {

	@Inject
	public DistributedSearchGuiLinkFactory() {
	}

	public Widget rebuildIndex(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + DistributedSearchGuiConstants.NAME + DistributedSearchGuiConstants.URL_REBUILD_INDEX, "rebuild index");
	}

}
