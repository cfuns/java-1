package de.benjaminborbe.note.guice;

import com.google.inject.Inject;

import de.benjaminborbe.note.dao.NoteValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class NoteValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final NoteValidator noteValidator) {
		validatorRegistry.register(noteValidator);
	}
}
