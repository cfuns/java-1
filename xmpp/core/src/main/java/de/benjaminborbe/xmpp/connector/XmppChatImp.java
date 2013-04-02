package de.benjaminborbe.xmpp.connector;

import de.benjaminborbe.xmpp.api.XmppChat;
import de.benjaminborbe.xmpp.api.XmppChatException;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

public class XmppChatImp implements XmppChat {

	private final Chat chat;

	public XmppChatImp(final Chat chat) {
		this.chat = chat;
	}

	@Override
	public void send(final String msg) throws XmppChatException {
		try {
			chat.sendMessage(msg);
		} catch (final XMPPException e) {
			throw new XmppChatException(e);
		}
	}

}
