package de.benjaminborbe.distributed.search.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.search.gui.DistributedSearchGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class DistributedSearchGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public DistributedSearchGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget rebuildIndex(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + DistributedSearchGuiConstants.NAME + DistributedSearchGuiConstants.URL_REBUILD_INDEX, new MapParameter(), "rebuild index");
	}

	public Widget page(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + DistributedSearchGuiConstants.NAME + DistributedSearchGuiConstants.URL_PAGE, new MapParameter(), "show page");
	}

	public Widget showIndex(final HttpServletRequest request, final String index, final String url) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/distributed_index/entry", new MapParameter().add("index", index).add("url", url), "show index entry");
	}

	public Widget rebuildPage(final HttpServletRequest request, final String index, final String url) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + DistributedSearchGuiConstants.NAME + DistributedSearchGuiConstants.URL_REBUILD_PAGE, new MapParameter()
				.add("index", index).add("url", url), "rebuild page");
	}

	public Widget rebuildAll(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + DistributedSearchGuiConstants.NAME + DistributedSearchGuiConstants.URL_REBUILD_ALL, new MapParameter(), "rebuild all")
				.addConfirm("rebuild all?");
	}

}
