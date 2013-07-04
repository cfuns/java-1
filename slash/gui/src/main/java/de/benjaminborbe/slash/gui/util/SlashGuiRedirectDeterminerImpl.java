package de.benjaminborbe.slash.gui.util;

import de.benjaminborbe.authentication.api.AuthenticationService;
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

	private final AuthenticationService authenticationService;

	private final SlashGuiRuleRegistry slashGuiRuleRegistry;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public SlashGuiRedirectDeterminerImpl(
		final Logger logger,
		final AuthenticationService authenticationService,
		final SlashGuiRuleRegistry slashGuiRuleRegistry,
		final ComparatorUtil comparatorUtil
	) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.slashGuiRuleRegistry = slashGuiRuleRegistry;
		this.comparatorUtil = comparatorUtil;
	}

	@Override
	public String getTarget(final HttpServletRequest request) {
//		for (String servletPath : extHttpServiceMock.getServletPaths()) {
//			logger.debug("registered servlet: " + servletPath);
//		}
		List<SlashGuiRuleResult> slashGuiRuleResults = new ArrayList<>();
		for (SlashGuiRule slashGuiRule : slashGuiRuleRegistry.getAll()) {
			slashGuiRuleResults.addAll(slashGuiRule.getTarget(request));
		}

		List<SlashGuiRuleResult> rules = comparatorUtil.sort(slashGuiRuleResults, new ComparatorBase<SlashGuiRuleResult, Integer>() {

			public Integer getValue(final SlashGuiRuleResult o) {
				return o.getPrio();
			}

			@Override
			public boolean inverted() {
				return true;
			}
		});

		if (slashGuiRuleResults.isEmpty()) {
			return request.getContextPath() + "/" + DEFAULT_TARGET;
		} else {
			return slashGuiRuleResults.get(0).getTarget();
		}
	}
}
