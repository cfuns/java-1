package de.benjaminborbe.storage.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import javax.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.storage.gui.StorageGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class StorageGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public StorageGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget restore(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + StorageGuiConstants.NAME + StorageGuiConstants.URL_BACKUP, new MapParameter(), "restore");
	}

	public Widget backup(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + StorageGuiConstants.NAME + StorageGuiConstants.URL_RESTORE, new MapParameter(), "backup");
	}

}
