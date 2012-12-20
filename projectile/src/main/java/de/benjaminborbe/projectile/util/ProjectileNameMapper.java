package de.benjaminborbe.projectile.util;

import com.google.inject.Inject;

public class ProjectileNameMapper {

	@Inject
	public ProjectileNameMapper() {
	}

	public String fullnameToLogin(final String fullname) {
		final String[] parts = fullname.toLowerCase().replaceFirst("van ", "").replaceAll("ß", "ss").replaceAll("ä", "ae").replaceAll("ü", "ue").replaceAll("ö", "oe").split(" ");
		if (parts != null && parts.length > 1) {
			return parts[parts.length - 1].substring(0, 1) + parts[0];
		}
		else {
			return fullname;
		}
	}
}
