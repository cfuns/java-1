package de.benjaminborbe.task.dao;

import com.google.inject.Injector;
import de.benjaminborbe.task.dao.task.TaskDao;
import de.benjaminborbe.task.dao.task.TaskDaoStorage;
import de.benjaminborbe.task.guice.TaskModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TaskDaoStorageIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TaskModulesMock());
		final TaskDao taskDao = injector.getInstance(TaskDao.class);
		assertNotNull(taskDao);
		assertEquals(TaskDaoStorage.class, taskDao.getClass());
	}

}
