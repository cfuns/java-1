package de.benjaminborbe.wow.core.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;

public interface WowAccount {

	WowAccountIdentifier getId();

	String getEmail();

	String getPassword();

	String getAccount();

	UserIdentifier getOwner();
}
