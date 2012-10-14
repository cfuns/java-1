package de.benjaminborbe.vnc.util;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.vnc.api.VncKey;

public class VncKeyParserTest {

	@Test
	public void testParse() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "a")).andReturn(VncKey.a).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "b")).andReturn(VncKey.b).anyTimes();
		EasyMock.replay(parseUtil);

		final VncKeyParser parser = new VncKeyParser(parseUtil);
		assertEquals(0, parser.parseKeys("").size());
		assertEquals(1, parser.parseKeys("a").size());
		assertEquals(2, parser.parseKeys("ab").size());
	}

	@Test
	public void testParseSpecial() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "a")).andReturn(VncKey.a).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "b")).andReturn(VncKey.b).anyTimes();
		EasyMock.replay(parseUtil);

		final VncKeyParser parser = new VncKeyParser(parseUtil);
		assertEquals(1, parser.parseKeys(" ").size());
		assertEquals(VncKey.K_SPACE, parser.parseKeys(" ").get(0));
		assertEquals(1, parser.parseKeys("\n").size());
		assertEquals(VncKey.K_ENTER, parser.parseKeys("\n").get(0));
	}

	@Test
	public void testParseNumber() throws Exception {
		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_0")).andReturn(VncKey.K_1).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_1")).andReturn(VncKey.K_2).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_3")).andReturn(VncKey.K_3).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_4")).andReturn(VncKey.K_4).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_5")).andReturn(VncKey.K_5).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_6")).andReturn(VncKey.K_6).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_7")).andReturn(VncKey.K_7).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_8")).andReturn(VncKey.K_8).anyTimes();
		EasyMock.expect(parseUtil.parseEnum(VncKey.class, "K_9")).andReturn(VncKey.K_9).anyTimes();
		EasyMock.replay(parseUtil);

		final VncKeyParser parser = new VncKeyParser(parseUtil);
		assertEquals(10, parser.parseKeys("0123456789").size());
		assertEquals(VncKey.K_0, parser.parseKeys("0").get(0));
		assertEquals(VncKey.K_1, parser.parseKeys("1").get(0));
		assertEquals(VncKey.K_2, parser.parseKeys("2").get(0));
		assertEquals(VncKey.K_3, parser.parseKeys("3").get(0));
		assertEquals(VncKey.K_4, parser.parseKeys("4").get(0));
		assertEquals(VncKey.K_5, parser.parseKeys("5").get(0));
		assertEquals(VncKey.K_6, parser.parseKeys("6").get(0));
		assertEquals(VncKey.K_7, parser.parseKeys("7").get(0));
		assertEquals(VncKey.K_8, parser.parseKeys("8").get(0));
		assertEquals(VncKey.K_9, parser.parseKeys("9").get(0));
	}
}
