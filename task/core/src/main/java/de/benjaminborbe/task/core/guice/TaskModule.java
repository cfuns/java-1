package de.benjaminborbe.task.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.core.dao.attachment.TaskAttachmentDao;
import de.benjaminborbe.task.core.dao.attachment.TaskAttachmentDaoStorage;
import de.benjaminborbe.task.core.dao.context.TaskContextDao;
import de.benjaminborbe.task.core.dao.context.TaskContextDaoStorage;
import de.benjaminborbe.task.core.dao.task.TaskDao;
import de.benjaminborbe.task.core.dao.task.TaskDaoStorage;
import de.benjaminborbe.task.core.service.TaskServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

public class TaskModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TaskAttachmentDao.class).to(TaskAttachmentDaoStorage.class).in(Singleton.class);
		bind(TaskDao.class).to(TaskDaoStorage.class).in(Singleton.class);
		bind(TaskContextDao.class).to(TaskContextDaoStorage.class).in(Singleton.class);
		bind(TaskService.class).to(TaskServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(TaskValidatorLinker.class);
	}
}
