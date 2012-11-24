package de.benjaminborbe.wow.xmpp;

import org.slf4j.Logger;

import de.benjaminborbe.tools.util.ThreadResult;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;

public abstract class WowStartStopXmppCommand extends WowBaseXmppCommand {

	private class RepeatThread implements Runnable {

		private final XmppChat chat;

		private RepeatThread(final XmppChat chat) {
			this.chat = chat;
		}

		@Override
		public void run() {
			try {
				before();
				send(chat, "started");
				while (running.get()) {
					runAction();
				}
				send(chat, "stopped");
			}
			catch (final Exception e) {
				logger.debug(e.getClass().getName(), e);
			}
			finally {
				after();
			}
		}
	}

	protected final ThreadResult<Boolean> running = new ThreadResult<Boolean>(false);

	private final Logger logger;

	private final ThreadRunner threadRunner;

	public WowStartStopXmppCommand(final Logger logger, final ThreadRunner threadRunner) {
		super(logger);
		this.logger = logger;
		this.threadRunner = threadRunner;
	}

	protected abstract void before();

	protected abstract void after();

	protected abstract void runAction();

	@Override
	public void executeInternal(final XmppChat chat, final String command) throws XmppChatException {
		final String action = parseArg(command);
		if ("start".equals(action)) {
			if (!running.get()) {
				send(chat, "starting...");
				running.set(true);
				threadRunner.run("fishing thread", new RepeatThread(chat));
				send(chat, "started");
			}
			else {
				send(chat, "already started");
			}
		}
		else if ("stop".equals(action)) {
			if (running.get()) {
				send(chat, "stopping....");
				running.set(false);
			}
			else {
				send(chat, "already stopped");
			}
		}
		else {
			send(chat, "parameter expected! [start|stop]");
		}
	}

}
