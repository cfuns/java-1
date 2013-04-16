package de.benjaminborbe.wow.core.bot.model;

import java.util.HashSet;
import java.util.Set;

public class Model {

	private final Set<Server> servers = new HashSet<>();

	public Set<Server> getServers() {
		return servers;
	}

}
