package de.benjaminborbe.filestorage.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilestorageEntryValidator extends ValidatorBase<FilestorageEntryBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public FilestorageEntryValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<FilestorageEntryBean> getType() {
		return FilestorageEntryBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<FilestorageEntryBean>> buildRules() {
		final Map<String, ValidatorRule<FilestorageEntryBean>> result = new HashMap<>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<FilestorageEntryBean>() {

				@Override
				public Collection<ValidationError> validate(final FilestorageEntryBean bean) {
					final FilestorageEntryIdentifier value = bean.getId();
					final List<ValidationConstraint<FilestorageEntryIdentifier>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<FilestorageEntryIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<FilestorageEntryIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// content
		{
			final String field = "content";
			result.put(field, new ValidatorRule<FilestorageEntryBean>() {

				@Override
				public Collection<ValidationError> validate(final FilestorageEntryBean bean) {
					final byte[] value = bean.getContent();
					final List<ValidationConstraint<byte[]>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<byte[]>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// contentType
		{
			final String field = "contentType";
			result.put(field, new ValidatorRule<FilestorageEntryBean>() {

				@Override
				public Collection<ValidationError> validate(final FilestorageEntryBean bean) {
					final String value = bean.getContentType();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// filename
		{
			final String field = "filename";
			result.put(field, new ValidatorRule<FilestorageEntryBean>() {

				@Override
				public Collection<ValidationError> validate(final FilestorageEntryBean bean) {
					final String value = bean.getFilename();
					final List<ValidationConstraint<String>> constraints = new ArrayList<>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
