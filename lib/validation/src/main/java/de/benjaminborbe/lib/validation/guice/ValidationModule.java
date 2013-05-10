package de.benjaminborbe.lib.validation.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.lib.validation.ValidationExecutorImpl;
import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.lib.validation.ValidatorRegistryImpl;

import javax.inject.Singleton;

public class ValidationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ValidatorRegistry.class).to(ValidatorRegistryImpl.class).in(Singleton.class);
		bind(ValidationExecutor.class).to(ValidationExecutorImpl.class).in(Singleton.class);
	}
}
