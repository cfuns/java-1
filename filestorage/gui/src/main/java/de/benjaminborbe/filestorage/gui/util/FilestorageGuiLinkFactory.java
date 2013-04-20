package de.benjaminborbe.filestorage.gui.util;

import javax.inject.Inject;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.gui.FilestorageGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class FilestorageGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public FilestorageGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget downloadLink(final HttpServletRequest request, final FilestorageEntryIdentifier filestorageEntryIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + FilestorageGuiConstants.NAME + FilestorageGuiConstants.URL_DOWNLOAD, new MapParameter().add(FilestorageGuiConstants.PARAMETER_FILE_ID, filestorageEntryIdentifier.getId()), "download");
	}
}
