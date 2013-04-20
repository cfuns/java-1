package de.benjaminborbe.gallery.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.website.link.LinkRelativWidget;

@Singleton
public class GalleryGuiLinkFactory {

	private final UrlUtil urlUtil;

	@Inject
	public GalleryGuiLinkFactory(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	public Widget deleteGroup(final HttpServletRequest request, final GalleryGroupIdentifier galleryGroupIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GROUP_DELETE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_GROUP_ID, String.valueOf(galleryGroupIdentifier)), "delete").addConfirm("delete?");
	}

	public Widget createGroup(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GROUP_CREATE, "create group");
	}

	public Widget listGroups(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GROUP_LIST, "groups");
	}

	public Widget deleteCollection(final HttpServletRequest request, final GalleryCollectionIdentifier galleryCollectionIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_DELETE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollectionIdentifier)), "delete");
	}

	public Widget createCollection(final HttpServletRequest request, final GalleryGroupIdentifier galleryGroupIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_CREATE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_GROUP_ID, String.valueOf(galleryGroupIdentifier)), "create collection");
	}

	public Widget listCollections(final HttpServletRequest request, final GalleryGroup galleryGroup) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_LIST, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_GROUP_ID, String.valueOf(galleryGroup.getId())), galleryGroup.getName());
	}

	public Widget listCollections(final HttpServletRequest request, final GalleryGroupIdentifier galleryGroupIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_LIST, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_GROUP_ID, String.valueOf(galleryGroupIdentifier)), "collections");
	}

	public Widget deleteEntry(final HttpServletRequest request, final GalleryEntryIdentifier galleryEntryIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_DELETE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(galleryEntryIdentifier)), "delete").addConfirm("delete?");
	}

	public Widget createEntry(final HttpServletRequest request, final GalleryCollectionIdentifier galleryCollectionIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_CREATE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollectionIdentifier)), "create entry");
	}

	public Widget listEntries(final HttpServletRequest request, final GalleryCollection galleryCollection) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_LIST, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollection.getId())), galleryCollection.getName());
	}

	public String collectionListUrl(final HttpServletRequest request, final GalleryGroupIdentifier galleryGroupIdentifier) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_LIST,
				new MapParameter().add(GalleryGuiConstants.PARAMETER_GROUP_ID, String.valueOf(galleryGroupIdentifier)));
	}

	public String entryListUrl(final HttpServletRequest request, final GalleryCollectionIdentifier galleryCollectionIdentifier) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_LIST,
				new MapParameter().add(GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollectionIdentifier)));
	}

	public String createImage(final HttpServletRequest request, final GalleryImageIdentifier imageIdentifier) throws UnsupportedEncodingException {
		// return urlUtil.buildUrl(request.getContextPath() + "/" + GalleryGuiConstants.NAME +
		// GalleryGuiConstants.URL_IMAGE,
		// new MapParameter().add(GalleryGuiConstants.PARAMETER_IMAGE_ID,
		// String.valueOf(imageIdentifier)));
		return request.getContextPath() + "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_IMAGE + "/" + String.valueOf(imageIdentifier);
	}

	public Widget updateCollection(final HttpServletRequest request, final GalleryCollectionIdentifier galleryCollectionIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_UPDATE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollectionIdentifier)), "edit");
	}

	public Widget updateGroup(final HttpServletRequest request, final GalleryGroupIdentifier galleryGroupIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GROUP_UPDATE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_GROUP_ID, String.valueOf(galleryGroupIdentifier)), "edit");
	}

	public Widget updateEntry(final HttpServletRequest request, final GalleryEntryIdentifier galleryEntryIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_UPDATE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(galleryEntryIdentifier)), "edit");
	}

	public Widget swapEntryPrio(final HttpServletRequest request, final GalleryEntryIdentifier galleryEntryIdentifierA, final GalleryEntryIdentifier galleryEntryIdentifierB,
			final String name) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_SWAP_PRIO, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_ENTRY_ID_A, String.valueOf(galleryEntryIdentifierA)).add(GalleryGuiConstants.PARAMETER_ENTRY_ID_B, String.valueOf(galleryEntryIdentifierB)),
				name);
	}

	public Widget listEntries(final HttpServletRequest request, final GalleryCollectionIdentifier galleryCollectionIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_LIST, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollectionIdentifier)), "entries");
	}

	public Widget shareEntry(final HttpServletRequest request, final GalleryEntryIdentifier galleryEntryIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_SHARE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(galleryEntryIdentifier)), "shared");
	}

	public Widget unshareEntry(final HttpServletRequest request, final GalleryEntryIdentifier galleryEntryIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_UNSHARE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(galleryEntryIdentifier)), "unshare");
	}
}
