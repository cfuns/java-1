package de.benjaminborbe.virt.api;

public class VirtNetworkDto implements VirtNetwork {

	private String name;

	private VirtNetworkIdentifier id;

	private VirtIpAddress ip;

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public VirtNetworkIdentifier getId() {
		return id;
	}

	public void setId(final VirtNetworkIdentifier id) {
		this.id = id;
	}

	public void setIp(final VirtIpAddress ip) {
		this.ip = ip;
	}

	public VirtIpAddress getIp() {
		return ip;
	}
}
