package de.benjaminborbe.projectile.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

public class ProjectileTeamValidator extends ValidatorBase<ProjectileTeamBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public ProjectileTeamValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<ProjectileTeamBean> getType() {
		return ProjectileTeamBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<ProjectileTeamBean>> buildRules() {
		final Map<String, ValidatorRule<ProjectileTeamBean>> result = new HashMap<String, ValidatorRule<ProjectileTeamBean>>();

		// id
		{
			final String field = "id";
			result.put(field, new ValidatorRule<ProjectileTeamBean>() {

				@Override
				public Collection<ValidationError> validate(final ProjectileTeamBean bean) {
					final ProjectileTeamIdentifier value = bean.getId();
					final List<ValidationConstraint<ProjectileTeamIdentifier>> constraints = new ArrayList<ValidationConstraint<ProjectileTeamIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<ProjectileTeamIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = "name";
			result.put(field, new ValidatorRule<ProjectileTeamBean>() {

				@Override
				public Collection<ValidationError> validate(final ProjectileTeamBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
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
