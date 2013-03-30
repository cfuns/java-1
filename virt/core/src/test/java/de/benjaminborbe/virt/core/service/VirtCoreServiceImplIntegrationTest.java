package de.benjaminborbe.virt.core.service;

import com.google.inject.Injector;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.virt.api.VirtMachineDto;
import de.benjaminborbe.virt.core.guice.VirtModulesMock;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class VirtCoreServiceImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		assertNotNull(injector.getInstance(VirtCoreServiceImpl.class));
	}

	@Test
	public void testCreateVirtualMachine() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("1337");
		final VirtMachineDto virtMachine = new VirtMachineDto();
		assertThat(virtService.createVirtualMachine(sessionIdentifier, virtMachine), is(not(nullValue())));

	}

}

