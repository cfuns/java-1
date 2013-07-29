package de.benjaminborbe.monitoring.dao;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintAnd;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintOr;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.storage.api.StorageException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoringNodeValidator extends ValidatorBase<MonitoringNodeBean> {

	private final class ValidationConstraintParentIdExists implements ValidationConstraint<MonitoringNodeIdentifier> {

		@Override
		public boolean precondition(final MonitoringNodeIdentifier object) {
			return object != null;
		}

		@Override
		public boolean validate(final MonitoringNodeIdentifier object) {
			try {
				return monitoringNodeDao.exists(object);
			} catch (final StorageException e) {
				return false;
			}
		}
	}

	private final class ValidationConstraintParentIdNotId implements ValidationConstraint<MonitoringNodeIdentifier> {

		private final MonitoringNodeIdentifier id;

		public ValidationConstraintParentIdNotId(final MonitoringNodeIdentifier id) {
			this.id = id;
		}

		@Override
		public boolean precondition(final MonitoringNodeIdentifier object) {
			return object != null;
		}

		@Override
		public boolean validate(final MonitoringNodeIdentifier object) {
			return !object.equals(id);
		}
	}

	private final ValidationConstraintValidator validationConstraintValidator;

	private final MonitoringNodeDao monitoringNodeDao;

	private final MonitoringCheckRegistry monitoringCheckRegistry;

	@Inject
	public MonitoringNodeValidator(
		final ValidationConstraintValidator validationConstraintValidator,
		final MonitoringNodeDao monitoringNodeDao,
		final MonitoringCheckRegistry monitoringCheckRegistry
	) {
		this.validationConstraintValidator = validationConstraintValidator;
		this.monitoringNodeDao = monitoringNodeDao;
		this.monitoringCheckRegistry = monitoringCheckRegistry;
	}

	@Override
	public Class<MonitoringNodeBean> getType() {
		return MonitoringNodeBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<MonitoringNodeBean>> buildRules() {
		final Map<String, ValidatorRule<MonitoringNodeBean>> result = new HashMap<String, ValidatorRule<MonitoringNodeBean>>();

		// id
		{
			final String field = MonitoringNodeBeanMapper.ID;
			result.put(field, new ValidatorRule<MonitoringNodeBean>() {

				@Override
				public Collection<ValidationError> validate(final MonitoringNodeBean bean) {
					final MonitoringNodeIdentifier value = bean.getId();
					final List<ValidationConstraint<MonitoringNodeIdentifier>> constraints = new ArrayList<ValidationConstraint<MonitoringNodeIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<MonitoringNodeIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// name
		{
			final String field = MonitoringNodeBeanMapper.NAME;
			result.put(field, new ValidatorRule<MonitoringNodeBean>() {

				@Override
				public Collection<ValidationError> validate(final MonitoringNodeBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// checkType
		{
			final String field = MonitoringNodeBeanMapper.CHECK_TYPE;
			result.put(field, new ValidatorRule<MonitoringNodeBean>() {

				@Override
				public Collection<ValidationError> validate(final MonitoringNodeBean bean) {
					final MonitoringCheckIdentifier value = bean.getCheckType();
					final List<ValidationConstraint<MonitoringCheckIdentifier>> constraints = new ArrayList<ValidationConstraint<MonitoringCheckIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<MonitoringCheckIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		// parameter
		{
			final String field = MonitoringNodeBeanMapper.PARAMETER;
			result.put(field, new ValidatorRule<MonitoringNodeBean>() {

				@Override
				public Collection<ValidationError> validate(final MonitoringNodeBean bean) {
					final List<ValidationError> errors = new ArrayList<ValidationError>();
					final Map<String, String> value = bean.getParameter();
					final List<ValidationConstraint<Map<String, String>>> constraints = new ArrayList<ValidationConstraint<Map<String, String>>>();
					constraints.add(new ValidationConstraintNotNull<Map<String, String>>());
					errors.addAll(validationConstraintValidator.validate(field, value, constraints));
					final MonitoringCheck check = monitoringCheckRegistry.get(bean.getCheckType());
					if (check != null) {
						errors.addAll(check.validate(value));
					}
					return errors;
				}
			});
		}

		// parentId
		{
			final String field = MonitoringNodeBeanMapper.PARENT_ID;
			result.put(field, new ValidatorRule<MonitoringNodeBean>() {

				@Override
				public Collection<ValidationError> validate(final MonitoringNodeBean bean) {
					final MonitoringNodeIdentifier value = bean.getParentId();
					final List<ValidationConstraint<MonitoringNodeIdentifier>> constraints = new ArrayList<ValidationConstraint<MonitoringNodeIdentifier>>();

					// null
					final ValidationConstraint<MonitoringNodeIdentifier> v1 = new ValidationConstraintNull<MonitoringNodeIdentifier>();

					// not null
					final ValidationConstraint<MonitoringNodeIdentifier> v2 = new ValidationConstraintAnd<MonitoringNodeIdentifier>()
						.add(new ValidationConstraintIdentifier<MonitoringNodeIdentifier>()).add(new ValidationConstraintParentIdExists())
						.add(new ValidationConstraintParentIdNotId(bean.getId()));

					constraints.add(new ValidationConstraintOr<MonitoringNodeIdentifier>().add(v1).add(v2));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
