package de.benjaminborbe.meta.online.guice;

import org.osgi.service.obr.Requirement;
import org.osgi.service.obr.Resolver;
import org.osgi.service.obr.Resource;

public class ResolverMock implements Resolver {

	@Override
	public void add(final Resource arg0) {
	}

	@Override
	public void deploy(final boolean arg0) {
	}

	@Override
	public Resource[] getAddedResources() {
		return null;
	}

	@Override
	public Resource[] getOptionalResources() {
		return null;
	}

	@Override
	public Requirement[] getReason(final Resource arg0) {
		return null;
	}

	@Override
	public Resource[] getRequiredResources() {
		return null;
	}

	@Override
	public Resource[] getResources(final Requirement arg0) {
		return null;
	}

	@Override
	public Requirement[] getUnsatisfiedRequirements() {
		return null;
	}

	@Override
	public boolean resolve() {
		return false;
	}

}
