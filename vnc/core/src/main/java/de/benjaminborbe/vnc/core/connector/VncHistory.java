package de.benjaminborbe.vnc.core.connector;

import com.glavsoft.rfb.client.ClientToServerMessage;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class VncHistory {

	private final List<ClientToServerMessage> list = new ArrayList<>();

	@Inject
	public VncHistory() {
		super();
	}

	public void add(final ClientToServerMessage clientToServerMessage) {
		list.add(clientToServerMessage);
	}

	public List<ClientToServerMessage> getList() {
		return list;
	}
}
