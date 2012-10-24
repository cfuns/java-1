package de.benjaminborbe.scala.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.scala.api.ScalaService;

@Singleton
public class ScalaServiceMock implements ScalaService {

	@Inject
	public ScalaServiceMock() {
	}

	@Override
	public void execute() {
	}
}
