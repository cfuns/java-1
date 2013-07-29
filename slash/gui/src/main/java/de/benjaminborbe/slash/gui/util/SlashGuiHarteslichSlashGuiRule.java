package de.benjaminborbe.slash.gui.util;

import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SlashGuiHarteslichSlashGuiRule implements SlashGuiRule {

	private final ExtHttpServiceMock extHttpServiceMock;

	@Inject
	public SlashGuiHarteslichSlashGuiRule(final ExtHttpServiceMock extHttpServiceMock) {
		this.extHttpServiceMock = extHttpServiceMock;
	}

	@Override
	public Collection<SlashGuiRuleResult> getTarget(final HttpServletRequest request) {
		final List<SlashGuiRuleResult> result = new ArrayList<SlashGuiRuleResult>();
		final String serverName = request.getServerName();
		if (serverName.contains("harteslicht.de") || serverName.contains("harteslicht.com")) {
			if (extHttpServiceMock.hasServletPath("/blog")) {
				result.add(new SlashGuiRuleResult(50, request.getContextPath() + "/blog"));
			}
		}
		return result;
	}
}
