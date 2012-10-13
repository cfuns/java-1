package de.benjaminborbe.wow.account;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class WowAccountBeanMapper extends BaseMapper<WowAccountBean> {

	private static final String ID = "id";

	private static final String EMAIL = "email";

	private static final String PASSWORD = "password";

	private static final String ACCOUNT_NAME = "account_name";

	private static final String OWNER = "author";

	@Inject
	public WowAccountBeanMapper(final Provider<WowAccountBean> provider) {
		super(provider);
	}

	@Override
	public void map(final WowAccountBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(EMAIL, object.getEmail());
		data.put(PASSWORD, object.getPassword());
		data.put(ACCOUNT_NAME, object.getAccount());
		data.put(OWNER, toString(object.getOwner()));
	}

	@Override
	public void map(final Map<String, String> data, final WowAccountBean object) throws MapException {
		object.setId(toWowAccountIdentifier(data.get(ID)));
		object.setEmail(data.get(EMAIL));
		object.setPassword(data.get(PASSWORD));
		object.setAccount(data.get(ACCOUNT_NAME));
		object.setOwner(toUserIdentifier(data.get(OWNER)));
	}

	private WowAccountIdentifier toWowAccountIdentifier(final String id) {
		return id != null ? new WowAccountIdentifier(id) : null;
	}

	private UserIdentifier toUserIdentifier(final String id) {
		return id != null ? new UserIdentifier(id) : null;
	}

	private String toString(final WowAccountIdentifier id) {
		return id != null ? id.getId() : null;
	}

	private String toString(final UserIdentifier creator) {
		return creator != null ? creator.getId() : null;
	}

}
