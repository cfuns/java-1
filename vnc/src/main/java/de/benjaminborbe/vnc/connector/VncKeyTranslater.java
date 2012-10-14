package de.benjaminborbe.vnc.connector;

import com.glavsoft.utils.Keymap;
import com.google.inject.Inject;

import de.benjaminborbe.vnc.api.VncKey;

public class VncKeyTranslater {

	@Inject
	public VncKeyTranslater() {
		super();
	}

	public int translate(final VncKey vncKey) {

		final String name = vncKey.name();
		final int c = name.charAt(0);

		return Keymap.unicode2keysym(c);
	}
}
