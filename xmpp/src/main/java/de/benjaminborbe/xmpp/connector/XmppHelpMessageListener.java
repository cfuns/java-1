package de.benjaminborbe.xmpp.connector;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.slf4j.Logger;

import com.google.inject.Inject;

public class XmppHelpMessageListener implements MessageListener {

	private final Logger logger;

	@Inject
	public XmppHelpMessageListener(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public void processMessage(final Chat chat, final Message message) {
		final Type type = message.getType();
		if (Type.chat.equals(type)) {
			if ("help".equalsIgnoreCase(message.getBody())) {
				try {
					chat.sendMessage("Help is available for the following commands:\nhelp");
				}
				catch (final XMPPException e) {
					logger.debug(e.getClass().getName(), e);
				}
			}
		}
	}
}
