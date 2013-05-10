package de.benjaminborbe.note.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.note.dao.NoteValidator;

import javax.inject.Inject;

public class NoteValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final NoteValidator noteValidator) {
		validatorRegistry.register(noteValidator);
	}
}
