package de.benjaminborbe.xmpp.connector;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

public class XmppManager {

	private static final int packetReplyTimeout = 500; // millis

	private final String server;

	private final int port;

	private ConnectionConfiguration config;

	private XMPPConnection connection;

	private ChatManager chatManager;

	private MessageListener messageListener;

	public XmppManager(final String server, final int port) {
		this.server = server;
		this.port = port;
	}

	public void init() throws XMPPException {

		System.out.println(String.format("Initializing connection to server %1$s port %2$d", server, port));

		SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);

		config = new ConnectionConfiguration(server, port);
		config.setSASLAuthenticationEnabled(false);
		config.setSecurityMode(SecurityMode.disabled);

		connection = new XMPPConnection(config);
		connection.connect();

		System.out.println("Connected: " + connection.isConnected());

		chatManager = connection.getChatManager();
		messageListener = new MyMessageListener();

	}

	public void performLogin(final String username, final String password) throws XMPPException {
		if (connection != null && connection.isConnected()) {
			connection.login(username, password);
		}
	}

	public void setStatus(final boolean available, final String status) {

		final Presence.Type type = available ? Type.available : Type.unavailable;
		final Presence presence = new Presence(type);

		presence.setStatus(status);
		connection.sendPacket(presence);

	}

	public void destroy() {
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
		}
	}

	public void sendMessage(final String message, final String buddyJID) throws XMPPException {
		System.out.println(String.format("Sending mesage '%1$s' to user %2$s", message, buddyJID));
		final Chat chat = chatManager.createChat(buddyJID, messageListener);
		chat.sendMessage(message);
	}

	public void createEntry(final String user, final String name) throws Exception {
		System.out.println(String.format("Creating entry for buddy '%1$s' with name %2$s", user, name));
		final Roster roster = connection.getRoster();
		roster.createEntry(user, name, null);
	}

	class MyMessageListener implements MessageListener {

		@Override
		public void processMessage(final Chat chat, final Message message) {
			final String from = message.getFrom();
			final String body = message.getBody();
			System.out.println(String.format("Received message '%1$s' from %2$s", body, from));
		}

	}

}
