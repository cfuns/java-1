package de.benjaminborbe.monitoring.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.check.MonitoringCheck;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidatorBase;
import de.benjaminborbe.tools.validation.ValidatorRule;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintAnd;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintOr;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;

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
			}
			catch (final StorageException e) {
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

	private final MonitoringCheckFactory monitoringCheckFactory;

	private final MonitoringNodeDao monitoringNodeDao;

	@Inject
	public MonitoringNodeValidator(
			final ValidationConstraintValidator validationConstraintValidator,
			final MonitoringCheckFactory monitoringCheckFactory,
			final MonitoringNodeDao monitoringNodeDao) {
		this.validationConstraintValidator = validationConstraintValidator;
		this.monitoringCheckFactory = monitoringCheckFactory;
		this.monitoringNodeDao = monitoringNodeDao;
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
					final MonitoringCheckType value = bean.getCheckType();
					final List<ValidationConstraint<MonitoringCheckType>> constraints = new ArrayList<ValidationConstraint<MonitoringCheckType>>();
					constraints.add(new ValidationConstraintNotNull<MonitoringCheckType>());
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
					final MonitoringCheck check = monitoringCheckFactory.get(bean.getCheckType());
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

				@SuppressWarnings("unchecked")
				@Override
				public Collection<ValidationError> validate(final MonitoringNodeBean bean) {
					final MonitoringNodeIdentifier value = bean.getParentId();
					final List<ValidationConstraint<MonitoringNodeIdentifier>> constraints = new ArrayList<ValidationConstraint<MonitoringNodeIdentifier>>();
					constraints.add(new ValidationConstraintOr<MonitoringNodeIdentifier>(new ValidationConstraintAnd<MonitoringNodeIdentifier>(
							new ValidationConstraintIdentifier<MonitoringNodeIdentifier>(), new ValidationConstraintParentIdExists(), new ValidationConstraintParentIdNotId(bean.getId())),
							new ValidationConstraintNull<MonitoringNodeIdentifier>()));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}

		return result;
	}
}
