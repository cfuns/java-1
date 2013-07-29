package de.benjaminborbe.lib.validation;

import de.benjaminborbe.api.ValidationError;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class ValidatorBase<T> implements Validator<T> {

	@SuppressWarnings("unchecked")
	@Override
	public final Collection<ValidationError> validateObject(final Object object) {
		return validate((T) object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Collection<ValidationError> validateObject(final Object object, final Collection<String> fields) {
		return validate((T) object, fields);
	}

	@Override
	public final Collection<ValidationError> validate(final T object) {
		final Set<ValidationError> result = new HashSet<ValidationError>();
		for (final ValidatorRule<T> value : buildRules().values()) {
			result.addAll(value.validate(object));
		}
		return result;
	}

	@Override
	public final Collection<ValidationError> validate(final T object, final Collection<String> fields) {
		final Set<ValidationError> result = new HashSet<ValidationError>();
		for (final Entry<String, ValidatorRule<T>> e : buildRules().entrySet()) {
			if (fields.contains(e.getKey())) {
				result.addAll(e.getValue().validate(object));
			}
		}
		return result;
	}

	protected abstract Map<String, ValidatorRule<T>> buildRules();

}
