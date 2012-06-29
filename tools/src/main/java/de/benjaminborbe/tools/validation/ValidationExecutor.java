package de.benjaminborbe.tools.validation;

import java.util.Collection;

import com.google.inject.Inject;

public class ValidationExecutor {

	private final ValidatorRegistry validatorRegistry;

	@Inject
	public ValidationExecutor(final ValidatorRegistry validatorRegistry) {
		this.validatorRegistry = validatorRegistry;
	}

	public ValidationResult validate(final Object object) {
		final ValidationResult result = new ValidationResult();
		final Collection<Validator<?>> validators = validatorRegistry.get(object.getClass());
		for (final Validator<?> validator : validators) {
			result.addAll(validator.validate(object));
		}
		return result;
	}

}
