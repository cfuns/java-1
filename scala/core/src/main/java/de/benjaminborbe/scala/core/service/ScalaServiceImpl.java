package de.benjaminborbe.scala.core.service;

import de.benjaminborbe.scala.api.ScalaService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ScalaServiceImpl implements ScalaService {

	private final Logger logger;

	@Inject
	public ScalaServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String helloWorld() {
		logger.trace("execute");
		return de.benjaminborbe.scala.core.util.HelloWorld.msg();
	}

}
