package de.benjaminborbe.slash.gui.util;

import de.benjaminborbe.tools.util.ComparatorBase;

public class SlashGuiRuleResultIntegerComparatorBase extends ComparatorBase<SlashGuiRuleResult, Integer> {

	public Integer getValue(final SlashGuiRuleResult o) {
		return o.getPrio();
	}

	@Override
	public boolean inverted() {
		return true;
	}
}
