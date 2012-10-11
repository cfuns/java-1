package de.benjaminborbe.authentication.user;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class UserBeanMapper extends BaseMapper<UserBean> {

	private static final String ID = "id";

	private static final String PASSWORD = "password";

	private static final String EMAIL = "email";

	private static final String FULLNAME = "fullname";

	@Inject
	public UserBeanMapper(final Provider<UserBean> provider) {
		super(provider);
	}

	@Override
	public void map(final UserBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(PASSWORD, object.getPassword());
		data.put(EMAIL, object.getEmail());
		data.put(FULLNAME, object.getFullname());
	}

	@Override
	public void map(final Map<String, String> data, final UserBean object) throws MapException {
		object.setId(toUserIdentifier(data.get(ID)));
		object.setPassword(data.get(PASSWORD));
		object.setEmail(data.get(EMAIL));
		object.setFullname(data.get(FULLNAME));
	}

	private UserIdentifier toUserIdentifier(final String id) {
		return id != null ? new UserIdentifier(id) : null;
	}

	private String toString(final UserIdentifier id) {
		return id != null ? id.getId() : null;
	}
}
