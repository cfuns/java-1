package de.benjaminborbe.selenium.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.configuration.mock.ConfigurationServiceMock;
import de.benjaminborbe.lib.test.SystemTest;
import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfiguration;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationClick;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectText;
import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationGetUrl;
import de.benjaminborbe.selenium.core.SeleniumCoreConstatns;
import de.benjaminborbe.selenium.core.guice.SeleniumModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Category(SystemTest.class)
public class SeleniumCoreRunnerSystemTest {

	private static final int PORT = 4444;

	private static final String HOST = "192.168.223.143";

	private static boolean notFound = true;

	@BeforeClass
	public static void setUp() {
		final Socket socket = new Socket();
		final SocketAddress endpoint = new InetSocketAddress(HOST, PORT);
		try {
			socket.connect(endpoint, 500);
			notFound = !socket.isConnected();
			notFound = false;
		} catch (final IOException e) {
			notFound = true;
		} finally {
			try {
				socket.close();
			} catch (final IOException e) {
			}
		}
	}

	@Test
	public void testRun() throws ConfigurationServiceException {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new SeleniumModulesMock());
		final SeleniumCoreConfigurationExecutor runner = injector.getInstance(SeleniumCoreConfigurationExecutor.class);
		final ConfigurationServiceMock configurationServiceMock = injector.getInstance(ConfigurationServiceMock.class);
		configurationServiceMock.setConfigurationValue(new ConfigurationIdentifier(SeleniumCoreConstatns.CONFIG_SELENIUM_REMOTE_HOST), HOST);
		configurationServiceMock.setConfigurationValue(new ConfigurationIdentifier(SeleniumCoreConstatns.CONFIG_SELENIUM_REMOTE_PORT), PORT);

		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol = new SeleniumExecutionProtocolImpl();
		runner.execute(new SeleniumConfigurationSimple(), seleniumExecutionProtocol);
		assertThat(seleniumExecutionProtocol.isCompleted(), is(true));
		assertThat(seleniumExecutionProtocol.hasErrors(), is(false));
	}
}

class SeleniumConfigurationSimple implements SeleniumConfiguration {

	@Override
	public SeleniumConfigurationIdentifier getId() {
		return new SeleniumConfigurationIdentifier(getClass().getName());
	}

	@Override
	public String getName() {
		return "test";
	}

	@Override
	public List<SeleniumActionConfiguration> getActionConfigurations() {
		try {
			final List<SeleniumActionConfiguration> list = new ArrayList<SeleniumActionConfiguration>();
			list.add(new SeleniumActionConfigurationGetUrl("open heise", new URL("http://www.heise.de")));
			list.add(new SeleniumActionConfigurationClick("click themen_aktuell", "//*[@id=\"themen_aktuell\"]/ol/li[4]/a"));
			list.add(new SeleniumActionConfigurationExpectText("find headline mitte_uebersicht", "//*[@id=\"mitte_uebersicht\"]/div[1]/h1", "Facebook – nicht nur eine Erfolgsgeschichte"));
			return list;
		} catch (final MalformedURLException e) {
			return null;
		}
	}

	@Override
	public Boolean getCloseWindow() {
		return true;
	}

}
