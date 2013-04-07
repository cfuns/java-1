package de.benjaminborbe.virt.core.service;

import com.google.inject.Injector;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.virt.api.VirtIpAddress;
import de.benjaminborbe.virt.api.VirtMachineDto;
import de.benjaminborbe.virt.api.VirtNetwork;
import de.benjaminborbe.virt.api.VirtNetworkDto;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.core.guice.VirtModulesMock;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
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
	public void testCreateVirtualMachine() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("1337");
		final VirtMachineDto virtMachine = new VirtMachineDto();
		assertThat(virtService.createMachine(sessionIdentifier, virtMachine), is(not(nullValue())));
	}

	@Test
	public void testCreateNetworkIdentifier() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		assertThat(virtService.createNetworkIdentifier(null), is(nullValue()));
		assertThat(virtService.createNetworkIdentifier(""), is(nullValue()));
		assertThat(virtService.createNetworkIdentifier(" "), is(nullValue()));
		assertThat(virtService.createNetworkIdentifier("1337"), is(not(nullValue())));
	}

	@Test
	public void testCreateMachineIdentifier() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		assertThat(virtService.createMachineIdentifier(null), is(nullValue()));
		assertThat(virtService.createMachineIdentifier(""), is(nullValue()));
		assertThat(virtService.createMachineIdentifier(" "), is(nullValue()));
		assertThat(virtService.createMachineIdentifier("1337"), is(not(nullValue())));
	}

	@Test
	public void testCreateVirtualMachineIdentifier() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		assertThat(virtService.createVirtualMachineIdentifier(null), is(nullValue()));
		assertThat(virtService.createVirtualMachineIdentifier(""), is(nullValue()));
		assertThat(virtService.createVirtualMachineIdentifier(" "), is(nullValue()));
		assertThat(virtService.createVirtualMachineIdentifier("1337"), is(not(nullValue())));
	}

	@Test
	public void testCreateNetwork() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("1337");

		{
			final VirtNetworkDto networkDto = new VirtNetworkDto();
			try {
				virtService.createNetwork(sessionIdentifier, networkDto);
				fail("ValidatorException expected");
			} catch (ValidationException e) {
				assertThat(e, is(not(nullValue())));
			}
		}
		{
			final VirtNetworkDto networkDto = new VirtNetworkDto();
			networkDto.setName("myNetwork");
			final VirtNetworkIdentifier networkIdentifier = virtService.createNetwork(sessionIdentifier, networkDto);
			assertThat(networkIdentifier, is(not(nullValue())));
		}
	}

	@Test
	public void testGetNetwork() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VirtModulesMock());
		final VirtCoreServiceImpl virtService = injector.getInstance(VirtCoreServiceImpl.class);
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("1337");

		final String name = "myNetwork";
		final VirtIpAddress ip = new VirtIpAddress(127, 0, 0, 1);

		final VirtNetworkDto networkDto = new VirtNetworkDto();
		networkDto.setName(name);
		networkDto.setIp(ip);

		final VirtNetworkIdentifier networkIdentifier = virtService.createNetwork(sessionIdentifier, networkDto);
		assertThat(networkIdentifier, is(not(nullValue())));

		final VirtNetwork network = virtService.getNetwork(sessionIdentifier, networkIdentifier);
		assertThat(network, is(notNullValue()));
		assertThat(network.getId(), is(networkIdentifier));
		assertThat(network.getName(), is(name));
		assertThat(network.getIp(), is(ip));
	}
}

