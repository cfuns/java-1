package de.benjaminborbe.portfolio.gui.util;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;

public class PortfolioGuiCacheUtil {

	@Inject
	public PortfolioGuiCacheUtil() {
		super();
	}

	public boolean isCacheable(final String contextPath, final String uri) {
		if (uri.indexOf(contextPath + "/" + PortfolioGuiConstants.NAME) != 0) {
			return false;
		}
		if (uri.indexOf(PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_IMAGE) != -1) {
			return true;
		}
		if (uri.indexOf(PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_GALLERY) != -1) {
			return true;
		}
		if (uri.indexOf(PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_LINKS) != -1) {
			return true;
		}
		if (uri.indexOf(PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT) != -1) {
			return true;
		}

		for (final String ext : getCacheAbleExtentions()) {
			if (uri.indexOf("." + ext) == (uri.length() - ext.length())) {
				return true;
			}
		}

		return false;
	}

	public List<String> getCacheAbleExtentions() {
		final List<String> exts = new ArrayList<String>();
		exts.add("css");
		exts.add("js");
		exts.add("gif");
		exts.add("png");
		exts.add("jpg");
		return exts;
	}
}
