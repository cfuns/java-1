package de.benjaminborbe.tools.password;

import java.util.Collection;

public interface PasswordGenerator {

	String generatePassword(int length, PasswordCharacter... characters);

	String generatePassword(int length, Collection<PasswordCharacter> characters);
}
