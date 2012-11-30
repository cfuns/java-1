package de.benjaminborbe.tools.password;

public interface PasswordValidator {

	boolean hasLength(String password, int minLength);

	boolean hasValidChars(String password);

	boolean hasDigest(String password, int amount);

	boolean hasLowerCharacter(String password, int amount);

	boolean hasUpperCharacter(String password, int amount);

	boolean hasSpecialCharacter(String password, int amount);

}
