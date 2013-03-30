package de.benjaminborbe.virt.api;

public class VirtNetworkDto implements VirtNetwork {

	private String name;

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
