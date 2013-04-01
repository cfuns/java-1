package de.benjaminborbe.xmpp.connector;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.slf4j.Logger;

import com.google.inject.Inject;

public class XmppDebugMessageListener implements MessageListener {

	private final Logger logger;

	@Inject
	public XmppDebugMessageListener(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void processMessage(final Chat chat, final Message message) {
		logger.trace("processMessage - chat: " + chat.getParticipant() + " message: " + message);
		logger.trace("getBody: " + message.getBody());
		logger.trace("getFrom: " + message.getFrom());
		logger.trace("getLanguage: " + message.getLanguage());
		logger.trace("getPacketID: " + message.getPacketID());
		logger.trace("getSubject: " + message.getSubject());
		logger.trace("getThread: " + message.getThread());
		logger.trace("getTo: " + message.getTo());
		logger.trace("getType: " + message.getType());
		final String from = message.getFrom();
		final String body = message.getBody();
		if (Type.chat.equals(message.getType())) {
			logger.trace(String.format("Received message '%1$s' from %2$s", body, from));
		}
	}

}
