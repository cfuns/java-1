package de.benjaminborbe.bookmark.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

import com.google.inject.Provider;

import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.util.ParseUtil;

public class BookmarkBeanMapperUnitTest {

	@Test
	public void testId() throws Exception {

		final List<List<String>> values = new ArrayList<List<String>>();
		values.add(new ArrayList<String>());
		values.add(Arrays.asList("a"));
		values.add(Arrays.asList("a", "b"));
		for (final List<String> value : values) {

			final BookmarkBeanMapper mapper = getBookmarkBeanMapper();
			final String fieldname = "keywords";
			{
				final BookmarkBean bean = new BookmarkBean();
				bean.setKeywords(value);
				final Map<String, String> data = mapper.map(bean);
				assertEquals(data.get(fieldname), value.isEmpty() ? null : StringUtil.join(value, ","));
			}
			{
				final Map<String, String> data = new HashMap<String, String>();
				data.put(fieldname, value.isEmpty() ? null : StringUtil.join(value, ","));
				final BookmarkBean bean = mapper.map(data);
				assertEquals(value.size(), bean.getKeywords().size());
				assertEquals(StringUtils.join(value, ","), StringUtils.join(bean.getKeywords(), ","));
			}
		}
	}

	private BookmarkBeanMapper getBookmarkBeanMapper() {
		final Provider<BookmarkBean> a = new ProviderMock<BookmarkBean>(new BookmarkBean());
		final ParseUtil b = EasyMock.createNiceMock(ParseUtil.class);
		EasyMock.replay(b);
		return new BookmarkBeanMapper(a, b);
	}
}
