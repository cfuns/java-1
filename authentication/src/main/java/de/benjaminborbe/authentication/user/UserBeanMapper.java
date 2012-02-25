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

	@Inject
	public UserBeanMapper(final Provider<UserBean> provider) {
		super(provider);
	}

	@Override
	public void map(final UserBean object, final Map<String, String> data) {
		data.put("id", toString(object.getId()));
	}

	@Override
	public void map(final Map<String, String> data, final UserBean object) throws MapException {
		object.setId(toUserIdentifier(data.get("id")));
	}

	private UserIdentifier toUserIdentifier(final String id) {
		return id != null ? new UserIdentifier(id) : null;
	}

	private String toString(final UserIdentifier id) {
		return id != null ? id.getId() : null;
	}
}
