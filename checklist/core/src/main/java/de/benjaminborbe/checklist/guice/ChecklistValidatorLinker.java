package de.benjaminborbe.checklist.guice;

import de.benjaminborbe.checklist.dao.ChecklistEntryValidator;
import de.benjaminborbe.checklist.dao.ChecklistListValidator;
import de.benjaminborbe.lib.validation.ValidatorRegistry;

import javax.inject.Inject;

public class ChecklistValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final ChecklistEntryValidator a, final ChecklistListValidator b) {
		validatorRegistry.register(a);
		validatorRegistry.register(b);
	}
}
