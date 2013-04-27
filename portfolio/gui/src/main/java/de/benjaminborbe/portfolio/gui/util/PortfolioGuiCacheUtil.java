package de.benjaminborbe.portfolio.gui.util;

import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PortfolioGuiCacheUtil {

	@Inject
	public PortfolioGuiCacheUtil() {
		super();
	}

	public boolean isCacheable(final String contextPath, final String uri) {
		if (uri.indexOf(contextPath + "/" + PortfolioGuiConstants.NAME) != 0) {
			return false;
		}
		if (uri.contains(PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_IMAGE)) {
			return true;
		}
		if (uri.contains(PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_GALLERY)) {
			return true;
		}
		if (uri.contains(PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_LINKS)) {
			return true;
		}
		if (uri.contains(PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT)) {
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
		final List<String> exts = new ArrayList<>();
		exts.add("css");
		exts.add("js");
		exts.add("gif");
		exts.add("png");
		exts.add("jpg");
		return exts;
	}
}
