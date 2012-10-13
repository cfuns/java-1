package de.benjaminborbe.xmpp.connector;

import org.jivesoftware.smack.RosterEntry;

public class XmppUser {

	private final String uid;

	private final String name;

	public XmppUser(final RosterEntry entry) {
		this.uid = entry.getUser();
		this.name = entry.getName();
	}

	public XmppUser(final String uid) {
		this.uid = uid;
		this.name = null;
	}

	public String getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}
}
