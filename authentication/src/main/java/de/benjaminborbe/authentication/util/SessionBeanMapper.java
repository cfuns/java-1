package de.benjaminborbe.authentication.util;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

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
		data.put(CURRENTUSER, object.getCurrentUser());
		data.put(ID, object.getId());
	}

	@Override
	public void map(final Map<String, String> data, final SessionBean object) throws MapException {
		object.setId(data.get(ID));
		object.setCurrentUser(data.get(CURRENTUSER));
	}

}
