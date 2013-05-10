package de.benjaminborbe.wiki.dao;

import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WikiPageValidatorUnitTest {

	@Test
	public void testValidateName() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();

		final WikiPageValidator va = new WikiPageValidator(validationConstraintValidator);
		final WikiPageBean bean = new WikiPageBean();

		assertThat(va.validate(bean).size(), is(2));

		bean.setId(new WikiPageIdentifier("1337"));
		assertThat(va.validate(bean).size(), is(1));

		bean.setTitle("Title");
		assertThat(va.validate(bean).size(), is(0));

		bean.setTitle("Test Foo-Bar");
		assertThat(va.validate(bean).size(), is(0));

	}
}
