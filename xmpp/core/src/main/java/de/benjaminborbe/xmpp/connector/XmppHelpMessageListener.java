package de.benjaminborbe.xmpp.connector;

import javax.inject.Inject;
import de.benjaminborbe.xmpp.api.XmppCommand;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.slf4j.Logger;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XmppHelpMessageListener implements MessageListener {

	private final Logger logger;

	private final XmppCommandRegistry xmppCommandRegistry;

	@Inject
	public XmppHelpMessageListener(final Logger logger, final XmppCommandRegistry xmppCommandRegistry) {
		this.logger = logger;
		this.xmppCommandRegistry = xmppCommandRegistry;
	}

	@Override
	public void processMessage(final Chat chat, final Message message) {
		final Type type = message.getType();
		if (Type.chat.equals(type)) {
			if ("help".equalsIgnoreCase(message.getBody())) {
				try {
					final StringWriter sw = new StringWriter();
					sw.append("Following commands are available:");
					for (final String command : getCommands()) {
						sw.append("\n");
						sw.append(command);
					}
					chat.sendMessage(sw.toString());
				} catch (final XMPPException e) {
					logger.debug(e.getClass().getName(), e);
				}
			}
		}
	}

	private List<String> getCommands() {
		final List<String> commands = new ArrayList<>();
		for (final XmppCommand command : xmppCommandRegistry.getAll()) {
			commands.add(command.getName());
		}
		Collections.sort(commands);
		return commands;
	}
}
