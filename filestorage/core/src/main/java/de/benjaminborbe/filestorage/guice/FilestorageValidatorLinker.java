package de.benjaminborbe.filestorage.guice;

import javax.inject.Inject;
import de.benjaminborbe.filestorage.dao.FilestorageEntryValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class FilestorageValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final FilestorageEntryValidator filestorageEntryValidator) {
		validatorRegistry.register(filestorageEntryValidator);
	}
}
