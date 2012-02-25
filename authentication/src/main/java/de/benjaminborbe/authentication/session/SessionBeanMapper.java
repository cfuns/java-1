package de.benjaminborbe.authentication.session;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class SessionBeanMapper extends BaseMapper<SessionBean> {

	private static final String CURRENTUSER = "currentUser";

	private static final String ID = "id";

	@Inject
	public SessionBeanMapper(final Provider<SessionBean> provider) {
		super(provider);
	}

	@Override
	public void map(final SessionBean object, final Map<String, String> data) throws MapException {
		data.put(CURRENTUSER, object.getCurrentUser() != null ? object.getCurrentUser().getId() : null);
		data.put(ID, object.getId() != null ? object.getId().getId() : null);
	}

	@Override
	public void map(final Map<String, String> data, final SessionBean object) throws MapException {
		object.setId(data.get(ID) != null ? new SessionIdentifier(data.get(ID)) : null);
		object.setCurrentUser(data.get(CURRENTUSER) != null ? new UserIdentifier(data.get(CURRENTUSER)) : null);
	}

}
