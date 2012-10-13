package de.benjaminborbe.xmpp.connector;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.slf4j.Logger;

import com.google.inject.Inject;

public class XmppChatManagerListener implements ChatManagerListener {

	private final Logger logger;

	private final XmppHelpMessageListener xmppHelpMessageListener;

	private final XmppDebugMessageListener xmppDebugMessageListener;

	private final XmppExecuteMessageListener xmppExecuteMessageListener;

	@Inject
	public XmppChatManagerListener(
			final Logger logger,
			final XmppHelpMessageListener xmppHelpMessageListener,
			final XmppDebugMessageListener xmppDebugMessageListener,
			final XmppExecuteMessageListener xmppExecuteMessageListener) {
		this.logger = logger;
		this.xmppHelpMessageListener = xmppHelpMessageListener;
		this.xmppDebugMessageListener = xmppDebugMessageListener;
		this.xmppExecuteMessageListener = xmppExecuteMessageListener;
	}

	@Override
	public void chatCreated(final Chat chat, final boolean createdLocally) {
		logger.debug("chatCreated chat: " + chat.getParticipant() + " local: " + createdLocally);

		chat.addMessageListener(xmppHelpMessageListener);
		chat.addMessageListener(xmppDebugMessageListener);
		chat.addMessageListener(xmppExecuteMessageListener);
	}
}
