package de.benjaminborbe.shortener.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringUrl;

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
					constraints.add(new ValidationConstraintStringUrl(urlUtil));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
