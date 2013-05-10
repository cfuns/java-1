package de.benjaminborbe.lib.validation;

import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationResult;

import javax.inject.Inject;
import java.util.Collection;

public class ValidationExecutorImpl implements ValidationExecutor {

	private final ValidatorRegistry validatorRegistry;

	@Inject
	public ValidationExecutorImpl(final ValidatorRegistry validatorRegistry) {
		this.validatorRegistry = validatorRegistry;
	}

	@Override
	public ValidationResult validate(final Object object) {
		final ValidationResultImpl result = new ValidationResultImpl();
		final Class<? extends Object> clazz = object.getClass();
		final Collection<Validator<?>> validators = validatorRegistry.get(clazz);
		if (validators == null || validators.isEmpty()) {
			result.add(new ValidationErrorSimple("no validator found for class " + clazz.getName()));
		} else {
			for (final Validator<?> validator : validators) {
				result.addAll(validator.validateObject(object));
			}
		}
		return result;
	}

	@Override
	public ValidationResult validate(final Object object, final Collection<String> fields) {
		return validate(object);
	}

}
