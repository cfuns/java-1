package de.benjaminborbe.wow.vnc;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.tools.util.RandomUtil;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;

public class WowVncMouseMoverUnitTest {

	@Test
	public void testRandomOneStep() throws Exception {
		final int stepCounter = 1;

		final VncLocation location = EasyMock.createMock(VncLocation.class);
		EasyMock.expect(location.getX()).andReturn(100).anyTimes();
		EasyMock.expect(location.getY()).andReturn(100).anyTimes();
		EasyMock.replay(location);

		final VncScreenContent screenContent = EasyMock.createMock(VncScreenContent.class);
		EasyMock.expect(screenContent.getPointerLocation()).andReturn(location).anyTimes();
		EasyMock.replay(screenContent);

		final RandomUtil randomUtil = EasyMock.createMock(RandomUtil.class);
		EasyMock.expect(randomUtil.getRandomized(5, 20)).andReturn(stepCounter);
		EasyMock.replay(randomUtil);

		final VncService vncService = EasyMock.createMock(VncService.class);
		EasyMock.expect(vncService.getScreenContent()).andReturn(screenContent).anyTimes();
		vncService.mouseMouse(1100, 1100);
		EasyMock.replay(vncService);

		final WowVncMouseMover wowVncMouseMover = new WowVncMouseMover(vncService, randomUtil);
		wowVncMouseMover.mouseMouse(1100, 1100);

		EasyMock.verify(location, screenContent, randomUtil, vncService);
	}

	@Test
	public void testRandomTwoStep() throws Exception {
		final int stepCounter = 2;

		final VncLocation location = EasyMock.createMock(VncLocation.class);
		EasyMock.expect(location.getX()).andReturn(100).anyTimes();
		EasyMock.expect(location.getY()).andReturn(100).anyTimes();
		EasyMock.replay(location);

		final VncScreenContent screenContent = EasyMock.createMock(VncScreenContent.class);
		EasyMock.expect(screenContent.getPointerLocation()).andReturn(location).anyTimes();
		EasyMock.replay(screenContent);

		final RandomUtil randomUtil = EasyMock.createMock(RandomUtil.class);
		EasyMock.expect(randomUtil.getRandomized(5, 20)).andReturn(stepCounter);
		EasyMock.expect(randomUtil.getRandomized(500, 10)).andReturn(500).times((stepCounter - 1) * 2);
		EasyMock.replay(randomUtil);

		final VncService vncService = EasyMock.createMock(VncService.class);
		EasyMock.expect(vncService.getScreenContent()).andReturn(screenContent).anyTimes();
		vncService.mouseMouse(600, 600);
		vncService.mouseMouse(1100, 1100);
		EasyMock.replay(vncService);

		final WowVncMouseMover wowVncMouseMover = new WowVncMouseMover(vncService, randomUtil);
		wowVncMouseMover.mouseMouse(1100, 1100);

		EasyMock.verify(location, screenContent, randomUtil, vncService);
	}

	@Test
	public void testRandomFiveStep() throws Exception {
		final int stepCounter = 5;

		final VncLocation location = EasyMock.createMock(VncLocation.class);
		EasyMock.expect(location.getX()).andReturn(100).anyTimes();
		EasyMock.expect(location.getY()).andReturn(100).anyTimes();
		EasyMock.replay(location);

		final VncScreenContent screenContent = EasyMock.createMock(VncScreenContent.class);
		EasyMock.expect(screenContent.getPointerLocation()).andReturn(location).anyTimes();
		EasyMock.replay(screenContent);

		final RandomUtil randomUtil = EasyMock.createMock(RandomUtil.class);
		EasyMock.expect(randomUtil.getRandomized(5, 20)).andReturn(stepCounter);
		EasyMock.expect(randomUtil.getRandomized(200, 10)).andReturn(200).times((stepCounter - 1) * 2);
		EasyMock.replay(randomUtil);

		final VncService vncService = EasyMock.createMock(VncService.class);
		EasyMock.expect(vncService.getScreenContent()).andReturn(screenContent).anyTimes();
		vncService.mouseMouse(300, 300);
		vncService.mouseMouse(500, 500);
		vncService.mouseMouse(700, 700);
		vncService.mouseMouse(900, 900);
		vncService.mouseMouse(1100, 1100);
		EasyMock.replay(vncService);

		final WowVncMouseMover wowVncMouseMover = new WowVncMouseMover(vncService, randomUtil);
		wowVncMouseMover.mouseMouse(1100, 1100);

		EasyMock.verify(location, screenContent, randomUtil, vncService);
	}
}
