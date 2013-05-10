package de.benjaminborbe.task.core.dao;

import com.google.inject.Injector;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.task.core.dao.context.TaskContextBean;
import de.benjaminborbe.task.core.guice.TaskModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TaskContextValidatorIntegrationTest {

	@Test
	public void testValidate() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final ValidationExecutor validationExecutor = injector.getInstance(ValidationExecutor.class);

		final TaskContextBean taskContext = new TaskContextBean();
		assertThat(validationExecutor.validate(taskContext).hasErrors(), is(true));
	}
}
