package de.benjaminborbe.filestorage.service;

import com.google.inject.Injector;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.filestorage.api.FilestorageEntry;
import de.benjaminborbe.filestorage.api.FilestorageEntryDto;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.guice.FilestorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class FilestorageServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new FilestorageModulesMock());
		assertNotNull(injector.getInstance(FilestorageServiceImpl.class));
	}

	@Test
	public void testCreateFilestorageEntryIdentifier() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new FilestorageModulesMock());
		final FilestorageService filestorageService = injector.getInstance(FilestorageServiceImpl.class);
		assertThat(filestorageService.createFilestorageEntryIdentifier(null), is(nullValue()));
		assertThat(filestorageService.createFilestorageEntryIdentifier(""), is(nullValue()));
		assertThat(filestorageService.createFilestorageEntryIdentifier(" "), is(nullValue()));
		assertThat(filestorageService.createFilestorageEntryIdentifier("1337"), is(notNullValue()));
		assertThat(filestorageService.createFilestorageEntryIdentifier("1337").getId(), is("1337"));
	}

	@Test
	public void testCreateFilestorageEntry() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new FilestorageModulesMock());
		final FilestorageService filestorageService = injector.getInstance(FilestorageServiceImpl.class);
		{
			try {
				final FilestorageEntryDto filestorageEntry = new FilestorageEntryDto();
				filestorageService.createFilestorageEntry(filestorageEntry);
				fail("ValidationException expected");
			} catch (ValidationException e) {
				assertThat(e, is(notNullValue()));
			}
		}
		{
			try {
				final FilestorageEntryDto filestorageEntry = new FilestorageEntryDto();
				filestorageEntry.setContent(new byte[0]);
				filestorageService.createFilestorageEntry(filestorageEntry);
				fail("ValidationException expected");
			} catch (ValidationException e) {
				assertThat(e, is(notNullValue()));
			}
		}
		{
			try {
				final FilestorageEntryDto filestorageEntry = new FilestorageEntryDto();
				filestorageEntry.setContent(new byte[0]);
				filestorageEntry.setContentType("text/plain");
				filestorageService.createFilestorageEntry(filestorageEntry);
				fail("ValidationException expected");
			} catch (ValidationException e) {
				assertThat(e, is(notNullValue()));
			}
		}
		{
			final FilestorageEntryDto filestorageEntry = new FilestorageEntryDto();
			filestorageEntry.setContent(new byte[0]);
			filestorageEntry.setContentType("text/plain");
			filestorageEntry.setFilename("readme.txt");
			final FilestorageEntryIdentifier filestorageEntryIdentifier = filestorageService.createFilestorageEntry(filestorageEntry);
			assertThat(filestorageEntryIdentifier, is(notNullValue()));
		}
	}

	@Test
	public void testGetFilestorageEntry() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new FilestorageModulesMock());
		final FilestorageService filestorageService = injector.getInstance(FilestorageServiceImpl.class);
		final FilestorageEntryDto filestorageEntryDto = new FilestorageEntryDto();
		filestorageEntryDto.setContent(new byte[0]);
		filestorageEntryDto.setContentType("text/plain");
		filestorageEntryDto.setFilename("readme.txt");
		final FilestorageEntryIdentifier filestorageEntryIdentifier = filestorageService.createFilestorageEntry(filestorageEntryDto);
		assertThat(filestorageEntryIdentifier, is(notNullValue()));

		final FilestorageEntry filestorageEntry = filestorageService.getFilestorageEntry(filestorageEntryIdentifier);
		assertThat(filestorageEntry, is(notNullValue()));
	}

	@Test
	public void testDeleteFilestorageEntry() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new FilestorageModulesMock());
		final FilestorageService filestorageService = injector.getInstance(FilestorageServiceImpl.class);
		final FilestorageEntryDto filestorageEntryDto = new FilestorageEntryDto();
		filestorageEntryDto.setContent(new byte[0]);
		filestorageEntryDto.setContentType("text/plain");
		filestorageEntryDto.setFilename("readme.txt");
		final FilestorageEntryIdentifier filestorageEntryIdentifier = filestorageService.createFilestorageEntry(filestorageEntryDto);
		assertThat(filestorageEntryIdentifier, is(notNullValue()));

		{
			final FilestorageEntry filestorageEntry = filestorageService.getFilestorageEntry(filestorageEntryIdentifier);
			assertThat(filestorageEntry, is(notNullValue()));
		}

		filestorageService.deleteFilestorageEntry(filestorageEntryIdentifier);

		{
			final FilestorageEntry filestorageEntry = filestorageService.getFilestorageEntry(filestorageEntryIdentifier);
			assertThat(filestorageEntry, is(nullValue()));
		}

	}

}
