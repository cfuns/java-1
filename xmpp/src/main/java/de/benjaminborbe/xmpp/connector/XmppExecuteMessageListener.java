package de.benjaminborbe.xmpp.connector;

import java.util.HashSet;
import java.util.Set;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.registry.RegistryChangeListener;
import de.benjaminborbe.xmpp.api.XmppCommand;

public class XmppExecuteMessageListener implements MessageListener, RegistryChangeListener<XmppCommand> {

	private final Logger logger;

	private final Set<XmppCommand> commands = new HashSet<XmppCommand>();

	@Inject
	public XmppExecuteMessageListener(final Logger logger, final XmppCommandRegistry xmppCommandRegistry) {
		this.logger = logger;
		xmppCommandRegistry.addListener(this);
	}

	@Override
	public void processMessage(final Chat chat, final Message message) {
		final Type type = message.getType();
		if (Type.chat.equals(type)) {
			final String body = message.getBody();
			for (final XmppCommand command : commands) {
				if (command.match(body)) {
					command.execute(new XmppChatImp(chat), body);
				}
			}
		}
	}

	@Override
	public void onAdd(final XmppCommand t) {
		logger.debug("onAdd " + t.getName());
		commands.add(t);
	}

	@Override
	public void onRemove(final XmppCommand t) {
		logger.debug("onRemove " + t.getName());
		commands.remove(t);
	}
}
