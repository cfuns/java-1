package de.benjaminborbe.xmpp.connector;

import com.google.inject.Inject;
import de.benjaminborbe.tools.registry.RegistryChangeListener;
import de.benjaminborbe.tools.util.ThreadRunner;
import de.benjaminborbe.xmpp.api.XmppCommand;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class XmppExecuteMessageListener implements MessageListener, RegistryChangeListener<XmppCommand> {

	private final class ExecuteCommand implements Runnable {

		private final Chat chat;

		private final XmppCommand command;

		private final String body;

		private ExecuteCommand(final Chat chat, final XmppCommand command, final String body) {
			this.chat = chat;
			this.command = command;
			this.body = body;
		}

		@Override
		public void run() {
			command.execute(new XmppChatImp(chat), body);
		}
	}

	private final Logger logger;

	private final Set<XmppCommand> commands = new HashSet<>();

	private final ThreadRunner threadRunner;

	@Inject
	public XmppExecuteMessageListener(final Logger logger, final XmppCommandRegistry xmppCommandRegistry, final ThreadRunner threadRunner) {
		this.logger = logger;
		this.threadRunner = threadRunner;
		xmppCommandRegistry.addListener(this);
	}

	@Override
	public void processMessage(final Chat chat, final Message message) {
		final Type type = message.getType();
		if (Type.chat.equals(type)) {
			final String body = message.getBody();
			for (final XmppCommand command : commands) {
				if (command.match(body)) {
					threadRunner.run("execute xmppchat message: " + message, new ExecuteCommand(chat, command, body));
				}
			}
		}
	}

	@Override
	public void onAdd(final XmppCommand t) {
		logger.trace("onAdd " + t.getName());
		commands.add(t);
	}

	@Override
	public void onRemove(final XmppCommand t) {
		logger.trace("onRemove " + t.getName());
		commands.remove(t);
	}
}
