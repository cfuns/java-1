package de.benjaminborbe.bookmark.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringUrl;
import de.benjaminborbe.tools.url.UrlUtil;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarkValidator extends ValidatorBase<BookmarkBean> {

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
	protected Map<String, ValidatorRule<BookmarkBean>> buildRules() {
		final Map<String, ValidatorRule<BookmarkBean>> result = new HashMap<String, ValidatorRule<BookmarkBean>>();

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<BookmarkBean>() {

				@Override
				public Collection<ValidationError> validate(final BookmarkBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// url
		{
			final String field = "url";
			result.put(field, new ValidatorRule<BookmarkBean>() {

				@Override
				public Collection<ValidationError> validate(final BookmarkBean bean) {
					final String value = bean.getUrl();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstraintStringUrl());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// keywords
		{
			final String field = "keywords";
			result.put(field, new ValidatorRule<BookmarkBean>() {

				@Override
				public Collection<ValidationError> validate(final BookmarkBean bean) {
					final List<String> value = bean.getKeywords();
					final List<ValidationConstraint<List<String>>> constraints = new ArrayList<ValidationConstraint<List<String>>>();
					constraints.add(new ValidationConstraintNotNull<List<String>>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}

}
