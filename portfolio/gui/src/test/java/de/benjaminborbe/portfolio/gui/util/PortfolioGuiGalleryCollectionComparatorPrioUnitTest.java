package de.benjaminborbe.portfolio.gui.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.gallery.api.GalleryCollection;

public class PortfolioGuiGalleryCollectionComparatorPrioUnitTest {

	@Test
	public void testSort() throws Exception {
		final PortfolioGuiGalleryCollectionComparatorPrio galleryComparatorPrio = new PortfolioGuiGalleryCollectionComparatorPrio();

		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			Collections.sort(list, galleryComparatorPrio);
		}

		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			list.add(build("a", 1l));
			list.add(build("b", 2l));
			list.add(build("c", 3l));
			Collections.sort(list, galleryComparatorPrio);
			assertEquals("c", list.get(0).getName());
			assertEquals("b", list.get(1).getName());
			assertEquals("a", list.get(2).getName());
		}

		{
			final List<GalleryCollection> list = new ArrayList<GalleryCollection>();
			list.add(build("a", 1l));
			list.add(build("b", 2l));
			list.add(build("c", null));
			Collections.sort(list, galleryComparatorPrio);
			assertEquals("b", list.get(0).getName());
			assertEquals("a", list.get(1).getName());
			assertEquals("c", list.get(2).getName());
		}
	}

	private GalleryCollection build(final String string, final Long l) {
		final GalleryCollection galleryCollection = EasyMock.createMock(GalleryCollection.class);
		EasyMock.expect(galleryCollection.getPriority()).andReturn(l).anyTimes();
		EasyMock.expect(galleryCollection.getName()).andReturn(string).anyTimes();
		EasyMock.replay(galleryCollection);
		return galleryCollection;
	}
}
