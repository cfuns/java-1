package de.benjaminborbe.gallery.image;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.Base64Util;

@Singleton
public class GalleryImageBeanMapper extends BaseMapper<GalleryImageBean> {

	private static final String ID = "id";

	private static final String NAME = "name";

	private static final String CONTENT = "content";

	private static final String CONTENT_TYPE = "contentType";

	private final Base64Util base64Util;

	@Inject
	public GalleryImageBeanMapper(final Provider<GalleryImageBean> provider, final Base64Util base64Util) {
		super(provider);
		this.base64Util = base64Util;
	}

	@Override
	public void map(final GalleryImageBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(NAME, object.getName());
		data.put(CONTENT_TYPE, object.getContentType());
		data.put(CONTENT, toString(object.getContent()));
	}

	@Override
	public void map(final Map<String, String> data, final GalleryImageBean object) throws MapException {
		object.setId(toGalleryImageIdentifier(data.get(ID)));
		object.setName(data.get(NAME));
		object.setContentType(data.get(CONTENT_TYPE));
		object.setContent(toByteArray(data.get(CONTENT)));
	}

	private byte[] toByteArray(final String content) {
		return base64Util.decode(content);
	}

	private String toString(final byte[] content) {
		return base64Util.encode(content);
	}

	private GalleryImageIdentifier toGalleryImageIdentifier(final String id) {
		return id != null ? new GalleryImageIdentifier(id) : null;
	}

	private String toString(final GalleryImageIdentifier id) {
		return id != null ? id.getId() : null;
	}
}
