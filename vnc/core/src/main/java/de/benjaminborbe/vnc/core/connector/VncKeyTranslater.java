package de.benjaminborbe.vnc.core.connector;

import com.glavsoft.utils.Keymap;
import de.benjaminborbe.vnc.api.VncKey;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class VncKeyTranslater {

	private final Map<VncKey, Integer> map = new HashMap<>();

	@Inject
	public VncKeyTranslater() {
		put(VncKey.K_0, Keymap.K_KP_0);
		put(VncKey.K_1, Keymap.K_KP_1);
		put(VncKey.K_2, Keymap.K_KP_2);
		put(VncKey.K_3, Keymap.K_KP_3);
		put(VncKey.K_4, Keymap.K_KP_4);
		put(VncKey.K_5, Keymap.K_KP_5);
		put(VncKey.K_6, Keymap.K_KP_6);
		put(VncKey.K_7, Keymap.K_KP_7);
		put(VncKey.K_8, Keymap.K_KP_8);
		put(VncKey.K_9, Keymap.K_KP_9);
		put(VncKey.K_ENTER, Keymap.K_ENTER);
		put(VncKey.K_SPACE, Keymap.K_KP_SPACE);
		put(VncKey.K_UP, Keymap.K_UP);
		put(VncKey.K_DOWN, Keymap.K_DOWN);
		put(VncKey.K_LEFT, Keymap.K_LEFT);
		put(VncKey.K_RIGHT, Keymap.K_RIGHT);
		put(VncKey.K_SLASH, Keymap.K_KP_DIVIDE);
		put(VncKey.K_ALT_LEFT, Keymap.K_ALT_LEFT);
		put(VncKey.K_F4, Keymap.K_F4);
	}

	private void put(final VncKey key, final int value) {
		map.put(key, value);
	}

	public int translate(final VncKey vncKey) throws VncKeyTranslaterException {
		if (vncKey == null) {
			throw new VncKeyTranslaterException("can't translate null");
		}
		final String name = vncKey.name();
		if (name.indexOf("K_") == 0) {
			if (map.containsKey(vncKey)) {
				return map.get(vncKey);
			} else {
				throw new VncKeyTranslaterException("can't translate " + vncKey.name());
			}
		} else {
			final int c = name.charAt(0);
			return Keymap.unicode2keysym(c);
		}
	}
}
