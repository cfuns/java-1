package de.benjaminborbe.gallery.gui.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
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

	public Widget deleteCollection(final HttpServletRequest request, final GalleryCollectionIdentifier galleryCollectionIdentifier) throws MalformedURLException,
			UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_DELETE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollectionIdentifier)), "delete");
	}

	public Widget createCollection(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_CREATE, "create collection");
	}

	public Widget listCollections(final HttpServletRequest request) throws MalformedURLException {
		return new LinkRelativWidget(request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_LIST, "collections");
	}

	public Widget deleteEntry(final HttpServletRequest request, final GalleryEntryIdentifier galleryEntryIdentifier) throws MalformedURLException, UnsupportedEncodingException {
		return new LinkRelativWidget(urlUtil, request, "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_DELETE, new MapParameter().add(
				GalleryGuiConstants.PARAMETER_ENTRY_ID, String.valueOf(galleryEntryIdentifier)), "delete");
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

	public String entryListUrl(final HttpServletRequest request, final GalleryCollectionIdentifier galleryCollectionIdentifier) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_LIST,
				new MapParameter().add(GalleryGuiConstants.PARAMETER_COLLECTION_ID, String.valueOf(galleryCollectionIdentifier)));
	}

	public String createImage(final HttpServletRequest request, final GalleryImageIdentifier imageIdentifier) throws UnsupportedEncodingException {
		return urlUtil.buildUrl(request.getContextPath() + "/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_IMAGE,
				new MapParameter().add(GalleryGuiConstants.PARAMETER_IMAGE_ID, String.valueOf(imageIdentifier)));
	}
}
