package de.benjaminborbe.xmpp.connector;

import com.google.inject.Inject;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.slf4j.Logger;

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
		logger.trace("chatCreated chat: " + chat.getParticipant() + " local: " + createdLocally);
		chat.addMessageListener(xmppHelpMessageListener);
		chat.addMessageListener(xmppExecuteMessageListener);
		chat.addMessageListener(xmppDebugMessageListener);
	}
}
