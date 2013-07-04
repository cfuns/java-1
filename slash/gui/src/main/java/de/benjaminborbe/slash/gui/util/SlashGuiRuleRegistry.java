package de.benjaminborbe.slash.gui.util;

import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SlashGuiRuleRegistry extends RegistryBase<SlashGuiRule> {

	@Inject
	public SlashGuiRuleRegistry(SlashGuiBenjaminBorbeSlashGuiRule bb, SlashGuiHarteslichSlashGuiRule harteslich, SlashGuiPokerSlashGuiRule poker) {
		super(bb, harteslich, poker);
	}
}
