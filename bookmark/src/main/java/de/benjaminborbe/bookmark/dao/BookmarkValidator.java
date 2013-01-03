package de.benjaminborbe.bookmark.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringUrl;

public class BookmarkValidator implements Validator<BookmarkBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	private final UrlUtil urlUtil;

	@Inject
	public BookmarkValidator(final ValidationConstraintValidator validationConstraintValidator, final UrlUtil urlUtil) {
		this.validationConstraintValidator = validationConstraintValidator;
		this.urlUtil = urlUtil;
	}

	@Override
	public Class<BookmarkBean> getType() {
		return BookmarkBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final BookmarkBean bookmark) {
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		{
			final String name = bookmark.getName();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("name", name, constraints));
		}

		// validate url
		{
			final String url = bookmark.getUrl();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			constraints.add(new ValidationConstraintStringUrl(urlUtil));
			result.addAll(validationConstraintValidator.validate("url", url, constraints));
		}

		// keywords
		{
			final List<String> keywords = bookmark.getKeywords();
			final List<ValidationConstraint<List<String>>> constraints = new ArrayList<ValidationConstraint<List<String>>>();
			constraints.add(new ValidationConstraintNotNull<List<String>>());
			result.addAll(validationConstraintValidator.validate("keywords", keywords, constraints));
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((BookmarkBean) object);
	}

}
