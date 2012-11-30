package de.benjaminborbe.tools.password;

public interface PasswordGenerator {

	String generatePassword(int length, PasswordCharacter... characters);
}
