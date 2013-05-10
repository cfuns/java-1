package de.benjaminborbe.message.dao;

import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.message.api.MessageIdentifier;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MessageValidatorUnitTest {

	@Test
	public void testValidate() throws Exception {
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		final MessageValidator messageValidator = new MessageValidator(validationConstraintValidator);
		final MessageBean bean = new MessageBean();
		assertThat(messageValidator.validate(bean).size(), is(2));
		bean.setId(new MessageIdentifier(""));
		assertThat(messageValidator.validate(bean).size(), is(2));
		bean.setId(new MessageIdentifier("1337"));
		assertThat(messageValidator.validate(bean).size(), is(1));
		bean.setType(null);
		assertThat(messageValidator.validate(bean).size(), is(1));
		bean.setType("");
		assertThat(messageValidator.validate(bean).size(), is(1));
		bean.setType("asdf");
		assertThat(messageValidator.validate(bean).size(), is(0));
	}
}
