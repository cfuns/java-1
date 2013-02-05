package de.benjaminborbe.distributed.index.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.index.gui.DistributedIndexGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class DistributedIndexGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public DistributedIndexGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget entryInfo(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + DistributedIndexGuiConstants.NAME + DistributedIndexGuiConstants.URL_ENTRY_INFO, "entry info");
	}

	public Widget entryInfo(final HttpServletRequest request, final String name, final String index, final String url) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + DistributedIndexGuiConstants.NAME + DistributedIndexGuiConstants.URL_ENTRY_INFO, new MapParameter().add(
				DistributedIndexGuiConstants.PARAMETER_INDEX, index).add(DistributedIndexGuiConstants.PARAMETER_URL, url), name);
	}

	public Widget wordInfo(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + DistributedIndexGuiConstants.NAME + DistributedIndexGuiConstants.URL_WORD_INFO, "word info");
	}

	public Widget wordInfo(final HttpServletRequest request, final String name, final String index, final String word) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + DistributedIndexGuiConstants.NAME + DistributedIndexGuiConstants.URL_WORD_INFO, new MapParameter().add(
				DistributedIndexGuiConstants.PARAMETER_INDEX, index).add(DistributedIndexGuiConstants.PARAMETER_WORD, word), name);
	}

	public Widget showPage(final HttpServletRequest request, final String index, final String url) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/distributed_search/page", new MapParameter().add("index", index).add("url", url), "show search page");
	}
}
