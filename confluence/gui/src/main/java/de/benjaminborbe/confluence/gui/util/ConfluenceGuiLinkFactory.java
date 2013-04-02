package de.benjaminborbe.confluence.gui.util;

import com.google.inject.Inject;
import de.benjaminborbe.confluence.api.ConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.gui.ConfluenceGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class ConfluenceGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public ConfluenceGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget createInstance(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ConfluenceGuiConstants.NAME + ConfluenceGuiConstants.URL_INSTANCE_CREATE, "create instance");
	}

	public Widget deleteInstance(final HttpServletRequest request, final ConfluenceInstanceIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ConfluenceGuiConstants.NAME + ConfluenceGuiConstants.URL_INSTANCE_DELETE, new MapParameter().add(
			ConfluenceGuiConstants.PARAMETER_INSTANCE_ID, String.valueOf(id)), "delete").addConfirm("delete?");
	}

	public String instanceListUrl(final HttpServletRequest request) {
		return request.getContextPath() + "/" + ConfluenceGuiConstants.NAME + ConfluenceGuiConstants.URL_INSTANCE_LIST;
	}

	public Widget updateInstance(final HttpServletRequest request, final ConfluenceInstanceIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ConfluenceGuiConstants.NAME + ConfluenceGuiConstants.URL_INSTANCE_UPDATE, new MapParameter().add(
			ConfluenceGuiConstants.PARAMETER_INSTANCE_ID, String.valueOf(id)), "edit");
	}

	public Widget expireAll(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ConfluenceGuiConstants.NAME + ConfluenceGuiConstants.URL_EXPIRE_ALL, "expire all").addConfirm("expire all?");
	}

	public Widget refreshIndex(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + ConfluenceGuiConstants.NAME + ConfluenceGuiConstants.URL_INDEX_REFRESH, "refresh index");
	}

}
