package de.benjaminborbe.wow.account;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class WowAccountBean implements Entity<WowAccountIdentifier>, WowAccount {

	private static final long serialVersionUID = 4613788971187238396L;

	private String account;

	private String password;

	private String email;

	private WowAccountIdentifier id;

	private UserIdentifier owner;

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getAccount() {
		return account;
	}

	public void setAccount(final String account) {
		this.account = account;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Override
	public WowAccountIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final WowAccountIdentifier id) {
		this.id = id;
	}

	@Override
	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

}
