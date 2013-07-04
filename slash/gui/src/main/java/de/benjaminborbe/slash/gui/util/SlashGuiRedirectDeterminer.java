package de.benjaminborbe.slash.gui.util;

import javax.servlet.http.HttpServletRequest;

public interface SlashGuiRedirectDeterminer {

	String getTarget(HttpServletRequest httpServletRequest);
}
