package de.benjaminborbe.slash.gui.util;

import de.benjaminborbe.tools.util.ComparatorBase;
import de.benjaminborbe.tools.util.ComparatorUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class SlashGuiRedirectDeterminerImpl implements SlashGuiRedirectDeterminer {

	private static final String DEFAULT_TARGET = "search";

	private final Logger logger;

	private final SlashGuiRuleRegistry slashGuiRuleRegistry;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public SlashGuiRedirectDeterminerImpl(
		final Logger logger,
		final SlashGuiRuleRegistry slashGuiRuleRegistry,
		final ComparatorUtil comparatorUtil
	) {
		this.logger = logger;
		this.slashGuiRuleRegistry = slashGuiRuleRegistry;
		this.comparatorUtil = comparatorUtil;
	}

	@Override
	public String getTarget(final HttpServletRequest request) {
		final List<SlashGuiRuleResult> slashGuiRuleResults = new ArrayList<SlashGuiRuleResult>();
		for (final SlashGuiRule slashGuiRule : slashGuiRuleRegistry.getAll()) {
			slashGuiRuleResults.addAll(slashGuiRule.getTarget(request));
		}
		logger.debug("found " + slashGuiRuleResults.size() + " posible targets");

		comparatorUtil.sort(slashGuiRuleResults, new ComparatorBase<SlashGuiRuleResult, Integer>() {

			public Integer getValue(final SlashGuiRuleResult o) {
				return o.getPrio();
			}

			@Override
			public boolean inverted() {
				return true;
			}
		});

		if (!slashGuiRuleResults.isEmpty()) {
			return slashGuiRuleResults.get(0).getTarget();
		}

		logger.debug("no target found, use default");
		return request.getContextPath() + "/" + DEFAULT_TARGET;
	}
}
