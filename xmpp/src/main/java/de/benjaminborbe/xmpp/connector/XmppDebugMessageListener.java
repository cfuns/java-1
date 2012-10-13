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
		logger.debug("processMessage - chat: " + chat.getParticipant() + " message: " + message);
		logger.debug("getBody: " + message.getBody());
		logger.debug("getFrom: " + message.getFrom());
		logger.debug("getLanguage: " + message.getLanguage());
		logger.debug("getPacketID: " + message.getPacketID());
		logger.debug("getSubject: " + message.getSubject());
		logger.debug("getThread: " + message.getThread());
		logger.debug("getTo: " + message.getTo());
		logger.debug("getType: " + message.getType());
		final String from = message.getFrom();
		final String body = message.getBody();
		if (Type.chat.equals(message.getType())) {
			logger.debug(String.format("Received message '%1$s' from %2$s", body, from));
		}
	}

}
