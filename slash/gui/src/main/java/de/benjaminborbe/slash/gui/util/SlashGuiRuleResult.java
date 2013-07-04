package de.benjaminborbe.slash.gui.util;

public class SlashGuiRuleResult {

	private int prio;

	private String target;

	public SlashGuiRuleResult(final int prio, final String target) {
		this.prio = prio;
		this.target = target;
	}

	public int getPrio() {
		return prio;
	}

	public void setPrio(final int prio) {
		this.prio = prio;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}
}
