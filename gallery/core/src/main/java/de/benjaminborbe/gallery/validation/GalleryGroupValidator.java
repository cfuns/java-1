package de.benjaminborbe.gallery.validation;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.dao.GalleryGroupBean;
import de.benjaminborbe.gallery.dao.GalleryGroupBeanMapper;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidatorBase;
import de.benjaminborbe.lib.validation.ValidatorRule;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintAnd;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIdentifier;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIntegerGE;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintOr;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringOnlyLetters;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryGroupValidator extends ValidatorBase<GalleryGroupBean> {

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public GalleryGroupValidator(final ValidationConstraintValidator validationConstraintValidator) {
		this.validationConstraintValidator = validationConstraintValidator;
	}

	@Override
	public Class<GalleryGroupBean> getType() {
		return GalleryGroupBean.class;
	}

	@Override
	protected Map<String, ValidatorRule<GalleryGroupBean>> buildRules() {
		final Map<String, ValidatorRule<GalleryGroupBean>> result = new HashMap<String, ValidatorRule<GalleryGroupBean>>();
		{
			final String field = GalleryGroupBeanMapper.ID;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final GalleryGroupIdentifier value = bean.getId();
					final List<ValidationConstraint<GalleryGroupIdentifier>> constraints = new ArrayList<ValidationConstraint<GalleryGroupIdentifier>>();
					constraints.add(new ValidationConstraintNotNull<GalleryGroupIdentifier>());
					constraints.add(new ValidationConstraintIdentifier<GalleryGroupIdentifier>());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.NAME;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final String value = bean.getName();
					final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
					constraints.add(new ValidationConstraintNotNull<String>());
					constraints.add(new ValidationConstraintStringMinLength(1));
					constraints.add(new ValidationConstraintStringMaxLength(255));
					constraints.add(new ValidationConstraintStringOnlyLetters());
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.PREVIEW_LONG_SIDE_MAX_LENGTH;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final Integer value = bean.getPreviewLongSideMaxLength();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintOr<Integer>().add(new ValidationConstraintNull<Integer>()).add(new ValidationConstraintAnd<Integer>().add(new ValidationConstraintNotNull<Integer>()).add(new ValidationConstraintIntegerGE(0))));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.PREVIEW_LONG_SIDE_MIN_LENGTH;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final Integer value = bean.getPreviewLongSideMinLength();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintOr<Integer>().add(new ValidationConstraintNull<Integer>()).add(new ValidationConstraintAnd<Integer>().add(new ValidationConstraintNotNull<Integer>()).add(new ValidationConstraintIntegerGE(0))));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.PREVIEW_SHORT_SIDE_MAX_LENGTH;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final Integer value = bean.getPreviewShortSideMaxLength();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintOr<Integer>().add(new ValidationConstraintNull<Integer>()).add(new ValidationConstraintAnd<Integer>().add(new ValidationConstraintNotNull<Integer>()).add(new ValidationConstraintIntegerGE(0))));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.PREVIEW_SHORT_SIDE_MIN_LENGTH;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final Integer value = bean.getPreviewShortSideMinLength();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintOr<Integer>().add(new ValidationConstraintNull<Integer>()).add(new ValidationConstraintAnd<Integer>().add(new ValidationConstraintNotNull<Integer>()).add(new ValidationConstraintIntegerGE(0))));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.LONG_SIDE_MAX_LENGTH;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final Integer value = bean.getLongSideMaxLength();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintOr<Integer>().add(new ValidationConstraintNull<Integer>()).add(new ValidationConstraintAnd<Integer>().add(new ValidationConstraintNotNull<Integer>()).add(new ValidationConstraintIntegerGE(0))));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.LONG_SIDE_MIN_LENGTH;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final Integer value = bean.getLongSideMinLength();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintOr<Integer>().add(new ValidationConstraintNull<Integer>()).add(new ValidationConstraintAnd<Integer>().add(new ValidationConstraintNotNull<Integer>()).add(new ValidationConstraintIntegerGE(0))));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.SHORT_SIDE_MAX_LENGTH;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final Integer value = bean.getShortSideMaxLength();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintOr<Integer>().add(new ValidationConstraintNull<Integer>()).add(new ValidationConstraintAnd<Integer>().add(new ValidationConstraintNotNull<Integer>()).add(new ValidationConstraintIntegerGE(0))));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		{
			final String field = GalleryGroupBeanMapper.SHORT_SIDE_MIN_LENGTH;
			result.put(field, new ValidatorRule<GalleryGroupBean>() {

				@Override
				public Collection<ValidationError> validate(final GalleryGroupBean bean) {
					final Integer value = bean.getShortSideMinLength();
					final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
					constraints.add(new ValidationConstraintOr<Integer>().add(new ValidationConstraintNull<Integer>()).add(new ValidationConstraintAnd<Integer>().add(new ValidationConstraintNotNull<Integer>()).add(new ValidationConstraintIntegerGE(0))));
					return validationConstraintValidator.validate(field, value, constraints);
				}
			});
		}
		return result;
	}

}
