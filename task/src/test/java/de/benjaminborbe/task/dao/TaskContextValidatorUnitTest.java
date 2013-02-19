package de.benjaminborbe.task.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;

public class TaskContextValidatorUnitTest {

	private TaskContextValidator getValidator() {
		final ValidationConstraintValidator v = new ValidationConstraintValidator();
		return new TaskContextValidator(v);
	}

	@Test
	public void testValidName() throws Exception {
		final TaskContextValidator taskContextValidator = getValidator();
		{
			final TaskContextBean taskContext = buildTaskContext("bla");
			assertThat(taskContextValidator.validate(taskContext).size(), is(0));
		}
		{
			final TaskContextBean taskContext = buildTaskContext("a b");
			assertThat(taskContextValidator.validate(taskContext).size(), is(1));
		}
		{
			final TaskContextBean taskContext = buildTaskContext("a-b");
			assertThat(taskContextValidator.validate(taskContext).size(), is(0));
		}
		{
			final TaskContextBean taskContext = buildTaskContext("all");
			assertThat(taskContextValidator.validate(taskContext).size(), is(1));
		}
		{
			final TaskContextBean taskContext = buildTaskContext("none");
			assertThat(taskContextValidator.validate(taskContext).size(), is(1));
		}
	}

	private TaskContextBean buildTaskContext(final String string) {
		final TaskContextBean bean = new TaskContextBean();
		bean.setId(new TaskContextIdentifier("13"));
		bean.setName(string);
		return bean;
	}
}
