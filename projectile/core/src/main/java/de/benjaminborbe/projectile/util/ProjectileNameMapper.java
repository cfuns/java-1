package de.benjaminborbe.projectile.util;

import org.slf4j.Logger;

import javax.inject.Inject;

public class ProjectileNameMapper {

	private final Logger logger;

	@Inject
	public ProjectileNameMapper(final Logger logger) {
		this.logger = logger;
	}

	public String fullnameToLogin(final String fullname) {
		final String[] parts = fullname.toLowerCase().replaceFirst("van ", "").replaceAll("ß", "ss").replaceAll("ä", "ae").replaceAll("ü", "ue").replaceAll("ö", "oe").split(" ");
		final String result;
		if (parts != null && parts.length > 1) {
			result = parts[parts.length - 1].substring(0, 1) + parts[0];
		} else {
			result = fullname;
		}
		logger.trace("fullnameToLogin - fullname: " + fullname + " => " + result);
		return result;
	}
}
