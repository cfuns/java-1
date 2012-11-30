package de.benjaminborbe.tools.password;

import java.util.HashSet;
import java.util.Set;

public enum PasswordCharacter {

	LOWER('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'),
	UPPER('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'),
	SPECIAL('!', '-', '_', '.', ';'),
	NUMBER('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

	private final char[] characters;

	private final Set<Character> characterSet = new HashSet<Character>();

	private PasswordCharacter(final char... characters) {
		this.characters = characters;
		for (final char c : characters) {
			characterSet.add(c);
		}
	}

	public char[] getCharacters() {
		return characters;
	}

	public boolean containsChar(final char c) {
		return characterSet.contains(c);
	}
}
