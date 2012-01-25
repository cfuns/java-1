package de.benjaminborbe.bookmark.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.dao.DaoCache;
import de.benjaminborbe.tools.util.IdGenerator;

@Singleton
public class BookmarkDaoImpl extends DaoCache<BookmarkBean> implements BookmarkDao {

	private final class BookmarkFavoritePredicate implements Predicate<BookmarkBean> {

		@Override
		public boolean apply(final BookmarkBean bookmark) {
			return bookmark.isFavorite();
		}
	}

	private static final String DEFAULT_DESCRIPTION = "-";

	@Inject
	public BookmarkDaoImpl(final Logger logger, final IdGenerator idGenerator, final Provider<BookmarkBean> provider) {
		super(logger, idGenerator, provider);
	}

	@Override
	protected void init() {
		// internal
		save(createBookmark("/bb/bookmark", "Local - BB - Bookmarks", Arrays.asList("bookmark", "lesezeichen")));
		save(createBookmark("/bb/worktime", "Local - BB - Worktimes"));
		save(createBookmark("/bb/util/passwordGenerator", "Local - BB - PasswordGenerator"));
		save(createBookmark("/bb/monitoring", "Local - BB - Monitoring"));
		save(createBookmark("/bb/gwt/Home.html", "Local - BB - GWT"));
		save(createBookmark("/bb/search", "Local - BB - Search"));
		save(createBookmark("/bb/search/components", "Local - BB - Search Components"));
		save(createBookmark("/bb/performance", "Local - BB - Performance"));
		save(createBookmark("/bb/crawler", "Local - BB - Crawler"));

		// extern
		save(createBookmark("https://console.aws.amazon.com/ec2/home", "Amazon EC2"));
		save(createBookmark("http://kleinanzeigen.ebay.de/", "Ebay Kleinanzeigen"));
		save(createBookmark("http://confluence.rocketnews.de", "Rocketnews - Confluence - Wiki", Arrays.asList("wiki", "confluence")));
		save(createBookmark("http://www.harteslicht.com", "Harteslicht", Arrays.asList("foto", "photo", "photography", "fotografie")));
		save(createBookmark("http://www.benjamin-borbe.de", "Benjamin Borbe", Arrays.asList("foto", "photo", "photography", "fotografie", "portfolio")));
		save(createBookmark("http://tomtom.de/gettingstarted", "TomTom"));
		save(createBookmark("http://www.guenstiger.de", "Guenstiger", Arrays.asList("search")));
		save(createBookmark("http://geizhals.at/deutschland/", "Geizhals", Arrays.asList("search")));
		save(createBookmark("http://www.google.de/?hl=en", "Google"));
		save(createBookmark("http://www.audible.de/", "Audible - Hörbücher", Arrays.asList("Hörbücher", "Hoerbuecher", "shop")));
		save(createBookmark("http://www.dwitte.de/", "Dennis Witte", Arrays.asList("Dennis Witte", "dwitte")));
		save(createBookmark("http://wuhrsteinalm.de/", "Wuhrsteinalm"));
		save(createBookmark("http://www.postbank.de/", "Postbank", Arrays.asList("bank")));
		save(createBookmark("https://banking.dkb.de/", "DKB", Arrays.asList("bank")));
		save(createBookmark("http://www.tagesschau.de/", "Tagesschau", Arrays.asList("Tagesschau")));
		save(createBookmark("http://www.tagesschau.de/100sekunden/", "Tagesschau in 100 Sekunden", Arrays.asList("Tagesschau")));
		save(createBookmark("http://filestube.com", "Filestube", Arrays.asList("Download")));
		save(createBookmark("http://store.steampowered.com/", "Steam - Store", Arrays.asList("Steam", "Shop", "Store")));
		save(createBookmark("http://www.rocketnews.de/manager/html/list", "Rocketnews - Tomcat - Manager", Arrays.asList("Tomcat", "Manager")));
		save(createBookmark("https://www.icloud.com/", "iCloud", Arrays.asList("iCloud", "Apple")));
		save(createBookmark("https://www.icloud.com/#find", "iCloud - Find", Arrays.asList("iCloud", "Find", "Apple")));
		save(createBookmark("http://dict.leo.org/", "dict.leo.org", Arrays.asList("Dictionary", "Translate")));

		// az
		save(createBookmark("https://code.allianz24.de/hudson/", "Allianz24 - Hudson / Jenkins", Arrays.asList("Hudson", "Jenkins", "Seibert-Media", "Allianz24", "Allsecur"), true));

		// local
		save(createBookmark("http://127.0.0.1:8180/sonar", "Local - Sonar"));
		save(createBookmark("http://127.0.0.1:8180/manager/html/list", "Local - Tomcat Manager"));
		save(createBookmark("http://127.0.0.1:8180/jenkins", "Local - Jenkins", Arrays.asList("Local", "Lokal", "Hudson", "Jenkins")));
		save(createBookmark("http://phpmyadmin/", "Local - phpMyAdmin"));
		save(createBookmark("http://0.0.0.0:8161/admin/queues.jsp", "Local - ActiveMQ - JMS"));

		// 20ft devel
		save(createBookmark("/bb/twentyfeetperformance", "Twentyfeet - Devel - Performance", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));
		save(createBookmark("http://20ft/", "Twentyfeet - Devel", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));
		save(createBookmark("http://20ft/app/admin", "Twentyfeet - Devel - Admin", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));
		save(createBookmark("http://20ft/app/?log_level=DEBUG", "Twentyfeet - Devel - App with Debug", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));
		save(createBookmark("http://20ft/app/Home.html?gwt.codesvr=127.0.0.1:9997", "Twentyfeet - Devel - App in Developermode", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));

		// 20ft live
		save(createBookmark("https://www.twentyfeet.com/", "Twentyfeet - Live", Arrays.asList("TwentyFeet", "20ft", "Live")));
		save(createBookmark("https://www.twentyfeet.com/admin/queues.jsp", "Twentyfeet - Live - ActiveMQ - JMS", Arrays.asList("TwentyFeet", "20ft", "Live"), true));
		save(createBookmark("https://central.twentyfeet.com/phpmyadmin/", "Twentyfeet - Live - phpMyadmin", Arrays.asList("TwentyFeet", "20ft", "Live")));
		save(createBookmark("https://kunden.seibert-media.net/display/20ft", "Twentyfeet - Live - Wiki", Arrays.asList("TwentyFeet", "20ft", "Live"), true));

		// 20ft test
		save(createBookmark("https://test.twentyfeet.com/", "Twentyfeet - Test", Arrays.asList("TwentyFeet", "20ft", "Test")));
		save(createBookmark("https://test.twentyfeet.com/admin/queues.jsp", "Twentyfeet - Test - ActiveMQ - JMS", Arrays.asList("TwentyFeet", "20ft", "Test")));

		// seibert-media
		save(createBookmark("https://timetracker.rp.seibert-media.net/", "Seibert-Media - Timetracker", Arrays.asList("Seibert-Media", "SM"), true));
		save(createBookmark("https://confluence.rp.seibert-media.net/", "Seibert-Media - Confluence - Wiki", Arrays.asList("Seibert-Media", "SM"), true));
		save(createBookmark("https://hudson.rp.seibert-media.net/", "Seibert-Media - Hudson / Jenkins", Arrays.asList("Hudson", "Jenkins", "Seibert-Media", "SM"), true));
		save(createBookmark("https://micro.rp.seibert-media.net", "Seibert-Media - Microblog", Arrays.asList("Seibert-Media", "SM")));
		save(createBookmark("http://nexus.rp.seibert-media.net/", "Seibert-Media - Nexus", Arrays.asList("Seibert-Media", "SM")));
		save(createBookmark("https://zimbra.rp.seibert-media.net/", "Seibert-Media - Zimbra", Arrays.asList("Seibert-Media", "SM")));
		save(createBookmark("https://projectile.rp.seibert-media.net/", "Seibert-Media - Projectile", Arrays.asList("Seibert-Media", "SM")));

		// Movie
		save(createBookmark("http://www.cinestar.de/de/kino/mainz-cinestar/", "Movie - Kino - Mainz - Cinestar", Arrays.asList("Movie", "Film")));
		save(createBookmark("http://www.filmstarts.de/", "Movie - Review - Filmstarts", Arrays.asList("Movie", "Film")));
		save(createBookmark("http://rogerebert.suntimes.com/", "Movie - Review - Roger Ebert", Arrays.asList("Movie", "Film")));
		save(createBookmark("http://imdb.com/", "Movie - Review - Imdb", Arrays.asList("Movie", "Film")));
	}

	protected BookmarkBean createBookmark(final String url, final String name) {
		return createBookmark(url, name, false);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final List<String> keywords) {
		return createBookmark(url, name, keywords, false);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final String description, final List<String> keywords) {
		return createBookmark(url, name, description, keywords, false);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final boolean favorite) {
		return createBookmark(url, name, DEFAULT_DESCRIPTION, new ArrayList<String>(), favorite);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final List<String> keywords, final boolean favorite) {
		return createBookmark(url, name, DEFAULT_DESCRIPTION, keywords, favorite);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final String description, final List<String> keywords, final boolean favorite) {
		final BookmarkBean bookmark = create();
		bookmark.setUrl(url);
		bookmark.setName(name);
		bookmark.setDescription(description);
		bookmark.setKeywords(keywords);
		bookmark.setFavorite(favorite);
		return bookmark;
	}

	@Override
	public Collection<BookmarkBean> getFavorites() {
		final Predicate<BookmarkBean> filter = new BookmarkFavoritePredicate();
		return Collections2.filter(getAll(), filter);
	}

}
