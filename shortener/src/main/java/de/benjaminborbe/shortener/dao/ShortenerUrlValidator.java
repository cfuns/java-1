package de.benjaminborbe.shortener.dao;

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

public class ShortenerUrlValidator implements Validator<ShortenerUrlBean> {

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
	public Collection<ValidationError> validate(final ShortenerUrlBean object) {
		final ShortenerUrlBean bean = object;
		final Set<ValidationError> result = new HashSet<ValidationError>();
		{
			final String url = bean.getUrl();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			constraints.add(new ValidationConstraintStringUrl(urlUtil));
			result.addAll(validationConstraintValidator.validate("url", url, constraints));
		}
		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((ShortenerUrlBean) object);
	}
}
