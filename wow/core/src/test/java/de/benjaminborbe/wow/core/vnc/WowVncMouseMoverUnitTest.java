package de.benjaminborbe.wow.core.vnc;

import de.benjaminborbe.tools.util.RandomUtil;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.wow.core.WowConstants;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class WowVncMouseMoverUnitTest {

	@Test
	public void testRandomOneStep() throws Exception {
		final int stepCounter = 1;

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final VncLocation location = EasyMock.createMock(VncLocation.class);
		EasyMock.expect(location.getX()).andReturn(100).anyTimes();
		EasyMock.expect(location.getY()).andReturn(100).anyTimes();
		EasyMock.replay(location);

		final VncScreenContent screenContent = EasyMock.createMock(VncScreenContent.class);
		EasyMock.expect(screenContent.getPointerLocation()).andReturn(location).anyTimes();
		EasyMock.replay(screenContent);

		final RandomUtil randomUtil = EasyMock.createMock(RandomUtil.class);
		EasyMock.expect(randomUtil.getRandomized(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(stepCounter);
		EasyMock.replay(randomUtil);

		final VncService vncService = EasyMock.createMock(VncService.class);
		EasyMock.expect(vncService.getScreenContent()).andReturn(screenContent).anyTimes();
		vncService.mouseMouse(1100, 1100);
		EasyMock.replay(vncService);

		final WowVncMouseMover wowVncMouseMover = new WowVncMouseMover(logger, vncService, randomUtil);
		wowVncMouseMover.mouseMouse(1100, 1100);

		EasyMock.verify(location, screenContent, randomUtil, vncService);
	}

	@Test
	public void testRandomTwoStep() throws Exception {
		final int stepCounter = 2;

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final VncLocation location = EasyMock.createMock(VncLocation.class);
		EasyMock.expect(location.getX()).andReturn(100).anyTimes();
		EasyMock.expect(location.getY()).andReturn(100).anyTimes();
		EasyMock.replay(location);

		final VncScreenContent screenContent = EasyMock.createMock(VncScreenContent.class);
		EasyMock.expect(screenContent.getPointerLocation()).andReturn(location).anyTimes();
		EasyMock.replay(screenContent);

		final RandomUtil randomUtil = EasyMock.createMock(RandomUtil.class);
		EasyMock.expect(randomUtil.getRandomized(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(stepCounter);
		EasyMock.expect(randomUtil.getRandomized(500, WowConstants.MOUSE_MOVE_STEP_RAMDOM)).andReturn(500).times((stepCounter - 1) * 2);
		EasyMock.replay(randomUtil);

		final VncService vncService = EasyMock.createMock(VncService.class);
		EasyMock.expect(vncService.getScreenContent()).andReturn(screenContent).anyTimes();
		vncService.mouseMouse(600, 600);
		vncService.mouseMouse(1100, 1100);
		EasyMock.replay(vncService);

		final WowVncMouseMover wowVncMouseMover = new WowVncMouseMover(logger, vncService, randomUtil);
		wowVncMouseMover.mouseMouse(1100, 1100);

		EasyMock.verify(location, screenContent, randomUtil, vncService);
	}

	@Test
	public void testRandomFiveStep() throws Exception {
		final int stepCounter = 5;

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final VncLocation location = EasyMock.createMock(VncLocation.class);
		EasyMock.expect(location.getX()).andReturn(100).anyTimes();
		EasyMock.expect(location.getY()).andReturn(100).anyTimes();
		EasyMock.replay(location);

		final VncScreenContent screenContent = EasyMock.createMock(VncScreenContent.class);
		EasyMock.expect(screenContent.getPointerLocation()).andReturn(location).anyTimes();
		EasyMock.replay(screenContent);

		final RandomUtil randomUtil = EasyMock.createMock(RandomUtil.class);
		EasyMock.expect(randomUtil.getRandomized(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(stepCounter);
		EasyMock.expect(randomUtil.getRandomized(200, WowConstants.MOUSE_MOVE_STEP_RAMDOM)).andReturn(200).times((stepCounter - 1) * 2);
		EasyMock.replay(randomUtil);

		final VncService vncService = EasyMock.createMock(VncService.class);
		EasyMock.expect(vncService.getScreenContent()).andReturn(screenContent).anyTimes();
		vncService.mouseMouse(300, 300);
		vncService.mouseMouse(500, 500);
		vncService.mouseMouse(700, 700);
		vncService.mouseMouse(900, 900);
		vncService.mouseMouse(1100, 1100);
		EasyMock.replay(vncService);

		final WowVncMouseMover wowVncMouseMover = new WowVncMouseMover(logger, vncService, randomUtil);
		wowVncMouseMover.mouseMouse(1100, 1100);

		EasyMock.verify(location, screenContent, randomUtil, vncService);
	}
}
