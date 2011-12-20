package de.benjaminborbe.slash.servlet;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.slash.guice.SlashModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class SlashServletTest {

	@Test
	public void Singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final SlashServlet a = injector.getInstance(SlashServlet.class);
		final SlashServlet b = injector.getInstance(SlashServlet.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}
}
