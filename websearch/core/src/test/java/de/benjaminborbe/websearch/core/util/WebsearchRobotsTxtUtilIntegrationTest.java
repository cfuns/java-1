package de.benjaminborbe.websearch.core.util;

import com.google.inject.Injector;
import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.mock.HttpdownloaderServiceMock;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.core.guice.WebsearchModulesMock;
import org.easymock.EasyMock;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WebsearchRobotsTxtUtilIntegrationTest {

	@Test
	public void testBuildRobotsTxtUrl() throws Exception {
		final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
		final HttpContent httpContent = EasyMock.createMock(HttpContent.class);
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		HttpdownloaderServiceMock httpdownloaderServiceMock = injector.getInstance(HttpdownloaderServiceMock.class);
		httpdownloaderServiceMock.setHttpResponse(httpResponse);

		EasyMock.expect(httpResponse.getContent()).andReturn(httpContent);
		EasyMock.expect(httpResponse.getHeader()).andReturn(null);
		final byte[] content = getContent().getBytes(HttpUtil.DEFAULT_CHARSET);
		EasyMock.expect(httpContent.getContent()).andReturn(content);

		Object[] mocks = new Object[]{httpResponse, httpContent};
		EasyMock.replay(mocks);

		final WebsearchRobotsTxtUtil u = injector.getInstance(WebsearchRobotsTxtUtil.class);
		assertThat(u.isAllowed(new URL("http://www.heise.de/")), is(true));
		assertThat(u.isAllowed(new URL("http://www.heise.de/bin/")), is(false));

		EasyMock.verify(mocks);
	}

	private String getContent() {
		StringBuilder sb = new StringBuilder();
		sb.append("User-agent: *\n");
		sb.append("Disallow: /bin/\n");
		return sb.toString();
	}
}
