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
		// extern
		save(createBookmark("https://console.aws.amazon.com/ec2/home", "Amazon EC2"));
		save(createBookmark("/bb/monitoring", "Monitoring"));

		// 20ft devel
		save(createBookmark("/bb/twentyfeetperformance", "Twentyfeet - Devel - Performance"));
		save(createBookmark("http://localhost:8180/app/", "Twentyfeet - Devel"));
		save(createBookmark("http://0.0.0.0:8161/admin/queues.jsp", "Twentyfeet - Devel - ActiveMQ - JMS"));
		save(createBookmark("http://localhost:8180/app/?log_level=DEBUG", "Twentyfeet - Devel - App with Debug"));
		save(createBookmark("http://127.0.0.1:8888/app/Home.html?gwt.codesvr=127.0.0.1:9997", "Twentyfeet - Devel - App in Developermode"));

		// 20ft live
		save(createBookmark("https://www.twentyfeet.com/", "Twentyfeet - Live"));
		save(createBookmark("https://www.twentyfeet.com/admin/queues.jsp", "Twentyfeet - Live - ActiveMQ - JMS"));
		save(createBookmark("https://central.twentyfeet.com/phpmyadmin/", "Twentyfeet - Live - phpMyadmin"));

		// 20ft test
		save(createBookmark("https://test.twentyfeet.com/", "Twentyfeet - Test"));
		save(createBookmark("https://test.twentyfeet.com/admin/queues.jsp", "Twentyfeet - Test - ActiveMQ - JMS"));

		// seibert-media
		save(createBookmark("https://timetracker.rp.seibert-media.net/", "Seibert-Media - Timetracker"));
		save(createBookmark("https://confluence.rp.seibert-media.net/", "Seibert-Media - Wiki"));
		save(createBookmark("https://kunden.seibert-media.net/", "Seibert-Media - Kunden-Wiki"));
	}

	protected Bookmark createBookmark(final String url, final String name) {
		final Bookmark bookmark = create();
		bookmark.setUrl(url);
		bookmark.setName(name);
		return bookmark;
	}

}
