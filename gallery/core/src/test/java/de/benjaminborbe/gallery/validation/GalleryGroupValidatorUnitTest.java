package de.benjaminborbe.gallery.validation;

import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.dao.GalleryGroupBean;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GalleryGroupValidatorUnitTest {

	@Test
	public void testValidate() throws Exception {
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		final GalleryGroupValidator GalleryGroupValidator = new GalleryGroupValidator(validationConstraintValidator);
		final GalleryGroupBean bean = new GalleryGroupBean();
		assertThat(GalleryGroupValidator.validate(bean).size(), is(2));
		bean.setId(new GalleryGroupIdentifier("1337"));
		assertThat(GalleryGroupValidator.validate(bean).size(), is(1));
		bean.setName("foobar");
		assertThat(GalleryGroupValidator.validate(bean).size(), is(0));

		bean.setPreviewLongSideMaxLength(-1);
		bean.setPreviewLongSideMinLength(-1);
		bean.setPreviewShortSideMaxLength(-1);
		bean.setPreviewShortSideMinLength(-1);

		bean.setLongSideMaxLength(-1);
		bean.setLongSideMinLength(-1);
		bean.setShortSideMaxLength(-1);
		bean.setShortSideMinLength(-1);

		assertThat(GalleryGroupValidator.validate(bean).size(), is(8));

		bean.setPreviewLongSideMaxLength(0);
		bean.setPreviewLongSideMinLength(0);
		bean.setPreviewShortSideMaxLength(0);
		bean.setPreviewShortSideMinLength(0);

		bean.setLongSideMaxLength(0);
		bean.setLongSideMinLength(0);
		bean.setShortSideMaxLength(0);
		bean.setShortSideMinLength(0);

		assertThat(GalleryGroupValidator.validate(bean).size(), is(0));

	}
}
