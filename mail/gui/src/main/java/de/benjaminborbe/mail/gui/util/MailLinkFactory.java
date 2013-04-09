package de.benjaminborbe.mail.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;

import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.mail.gui.MailGuiConstants;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

public class MailLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public MailLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget sendTestMail(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + MailGuiConstants.NAME + MailGuiConstants.URL_HOME, new MapParameter().add(MailGuiConstants.PARAMETER_TESTMAIL, "true"),
				"send testmail");
	}
}
