package de.benjaminborbe.sample;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.sample.guice.SampleModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SampleActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SampleModulesMock());
		final SampleActivator o = injector.getInstance(SampleActivator.class);
		assertNotNull(o);
	}

}
