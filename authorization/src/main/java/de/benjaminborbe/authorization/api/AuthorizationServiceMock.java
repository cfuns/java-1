package de.benjaminborbe.authorization.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthorizationServiceMock implements AuthorizationService {

	@Inject
	public AuthorizationServiceMock() {
	}

	@Override
	public void execute() {
	}
}
