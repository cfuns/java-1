package de.benjaminborbe.shortener.gui.util;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;
import de.benjaminborbe.shortener.gui.ShortenerGuiConstants;
import de.benjaminborbe.tools.url.UrlUtil;

public class ShortenerGuiLinkFactory {

	private final UrlUtil urlUil;

	@Inject
	public ShortenerGuiLinkFactory(final UrlUtil urlUil) {
		this.urlUil = urlUil;
	}

	public String getRedirectUrl(final HttpServletRequest request, final ShortenerUrlIdentifier shortenerUrlIdentifier) {
		return urlUil.buildBaseUrl(request) + ShortenerGuiConstants.URL_REDIRECT + "/" + String.valueOf(shortenerUrlIdentifier);
	}
}
