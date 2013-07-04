package de.benjaminborbe.slash.gui.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface SlashGuiRule {

	Collection<SlashGuiRuleResult> getTarget(final HttpServletRequest request);
}
