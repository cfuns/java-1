package de.benjaminborbe.tools.mapper;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.date.CurrentTime;

public class SingleMapStringListUnitTest {

	public class T {

		private List<String> field;

		public List<String> getField() {
			return field;
		}

		public void setField(final List<String> field) {
			this.field = field;
		}

	}

	@Test
	public void testMap() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final String name = "field";

		{
			final List<String> values = Arrays.asList("a", "b");
			final SingleMapStringList<T> map = new SingleMapStringList<T>(name);
			final String valuesString = map.toString(values);
			assertEquals(values, map.fromString(valuesString));
		}
		{
			final List<String> values = null;
			final SingleMapStringList<T> map = new SingleMapStringList<T>(name);
			final String valuesString = map.toString(values);
			assertEquals(values, map.fromString(valuesString));
		}
		// TODO bborbe: , escape
		// {
		// final List<String> values = Arrays.asList("a", "b,c");
		// final SingleMapStringList<T> map = new SingleMapStringList<T>(name);
		// final String valuesString = map.toString(values);
		// assertEquals(values, map.fromString(valuesString));
		// }
	}
}
