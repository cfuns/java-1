package de.benjaminborbe.index.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.dao.DaoBase;
import de.benjaminborbe.tools.util.IdGenerator;

@Singleton
public class BookmarkDaoImpl extends DaoBase<Bookmark> implements BookmarkDao {

	@Inject
	public BookmarkDaoImpl(final Logger logger, final IdGenerator idGenerator, final Provider<Bookmark> provider) {
		super(logger, idGenerator, provider);
	}

	@Override
	protected void init() {
		save(createBookmark("/bb/twentyfeetperformance", "Devel - Twentyfeet - Performance"));
		save(createBookmark("http://localhost:8180/app/", "Devel - Twentyfeet"));
		save(createBookmark("http://0.0.0.0:8161/admin/queues.jsp", "Devel - Twentyfeet - ActiveMQ - JMS"));
		save(createBookmark("http://localhost:8180/app/?log_level=DEBUG", "Devel - Twentyfeet with Debug"));
		save(createBookmark("http://127.0.0.1:8888/app/Home.html?gwt.codesvr=127.0.0.1:9997", "Devel - Twentyfeet - GWT"));
		save(createBookmark("https://timetracker.rp.seibert-media.net/", "Live - Timetracker"));
		save(createBookmark("https://www.twentyfeet.com/", "Live - Twentyfeet"));
		save(createBookmark("https://www.twentyfeet.com/admin/queues.jsp", "Live - Twentyfeet - ActiveMQ - JMS"));
		save(createBookmark("https://test.twentyfeet.com/", "Test - Twentyfeet"));
		save(createBookmark("https://test.twentyfeet.com/admin/queues.jsp", "Test - Twentyfeet - ActiveMQ - JMS"));
		save(createBookmark("https://console.aws.amazon.com/ec2/home", "Amazon EC2"));
		save(createBookmark("https://central.twentyfeet.com/phpmyadmin/", "Live - Twentyfeet - phpMyadmin"));
	}

	protected Bookmark createBookmark(final String url, final String name) {
		final Bookmark bookmark = create();
		bookmark.setUrl(url);
		bookmark.setName(name);
		return bookmark;
	}

}
