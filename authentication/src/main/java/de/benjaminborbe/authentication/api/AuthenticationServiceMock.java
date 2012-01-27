package de.benjaminborbe.authentication.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AuthenticationServiceMock implements AuthenticationService {

	@Inject
	public AuthenticationServiceMock() {
	}

	@Override
	public void execute() {
	}
}
