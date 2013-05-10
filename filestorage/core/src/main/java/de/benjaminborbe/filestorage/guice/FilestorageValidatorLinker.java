package de.benjaminborbe.filestorage.guice;

import de.benjaminborbe.filestorage.dao.FilestorageEntryValidator;
import de.benjaminborbe.lib.validation.ValidatorRegistry;

import javax.inject.Inject;

public class FilestorageValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final FilestorageEntryValidator filestorageEntryValidator) {
		validatorRegistry.register(filestorageEntryValidator);
	}
}
