package de.benjaminborbe.checklist.guice;

import com.google.inject.Inject;

import de.benjaminborbe.checklist.dao.ChecklistEntryValidator;
import de.benjaminborbe.checklist.dao.ChecklistListValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class ChecklistValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final ChecklistEntryValidator a, final ChecklistListValidator b) {
		validatorRegistry.register(a);
		validatorRegistry.register(b);
	}
}
