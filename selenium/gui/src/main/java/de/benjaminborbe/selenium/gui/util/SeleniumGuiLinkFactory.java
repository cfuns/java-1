package de.benjaminborbe.selenium.gui.util;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.gui.SeleniumGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class SeleniumGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public SeleniumGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget configurationRun(
		final HttpServletRequest request,
		final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + SeleniumGuiConstants.NAME + SeleniumGuiConstants.URL_CONFIGURATION_RUN, new MapParameter().add(
			SeleniumGuiConstants.PARAMETER_CONFIGURATION_ID, String.valueOf(seleniumConfigurationIdentifier)), "execute");
	}

	public Widget configurationList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + SeleniumGuiConstants.NAME + SeleniumGuiConstants.URL_CONFIGURATION_LIST, new MapParameter(), "list");
	}

	public Widget configurationXmlRun(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + SeleniumGuiConstants.NAME + SeleniumGuiConstants.URL_CONFIGURATION_XML_RUN, new MapParameter(), "execute xml");
	}

	public Widget configurationXmlUpload(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + SeleniumGuiConstants.NAME + SeleniumGuiConstants.URL_CONFIGURATION_XML_UPLOAD, new MapParameter(), "upload xml");
	}

	public Widget configurationXmlDelete(
		final HttpServletRequest request,
		final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + SeleniumGuiConstants.NAME + SeleniumGuiConstants.URL_CONFIGURATION_XML_DELETE, new MapParameter().add(
			SeleniumGuiConstants.PARAMETER_CONFIGURATION_ID, String.valueOf(seleniumConfigurationIdentifier)), "delete");
	}

	public Widget configurationXmlList(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + SeleniumGuiConstants.NAME + SeleniumGuiConstants.URL_CONFIGURATION_XML_LIST, new MapParameter(), "xml list");
	}
}
