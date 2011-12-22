package de.benjaminborbe.translate;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.translate.TranslateActivator;
import de.benjaminborbe.translate.guice.TranslateModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class TranslateActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TranslateModulesMock());
		final TranslateActivator o = injector.getInstance(TranslateActivator.class);
		assertNotNull(o);
	}

}
