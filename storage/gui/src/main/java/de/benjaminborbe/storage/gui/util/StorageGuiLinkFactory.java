package de.benjaminborbe.storage.gui.util;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.storage.gui.StorageGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class StorageGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public StorageGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget restore(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + StorageGuiConstants.NAME + StorageGuiConstants.URL_RESTORE, new MapParameter(), "restore");
	}

	public Widget backup(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + StorageGuiConstants.NAME + StorageGuiConstants.URL_BACKUP, new MapParameter(), "backup");
	}

}
