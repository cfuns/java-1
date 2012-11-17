package de.benjaminborbe.meta.guice;

import java.net.URL;

import org.osgi.service.obr.Repository;
import org.osgi.service.obr.RepositoryAdmin;
import org.osgi.service.obr.Resolver;
import org.osgi.service.obr.Resource;

public class RepositoryAdminMock implements RepositoryAdmin {

	public RepositoryAdminMock() {

	}

	@Override
	public Repository addRepository(final URL arg0) throws Exception {
		return null;
	}

	@Override
	public Resource[] discoverResources(final String arg0) {
		return new Resource[0];
	}

	@Override
	public Resource getResource(final String arg0) {
		return null;
	}

	@Override
	public Repository[] listRepositories() {
		return null;
	}

	@Override
	public boolean removeRepository(final URL arg0) {
		return false;
	}

	@Override
	public Resolver resolver() {
		return null;
	}

}
