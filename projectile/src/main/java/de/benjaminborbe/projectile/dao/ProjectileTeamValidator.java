package de.benjaminborbe.projectile.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.Validator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

public class ProjectileTeamValidator implements Validator<ProjectileTeamBean> {

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
	public Collection<ValidationError> validate(final ProjectileTeamBean team) {
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// id
		{
			final ProjectileTeamIdentifier id = team.getId();
			final List<ValidationConstraint<ProjectileTeamIdentifier>> constraints = new ArrayList<ValidationConstraint<ProjectileTeamIdentifier>>();
			constraints.add(new ValidationConstraintNotNull<ProjectileTeamIdentifier>());
			result.addAll(validationConstraintValidator.validate("id", id, constraints));
		}

		// name
		{
			final String name = team.getName();
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("name", name, constraints));
		}

		return result;
	}

	@Override
	public Collection<ValidationError> validateObject(final Object object) {
		return validate((ProjectileTeamBean) object);
	}

}
