package de.benjaminborbe.portfolio.gui.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.gallery.api.GalleryCollection;

public class GalleryComparatorUnitTest {

	@Test
	public void testSort() throws Exception {
		final GalleryComparatorName galleryComparatorName = new GalleryComparatorName();
		final GalleryComparatorPrio galleryComparatorPrio = new GalleryComparatorPrio();
		final GalleryComparator galleryComparator = new GalleryComparator(galleryComparatorName, galleryComparatorPrio);

		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			Collections.sort(list, galleryComparator);
		}

		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			list.add(build("name", 1l));
			Collections.sort(list, galleryComparator);
		}
		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			list.add(build("a", 1l));
			list.add(build("c", 1l));
			list.add(build("b", 1l));
			Collections.sort(list, galleryComparator);
			assertEquals("a", list.get(0).getName());
			assertEquals("b", list.get(1).getName());
			assertEquals("c", list.get(2).getName());
		}
		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			list.add(build("a", null));
			list.add(build("c", null));
			list.add(build("b", null));
			Collections.sort(list, galleryComparator);
			assertEquals("a", list.get(0).getName());
			assertEquals("b", list.get(1).getName());
			assertEquals("c", list.get(2).getName());
		}
		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			list.add(build("a", 2l));
			list.add(build("c", 1l));
			list.add(build("b", 2l));
			Collections.sort(list, galleryComparator);
			assertEquals("c", list.get(0).getName());
			assertEquals("a", list.get(1).getName());
			assertEquals("b", list.get(2).getName());
		}
		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			list.add(build("a", null));
			list.add(build("c", 1l));
			list.add(build("b", null));
			Collections.sort(list, galleryComparator);
			assertEquals("c", list.get(0).getName());
			assertEquals("a", list.get(1).getName());
			assertEquals("b", list.get(2).getName());
		}
	}

	private GalleryCollection build(final String string, final Long l) {
		final GalleryCollection galleryCollection = EasyMock.createMock(GalleryCollection.class);
		EasyMock.expect(galleryCollection.getName()).andReturn(string).anyTimes();
		EasyMock.expect(galleryCollection.getPriority()).andReturn(l).anyTimes();
		EasyMock.replay(galleryCollection);
		return galleryCollection;
	}
}
