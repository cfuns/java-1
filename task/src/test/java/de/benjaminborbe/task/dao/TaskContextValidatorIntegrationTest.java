package de.benjaminborbe.task.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.task.guice.TaskModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.validation.ValidationExecutor;

public class TaskContextValidatorIntegrationTest {

	@Test
	public void testValidate() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final ValidationExecutor validationExecutor = injector.getInstance(ValidationExecutor.class);

		final TaskContextBean taskContext = new TaskContextBean();
		assertThat(validationExecutor.validate(taskContext).hasErrors(), is(true));
	}
}
