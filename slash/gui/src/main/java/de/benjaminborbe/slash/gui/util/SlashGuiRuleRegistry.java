package de.benjaminborbe.slash.gui.util;

import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SlashGuiRuleRegistry extends RegistryBase<SlashGuiRule> {

	@Inject
	public SlashGuiRuleRegistry(
		final SlashGuiBenjaminBorbeSlashGuiRule bb,
		final SlashGuiHarteslichSlashGuiRule harteslich,
		final SlashGuiPokerSlashGuiRule poker
	) {
		super(bb, harteslich, poker);
	}
}
