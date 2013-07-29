package de.benjaminborbe.shortener.dao;

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

public class ShortenerUrlValidator extends ValidatorBase<ShortenerUrlBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	private final UrlUtil urlUtil;

	@Inject
	public ShortenerUrlValidator(final ValidationConstraintValidator validationConstraintValidator, final UrlUtil urlUtil) {
		this.validationConstraintValidator = validationConstraintValidator;
		this.urlUtil = urlUtil;
	}

	@Override
	public Class<ShortenerUrlBean> getType() {
		return ShortenerUrlBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<ShortenerUrlBean>> buildRules() {
		final Map<String, ValidatorRule<ShortenerUrlBean>> result = new HashMap<String, ValidatorRule<ShortenerUrlBean>>();

		// url
		{
			final String field = "url";
			result.put(field, new ValidatorRule<ShortenerUrlBean>() {

				@Override
				public Collection<ValidationError> validate(final ShortenerUrlBean bean) {
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

		return result;
	}
}
