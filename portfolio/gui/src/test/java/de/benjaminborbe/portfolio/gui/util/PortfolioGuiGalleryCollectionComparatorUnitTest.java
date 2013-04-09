package de.benjaminborbe.portfolio.gui.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;

public class PortfolioGuiGalleryCollectionComparatorUnitTest {

	@Test
	public void testSort() throws Exception {
		final PortfolioGuiGalleryCollectionComparatorName galleryComparatorName = new PortfolioGuiGalleryCollectionComparatorName();
		final PortfolioGuiGalleryCollectionComparatorPrio galleryComparatorPrio = new PortfolioGuiGalleryCollectionComparatorPrio();
		final PortfolioGuiGalleryCollectionComparator galleryComparator = new PortfolioGuiGalleryCollectionComparator(galleryComparatorName, galleryComparatorPrio);

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
			assertEquals("a", list.get(0).getName());
			assertEquals("b", list.get(1).getName());
			assertEquals("c", list.get(2).getName());
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

	private GalleryCollection build(final String name, final Long prio) {
		return new GalleryCollection() {

			@Override
			public Boolean getShared() {
				return null;
			}

			@Override
			public Long getPriority() {
				return prio;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public Calendar getModified() {
				return null;
			}

			@Override
			public GalleryCollectionIdentifier getId() {
				return null;
			}

			@Override
			public GalleryGroupIdentifier getGroupId() {
				return null;
			}

			@Override
			public Calendar getCreated() {
				return null;
			}

			@Override
			public String toString() {
				return name + " " + prio;
			}

		};
	}
}
