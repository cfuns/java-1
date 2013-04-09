package de.benjaminborbe.wow.bot.model;

import java.util.HashSet;
import java.util.Set;

public class Model {

	private final Set<Server> servers = new HashSet<Server>();

	public Set<Server> getServers() {
		return servers;
	}

}
