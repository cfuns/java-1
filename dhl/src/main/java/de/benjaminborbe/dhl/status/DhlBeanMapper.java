package de.benjaminborbe.dhl.status;

import java.util.Map;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.Inject;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class DhlBeanMapper extends BaseMapper<DhlBean> {

	private static final String ID = "id";

	private static final String TRACKINGNUMBER = "trackingnumber";

	private static final String ZIP = "zip";

	private final ParseUtil parseUtil;

	@Inject
	public DhlBeanMapper(final Provider<DhlBean> provider, final ParseUtil parseUtil) {
		super(provider);
		this.parseUtil = parseUtil;
	}

	@Override
	public void map(final DhlBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(TRACKINGNUMBER, toString(object.getTrackingNumber()));
		data.put(ZIP, toString(object.getZip()));
	}

	@Override
	public void map(final Map<String, String> data, final DhlBean object) throws MapException {
		try {
			object.setId(toDhlIdentifier(data.get(ID)));
			object.setTrackingNumber(parseUtil.parseLong(data.get(TRACKINGNUMBER)));
			object.setZip(parseUtil.parseLong(data.get(ZIP)));
		}
		catch (final ParseException e) {
			throw new MapException(e.getClass().getSimpleName(), e);
		}
	}

	private DhlIdentifier toDhlIdentifier(final String string) {
		return new DhlIdentifier(string);
	}

	private String toString(final DhlIdentifier id) {
		return id != null ? id.getId() : null;
	}

	private String toString(final long number) {
		return String.valueOf(number);
	}
}
