package de.benjaminborbe.projectile.api;

public class ProjectileServiceException extends Exception {

	private static final long serialVersionUID = 8025807583205475519L;

	public ProjectileServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public ProjectileServiceException(final String arg0) {
		super(arg0);
	}

	public ProjectileServiceException(final Throwable arg0) {
		super(arg0);
	}

}
