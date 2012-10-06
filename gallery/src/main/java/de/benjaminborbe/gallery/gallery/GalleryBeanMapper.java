package de.benjaminborbe.gallery.gallery;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class GalleryBeanMapper extends BaseMapper<GalleryBean> {

	private static final String ID = "id";

	private static final String NAME = "name";

	@Inject
	public GalleryBeanMapper(final Provider<GalleryBean> provider) {
		super(provider);
	}

	@Override
	public void map(final GalleryBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(NAME, object.getName());
	}

	@Override
	public void map(final Map<String, String> data, final GalleryBean object) throws MapException {
		object.setId(toGalleryIdentifier(data.get(ID)));
		object.setName(data.get(NAME));
	}

	private GalleryIdentifier toGalleryIdentifier(final String id) {
		return id != null ? new GalleryIdentifier(id) : null;
	}

	private String toString(final GalleryIdentifier id) {
		return id != null ? id.getId() : null;
	}
}
