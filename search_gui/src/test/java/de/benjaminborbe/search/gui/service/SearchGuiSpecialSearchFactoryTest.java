package de.benjaminborbe.search.gui.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.search.api.SearchSpecial;

public class SearchGuiSpecialSearchFactoryTest {

	@Test
	public void testFoo() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final SearchSpecial specialSearchG = EasyMock.createMock(SearchSpecial.class);
		EasyMock.expect(specialSearchG.getName()).andReturn("g").anyTimes();
		EasyMock.replay(specialSearchG);

		final SearchSpecial specialSearchB = EasyMock.createMock(SearchSpecial.class);
		EasyMock.expect(specialSearchB.getName()).andReturn("b").anyTimes();
		EasyMock.replay(specialSearchB);

		final Collection<SearchSpecial> list = Arrays.asList(specialSearchB, specialSearchG);

		final SearchGuiSpecialSearchRegistry searchGuiSpecialSearchRegistry = EasyMock.createMock(SearchGuiSpecialSearchRegistry.class);
		EasyMock.expect(searchGuiSpecialSearchRegistry.getAll()).andReturn(list).anyTimes();
		EasyMock.replay(searchGuiSpecialSearchRegistry);

		final SearchGuiSpecialSearchFactory searchGuiSpecialSearchFactory = new SearchGuiSpecialSearchFactoryImpl(logger, searchGuiSpecialSearchRegistry);

		assertNull(searchGuiSpecialSearchFactory.findSpecial("asdf"));
		assertEquals(specialSearchG, searchGuiSpecialSearchFactory.findSpecial("g:"));
		assertEquals(specialSearchG, searchGuiSpecialSearchFactory.findSpecial("g: bla"));
		assertEquals(specialSearchB, searchGuiSpecialSearchFactory.findSpecial("b:"));
		assertEquals(specialSearchB, searchGuiSpecialSearchFactory.findSpecial("b: bla"));
		assertNull(searchGuiSpecialSearchFactory.findSpecial("c:"));
	}
}
