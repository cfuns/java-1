package de.benjaminborbe.tools.validation;

import java.util.Collection;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationResult;

public class ValidationExecutor {

	private final ValidatorRegistry validatorRegistry;

	@Inject
	public ValidationExecutor(final ValidatorRegistry validatorRegistry) {
		this.validatorRegistry = validatorRegistry;
	}

	public ValidationResult validate(final Object object) {
		final ValidationResultImpl result = new ValidationResultImpl();
		final Class<? extends Object> clazz = object.getClass();
		final Collection<Validator<?>> validators = validatorRegistry.get(clazz);
		if (validators == null || validators.isEmpty()) {
			result.add(new ValidationErrorSimple("no validator found for class " + clazz.getName()));
		}
		else {
			for (final Validator<?> validator : validators) {
				result.addAll(validator.validateObject(object));
			}
		}
		return result;
	}

}
