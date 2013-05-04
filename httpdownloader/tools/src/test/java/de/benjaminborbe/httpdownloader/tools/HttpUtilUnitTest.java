package de.benjaminborbe.httpdownloader.tools;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpUtilUnitTest {

	@Test
	public void testIsAvailable() {
		final ChannelTools channelTools = new ChannelTools();
		final StreamUtil streamUtil = new StreamUtil(channelTools);
		final HttpUtil httpUtil = new HttpUtil(streamUtil);
		{
			final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
			EasyMock.expect(httpResponse.getReturnCode()).andReturn(null);
			EasyMock.replay(httpResponse);
			assertThat(httpUtil.isAvailable(httpResponse), is(false));
		}
		{
			final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
			EasyMock.expect(httpResponse.getReturnCode()).andReturn(404);
			EasyMock.replay(httpResponse);
			assertThat(httpUtil.isAvailable(httpResponse), is(false));
		}
		{
			final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
			EasyMock.expect(httpResponse.getReturnCode()).andReturn(200);
			EasyMock.replay(httpResponse);
			assertThat(httpUtil.isAvailable(httpResponse), is(true));
		}
	}

	@Test
	public void testIsHtml() throws Exception {
		final ChannelTools channelTools = new ChannelTools();
		final StreamUtil streamUtil = new StreamUtil(channelTools);
		final HttpUtil httpUtil = new HttpUtil(streamUtil);
		{
			final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
			EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_TYPE)).andReturn(null);
			EasyMock.replay(httpHeader);
			assertThat(httpUtil.isHtml(httpHeader), is(false));
		}
		{
			final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
			EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_TYPE)).andReturn("text/plain");
			EasyMock.replay(httpHeader);
			assertThat(httpUtil.isHtml(httpHeader), is(false));
		}
		{
			final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
			EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_TYPE)).andReturn("text/html");
			EasyMock.replay(httpHeader);
			assertThat(httpUtil.isHtml(httpHeader), is(true));
		}
		{
			final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
			EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_TYPE)).andReturn("text/html;charset=UTF-8");
			EasyMock.replay(httpHeader);
			assertThat(httpUtil.isHtml(httpHeader), is(true));
		}
	}

	@Test
	public void testGetContent() throws Exception {
		final ChannelTools channelTools = new ChannelTools();
		final StreamUtil streamUtil = new StreamUtil(channelTools);
		final HttpUtil httpUtil = new HttpUtil(streamUtil);
		final String content = "Hello World";
		final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
		final HttpContent httpContent = EasyMock.createMock(HttpContent.class);
		EasyMock.expect(httpResponse.getContent()).andReturn(httpContent);
		EasyMock.expect(httpContent.getContent()).andReturn(content.getBytes());
		final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
		EasyMock.expect(httpResponse.getHeader()).andReturn(httpHeader);
		EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_TYPE)).andReturn("text/html; charset=utf-8");
		EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_ENCODING)).andReturn(null);

		final Object[] mocks = new Object[]{httpResponse, httpContent, httpHeader};
		EasyMock.replay(mocks);

		assertThat(httpUtil.getContent(httpResponse), is(content));

		EasyMock.verify(mocks);
	}

	@Test
	public void testGetContentEncoding() throws Exception {
		final ChannelTools channelTools = new ChannelTools();
		final StreamUtil streamUtil = new StreamUtil(channelTools);
		final HttpUtil httpUtil = new HttpUtil(streamUtil);
		final String content = "für";
		final String encoding = "UTF8";
		final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
		final HttpContent httpContent = EasyMock.createMock(HttpContent.class);
		EasyMock.expect(httpResponse.getContent()).andReturn(httpContent);
		EasyMock.expect(httpContent.getContent()).andReturn(content.getBytes(encoding));
		final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
		EasyMock.expect(httpResponse.getHeader()).andReturn(httpHeader);
		EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_TYPE)).andReturn("text/html; charset=utf-8");
		EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_ENCODING)).andReturn(null);

		final Object[] mocks = new Object[]{httpResponse, httpContent, httpHeader};
		EasyMock.replay(mocks);

		assertThat(httpUtil.getContent(httpResponse), is(content));

		EasyMock.verify(mocks);
	}

	@Test
	public void testGetCharset() {
		final ChannelTools channelTools = new ChannelTools();
		final StreamUtil streamUtil = new StreamUtil(channelTools);
		final HttpUtil httpUtil = new HttpUtil(streamUtil);
		final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
		final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
		EasyMock.expect(httpResponse.getHeader()).andReturn(httpHeader);
		EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_TYPE)).andReturn("text/html; charset=utf-8");

		final Object[] mocks = new Object[]{httpResponse, httpHeader};
		EasyMock.replay(mocks);

		assertThat(httpUtil.getCharset(httpResponse), is("utf-8"));

		EasyMock.verify(mocks);
	}

	@Test
	public void testGetCharsetDefault() {
		final ChannelTools channelTools = new ChannelTools();
		final StreamUtil streamUtil = new StreamUtil(channelTools);
		final HttpUtil httpUtil = new HttpUtil(streamUtil);
		final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
		final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
		EasyMock.expect(httpResponse.getHeader()).andReturn(httpHeader);
		EasyMock.expect(httpHeader.getValue(HttpUtil.CONTENT_TYPE)).andReturn("text/html");

		final Object[] mocks = new Object[]{httpResponse, httpHeader};
		EasyMock.replay(mocks);

		assertThat(httpUtil.getCharset(httpResponse), is("UTF8"));

		EasyMock.verify(mocks);
	}
}
