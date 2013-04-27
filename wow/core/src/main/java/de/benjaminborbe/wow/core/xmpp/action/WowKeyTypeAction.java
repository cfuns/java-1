package de.benjaminborbe.wow.core.xmpp.action;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncKeyParseException;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

public class WowKeyTypeAction extends WowActionBase {

	private final List<VncKey> keys;

	private final VncService vncService;

	private final Logger logger;

	private boolean executed = false;

	public WowKeyTypeAction(final Logger logger, final VncService vncService, final String name, final ThreadResult<Boolean> running, final List<VncKey> keys) {
		super(logger, name, running);
		this.logger = logger;
		this.vncService = vncService;
		this.keys = keys;
	}

	public WowKeyTypeAction(
		final Logger logger,
		final VncService vncService,
		final String name,
		final ThreadResult<Boolean> running,
		final String keys
	) throws VncKeyParseException {
		this(logger, vncService, name, running, vncService.getVncKeyParser().parseKeys(keys));
	}

	public WowKeyTypeAction(final Logger logger, final VncService vncService, final String name, final ThreadResult<Boolean> running, final VncKey... keys) {
		this(logger, vncService, name, running, Arrays.asList(keys));
	}

	@Override
	public void executeOnce() {
		logger.debug(name + " - execute started");
		try {
			for (final VncKey key : keys) {
				vncService.keyType(key);
				Thread.sleep(30);
			}
			executed = true;
		} catch (final VncServiceException e) {
			logger.debug(e.getClass().getName(), e);
		} catch (final InterruptedException e) {
		}
		logger.debug(name + " - execute finished");
	}

	@Override
	public boolean validateExecuteResult() {
		return executed && super.validateExecuteResult();
	}

}
