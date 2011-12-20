package de.benjaminborbe.worktime.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Calendar;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.worktime.api.WorktimeRecorder;

@Singleton
public class WorktimeRecorderImpl implements WorktimeRecorder {

	private static final int TIMEOUT = 5 * 1000;

	private static final String HOSTNAME = "timetracker.rp.seibert-media.net";

	private static final int PORT = 443;

	private final Logger logger;

	private final WorktimeStorageService worktimeStorageService;

	private final TimeZoneUtil timeZoneUtil;

	private final CalendarUtil calendarUtil;

	@Inject
	public WorktimeRecorderImpl(
			final Logger logger,
			final WorktimeStorageService worktimeStorageService,
			final TimeZoneUtil timeZoneUtil,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.worktimeStorageService = worktimeStorageService;
		this.timeZoneUtil = timeZoneUtil;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void recordWorktime() {
		final boolean inOffice = inOffice();
		logger.debug("inOffice = " + inOffice);
		final Calendar calendar = calendarUtil.now(timeZoneUtil.getUTCTimeZone());
		worktimeStorageService.save(new WorktimeValueImpl(calendar, inOffice));
	}

	protected boolean inOffice() {
		Socket socket = null;
		try {
			socket = new Socket();
			final SocketAddress endpoint = new InetSocketAddress(HOSTNAME, PORT);
			socket.connect(endpoint, TIMEOUT);
			if (socket.isConnected()) {
				final String msg = "connected successful to " + HOSTNAME + ":" + PORT;
				logger.debug(msg);
				return true;
			}
			else {
				final String msg = "connecting failed to " + HOSTNAME + ":" + PORT;
				logger.warn(msg);
				return false;
			}
		}
		catch (final Exception e) {
			logger.warn("check tcp-connect to " + HOSTNAME + ":" + PORT + " failed");
			return false;
		}
		finally {
			try {
				if (socket != null)
					socket.close();
			}
			catch (final IOException e) {
				logger.debug("IOException while close socket", e);
			}
		}
	}
}
