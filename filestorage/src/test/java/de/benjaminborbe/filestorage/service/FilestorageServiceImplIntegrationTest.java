package de.benjaminborbe.filestorage.service;

import com.google.inject.Injector;
import de.benjaminborbe.filestorage.guice.FilestorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class FilestorageServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new FilestorageModulesMock());
		assertNotNull(injector.getInstance(FilestorageServiceImpl.class));
	}

	@Test
	public void testCreateFilestorageEntry() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new FilestorageModulesMock());
		final FilestorageServiceImpl filestorageService = injector.getInstance(FilestorageServiceImpl.class);
		assertThat(filestorageService.createFilestorageEntry(null), is(nullValue()));
		assertThat(filestorageService.createFilestorageEntry(""), is(nullValue()));
		assertThat(filestorageService.createFilestorageEntry(" "), is(nullValue()));
		assertThat(filestorageService.createFilestorageEntry("1337"), is(notNullValue()));
		assertThat(filestorageService.createFilestorageEntry("1337").getId(), is("1337"));
	}

}
