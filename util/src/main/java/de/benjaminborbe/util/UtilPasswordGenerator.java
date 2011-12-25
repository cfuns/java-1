package de.benjaminborbe.util;

public interface UtilPasswordGenerator {

	String generatePassword(int length, UtilPasswordCharacter... characters);
}
