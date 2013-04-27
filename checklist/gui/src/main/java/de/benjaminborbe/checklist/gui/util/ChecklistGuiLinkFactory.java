package de.benjaminborbe.checklist.gui.util;

import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.checklist.gui.ChecklistGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;
import de.benjaminborbe.website.widget.ImageWidget;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public class ChecklistGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public ChecklistGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget entryCreate(
		final HttpServletRequest request,
		final ChecklistListIdentifier checklistListIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_ENTRY_CREATE, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_LIST_ID, String.valueOf(checklistListIdentifier)), "create entry");
	}

	public Widget entryDelete(
		final HttpServletRequest request,
		final ChecklistEntryIdentifier checklistEntryIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_ENTRY_DELETE, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(checklistEntryIdentifier)), "delete").addConfirm("delete?");
	}

	public Widget entryList(
		final HttpServletRequest request,
		final ChecklistListIdentifier checklistListIdentifier,
		final String name
	) throws MalformedURLException,
		UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_ENTRY_LIST, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_LIST_ID, String.valueOf(checklistListIdentifier)), name);
	}

	public String entryListUrl(final HttpServletRequest request, final ChecklistListIdentifier checklistListIdentifier) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_ENTRY_LIST,
			new MapParameter().add(ChecklistGuiConstants.PARAMETER_LIST_ID, String.valueOf(checklistListIdentifier)));
	}

	public Widget entryUpdate(
		final HttpServletRequest request,
		final ChecklistEntryIdentifier checklistEntryIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_ENTRY_UPDATE, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(checklistEntryIdentifier)), "edit");
	}

	public Widget listCreate(final HttpServletRequest request) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_LIST_CREATE, new MapParameter(), "create list");
	}

	public Widget listDelete(
		final HttpServletRequest request,
		final ChecklistListIdentifier checklistListIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_LIST_DELETE, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_LIST_ID, String.valueOf(checklistListIdentifier)), "delete").addConfirm("delete?");
	}

	public Widget listUpdate(
		final HttpServletRequest request,
		final ChecklistListIdentifier checklistListIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_LIST_UPDATE, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_LIST_ID, String.valueOf(checklistListIdentifier)), "edit");
	}

	public Widget entryComplete(
		final HttpServletRequest request,
		final ChecklistEntryIdentifier checklistEntryIdentifier
	) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_ENTRY_COMPLETE, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(checklistEntryIdentifier)), buildImage(request, "complete"));
	}

	public Widget entryUncomplete(final HttpServletRequest request, final ChecklistEntryIdentifier checklistEntryIdentifier) throws MalformedURLException,
		UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_ENTRY_UNCOMPLETE, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(checklistEntryIdentifier)), buildImage(request, "uncomplete"));
	}

	private Widget buildImage(final HttpServletRequest request, final String name) {
		return new ImageWidget(request.getContextPath() + "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_IMAGES + "/" + name + "-icon.png", 20, 20).addAlt(name)
			.addClass("icon");
	}

	public Widget listReset(final HttpServletRequest request, final ChecklistListIdentifier id) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + ChecklistGuiConstants.NAME + ChecklistGuiConstants.URL_LIST_RESET, new MapParameter().add(
			ChecklistGuiConstants.PARAMETER_LIST_ID, String.valueOf(id)), "reset").addConfirm("reset checklist?");
	}
}
