package de.benjaminborbe.slash.gui.util;

import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SlashGuiPokerSlashGuiRule implements SlashGuiRule {

	private final ExtHttpServiceMock extHttpServiceMock;

	@Inject
	public SlashGuiPokerSlashGuiRule(final ExtHttpServiceMock extHttpServiceMock) {
		this.extHttpServiceMock = extHttpServiceMock;
	}

	@Override
	public Collection<SlashGuiRuleResult> getTarget(final HttpServletRequest request) {
		final List<SlashGuiRuleResult> result = new ArrayList<SlashGuiRuleResult>();
		if (extHttpServiceMock.hasServletPath("/poker")) {
			result.add(new SlashGuiRuleResult(50, request.getContextPath() + "/poker"));
		}
		return result;
	}
}
