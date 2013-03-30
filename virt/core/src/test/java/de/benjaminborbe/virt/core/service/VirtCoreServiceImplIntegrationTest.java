package de.benjaminborbe.virt.core.service;

import com.google.inject.Injector;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.virt.api.VirtMachineDto;
import de.benjaminborbe.virt.api.VirtNetworkDto;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.core.guice.VirtModulesMock;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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

	@Test
	public void testCreateVirtNetworkIdentifier() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		assertThat(virtService.createVirtNetworkIdentifier(null), is(nullValue()));
		assertThat(virtService.createVirtNetworkIdentifier(""), is(nullValue()));
		assertThat(virtService.createVirtNetworkIdentifier(" "), is(nullValue()));
		assertThat(virtService.createVirtNetworkIdentifier("1337"), is(not(nullValue())));
	}

	@Test
	public void testCreateVirtMachineIdentifier() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		assertThat(virtService.createVirtMachineIdentifier(null), is(nullValue()));
		assertThat(virtService.createVirtMachineIdentifier(""), is(nullValue()));
		assertThat(virtService.createVirtMachineIdentifier(" "), is(nullValue()));
		assertThat(virtService.createVirtMachineIdentifier("1337"), is(not(nullValue())));
	}

	@Test
	public void testCreateVirtNetwork() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("1337");

		{
			final VirtNetworkDto virtNetwork = new VirtNetworkDto();
			try {
				virtService.createVirtNetwork(sessionIdentifier, virtNetwork);
				fail("ValidatorException expected");
			} catch (ValidationException e) {
				assertThat(e, is(not(nullValue())));
			}
		}
		{
			final VirtNetworkDto virtNetwork = new VirtNetworkDto();
			virtNetwork.setName("myNetwork");
			final VirtNetworkIdentifier virtNetworkIdentifier = virtService.createVirtNetwork(sessionIdentifier, virtNetwork);
			assertThat(virtNetworkIdentifier, is(not(nullValue())));
		}
	}

}

