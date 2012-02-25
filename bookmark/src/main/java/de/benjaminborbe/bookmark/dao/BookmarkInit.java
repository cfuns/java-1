package de.benjaminborbe.bookmark.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.storage.api.StorageException;

public class BookmarkInit {

	private static final String DEFAULT_DESCRIPTION = "-";

	private final BookmarkDao bookmarkDao;

	@Inject
	public BookmarkInit(final BookmarkDao bookmarkDao) {
		this.bookmarkDao = bookmarkDao;
	}

	public void init() throws StorageException {
		// internal
		bookmarkDao.save(createBookmark("/bb/bookmark", "Local - BB - Bookmarks", Arrays.asList("bookmark", "lesezeichen")));
		bookmarkDao.save(createBookmark("/bb/worktime", "Local - BB - Worktimes"));
		bookmarkDao.save(createBookmark("/bb/util/passwordGenerator", "Local - BB - PasswordGenerator"));
		bookmarkDao.save(createBookmark("/bb/monitoring", "Local - BB - Monitoring"));
		bookmarkDao.save(createBookmark("/bb/gwt/Home.html", "Local - BB - GWT"));
		bookmarkDao.save(createBookmark("/bb/search", "Local - BB - Search"));
		bookmarkDao.save(createBookmark("/bb/search/components", "Local - BB - Search Components"));
		bookmarkDao.save(createBookmark("/bb/performance", "Local - BB - Performance"));
		bookmarkDao.save(createBookmark("/bb/crawler", "Local - BB - Crawler"));
		bookmarkDao.save(createBookmark("/bb/configuration", "Local - BB - Configuration"));
		bookmarkDao.save(createBookmark("/bb/authentication/login", "Local - BB - Login"));
		bookmarkDao.save(createBookmark("/bb/websearch/list", "Local - BB - Websearch - Pages"));
		bookmarkDao.save(createBookmark("/bb/websearch/refresh", "Local - BB - Websearch - Refresh"));

		// extern
		bookmarkDao.save(createBookmark("https://console.aws.amazon.com/ec2/home", "Amazon EC2"));
		bookmarkDao.save(createBookmark("http://kleinanzeigen.ebay.de/", "Ebay Kleinanzeigen"));
		bookmarkDao.save(createBookmark("http://confluence.benjamin-borbe.de", "Benjamin Borbe - Confluence - Wiki", Arrays.asList("wiki", "confluence", "rocketnews")));
		bookmarkDao.save(createBookmark("http://www.harteslicht.com", "Harteslicht", Arrays.asList("foto", "photo", "photography", "fotografie")));
		bookmarkDao.save(createBookmark("http://www.benjamin-borbe.de", "Benjamin Borbe", Arrays.asList("foto", "photo", "photography", "fotografie", "portfolio")));
		bookmarkDao.save(createBookmark("http://tomtom.de/gettingstarted", "TomTom"));
		bookmarkDao.save(createBookmark("http://www.guenstiger.de", "Guenstiger", Arrays.asList("search")));
		bookmarkDao.save(createBookmark("http://geizhals.at/deutschland/", "Geizhals", Arrays.asList("search")));
		bookmarkDao.save(createBookmark("http://www.google.de/?hl=en", "Google"));
		bookmarkDao.save(createBookmark("http://www.audible.de/", "Audible - Hörbücher", Arrays.asList("Hörbücher", "Hoerbuecher", "shop")));
		bookmarkDao.save(createBookmark("http://www.dwitte.de/", "Dennis Witte", Arrays.asList("Dennis Witte", "dwitte")));
		bookmarkDao.save(createBookmark("http://wuhrsteinalm.de/", "Wuhrsteinalm"));
		bookmarkDao.save(createBookmark("http://www.postbank.de/", "Postbank", Arrays.asList("bank")));
		bookmarkDao.save(createBookmark("https://banking.dkb.de/", "DKB", Arrays.asList("bank")));
		bookmarkDao.save(createBookmark("http://www.tagesschau.de/", "Tagesschau", Arrays.asList("Tagesschau")));
		bookmarkDao.save(createBookmark("http://www.tagesschau.de/100sekunden/", "Tagesschau in 100 Sekunden", Arrays.asList("Tagesschau")));
		bookmarkDao.save(createBookmark("http://filestube.com", "Filestube", Arrays.asList("Download")));
		bookmarkDao.save(createBookmark("http://store.steampowered.com/", "Steam - Store", Arrays.asList("Steam", "Shop", "Store")));
		bookmarkDao.save(createBookmark("http://www.rocketnews.de/manager/html/list", "Rocketnews - Tomcat - Manager", Arrays.asList("Tomcat", "Manager")));
		bookmarkDao.save(createBookmark("https://www.icloud.com/", "iCloud", Arrays.asList("iCloud", "Apple")));
		bookmarkDao.save(createBookmark("https://www.icloud.com/#find", "iCloud - Find", Arrays.asList("iCloud", "Find", "Apple")));
		bookmarkDao.save(createBookmark("http://dict.leo.org/", "dict.leo.org", Arrays.asList("Dictionary", "Translate")));
		bookmarkDao.save(createBookmark("http://www.doodle.com/", "Doodle", Arrays.asList("Abstimmen")));
		bookmarkDao.save(createBookmark("http://tomcat.apache.org/tomcat-6.0-doc/config/manager.html", "Tomcat-6.0 Config Manager"));
		bookmarkDao.save(createBookmark("http://www.google.com/analytics/", "Google Analytics"));
		bookmarkDao.save(createBookmark("http://www.rmv.de/", "RMV", Arrays.asList("Bus")));

		// az
		bookmarkDao.save(createBookmark("https://code.allianz24.de/hudson/", "Allianz24 - Hudson / Jenkins",
				Arrays.asList("Hudson", "Jenkins", "Seibert-Media", "Allianz24", "Allsecur", "AZ24"), true));
		bookmarkDao.save(createBookmark("https://wiki.allianz24.de/cgi-bin/twiki/view/AZ24/AnsprechPartner", "Allianz24 - AnsprechPartner",
				Arrays.asList("Seibert-Media", "Allianz24", "Allsecur", "AZ24")));

		// local
		bookmarkDao.save(createBookmark("http://127.0.0.1:8180/sonar", "Local - Sonar"));
		bookmarkDao.save(createBookmark("http://127.0.0.1:8180/manager/html/list", "Local - Tomcat Manager"));
		bookmarkDao.save(createBookmark("http://127.0.0.1:8180/jenkins", "Local - Jenkins", Arrays.asList("Local", "Lokal", "Hudson", "Jenkins")));
		bookmarkDao.save(createBookmark("http://phpmyadmin/", "Local - phpMyAdmin"));
		bookmarkDao.save(createBookmark("http://0.0.0.0:8161/admin/queues.jsp", "Local - ActiveMQ - JMS"));

		// 20ft devel
		bookmarkDao.save(createBookmark("/bb/twentyfeetperformance", "Twentyfeet - Devel - Performance", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));
		bookmarkDao.save(createBookmark("http://20ft/", "Twentyfeet - Devel", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));
		bookmarkDao.save(createBookmark("http://20ft/app/admin", "Twentyfeet - Devel - Admin", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));
		bookmarkDao.save(createBookmark("http://20ft/app/?log_level=DEBUG", "Twentyfeet - Devel - App with Debug", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));
		bookmarkDao.save(createBookmark("http://20ft/app/Home.html?gwt.codesvr=127.0.0.1:9997", "Twentyfeet - Devel - App in Developermode",
				Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel")));

		// 20ft live
		bookmarkDao.save(createBookmark("https://www.twentyfeet.com/", "Twentyfeet - Live", Arrays.asList("TwentyFeet", "20ft", "Live")));
		bookmarkDao.save(createBookmark("https://www.twentyfeet.com/admin/queues.jsp", "Twentyfeet - Live - ActiveMQ - JMS", Arrays.asList("TwentyFeet", "20ft", "Live"), true));
		bookmarkDao.save(createBookmark("https://central.twentyfeet.com/phpmyadmin/", "Twentyfeet - Live - phpMyadmin", Arrays.asList("TwentyFeet", "20ft", "Live")));
		bookmarkDao.save(createBookmark("https://kunden.seibert-media.net/display/20ft", "Twentyfeet - Live - Wiki", Arrays.asList("TwentyFeet", "20ft", "Live")));

		// 20ft test
		bookmarkDao.save(createBookmark("https://test.twentyfeet.com/", "Twentyfeet - Test", Arrays.asList("TwentyFeet", "20ft", "Test")));
		bookmarkDao.save(createBookmark("https://test.twentyfeet.com/admin/queues.jsp", "Twentyfeet - Test - ActiveMQ - JMS", Arrays.asList("TwentyFeet", "20ft", "Test")));

		// seibert-media
		bookmarkDao.save(createBookmark("https://vpn.rp.seibert-media.net/", "Seibert-Media - VPN", Arrays.asList("Seibert-Media", "SM", "VPN")));
		bookmarkDao.save(createBookmark("https://timetracker.rp.seibert-media.net/", "Seibert-Media - Timetracker", Arrays.asList("Seibert-Media", "SM"), true));
		bookmarkDao.save(createBookmark("https://confluence.rp.seibert-media.net/", "Seibert-Media - Confluence - Wiki", Arrays.asList("Seibert-Media", "SM"), true));
		bookmarkDao.save(createBookmark("https://hudson.rp.seibert-media.net/", "Seibert-Media - Hudson / Jenkins", Arrays.asList("Hudson", "Jenkins", "Seibert-Media", "SM")));
		bookmarkDao.save(createBookmark("https://micro.rp.seibert-media.net", "Seibert-Media - Microblog", Arrays.asList("Seibert-Media", "SM")));
		bookmarkDao.save(createBookmark("http://nexus.rp.seibert-media.net/", "Seibert-Media - Nexus", Arrays.asList("Seibert-Media", "SM")));
		bookmarkDao.save(createBookmark("https://zimbra.rp.seibert-media.net/", "Seibert-Media - Zimbra", Arrays.asList("Seibert-Media", "SM")));
		bookmarkDao.save(createBookmark("https://projectile.rp.seibert-media.net/", "Seibert-Media - Projectile", Arrays.asList("Seibert-Media", "SM")));

		// Movie
		bookmarkDao.save(createBookmark("http://www.cinestar.de/de/kino/mainz-cinestar/", "Movie - Kino - Mainz - Cinestar", Arrays.asList("Movie", "Film", "Kino")));
		bookmarkDao.save(createBookmark("http://www.cineplex.de/kino/programm/city53/?scope=week", "Movie - Kino - Wiesbaden - Cineplex", Arrays.asList("Movie", "Film", "Kino")));
		bookmarkDao.save(createBookmark("http://www.filmstarts.de/", "Movie - Review - Filmstarts", Arrays.asList("Movie", "Film")));
		bookmarkDao.save(createBookmark("http://rogerebert.suntimes.com/", "Movie - Review - Roger Ebert", Arrays.asList("Movie", "Film")));
		bookmarkDao.save(createBookmark("http://imdb.com/", "Movie - Review - Imdb", Arrays.asList("Movie", "Film")));
	}

	protected BookmarkBean createBookmark(final String url, final String name) throws StorageException {
		return createBookmark(url, name, false);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final List<String> keywords) throws StorageException {
		return createBookmark(url, name, keywords, false);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final String description, final List<String> keywords) throws StorageException {
		return createBookmark(url, name, description, keywords, false);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final boolean favorite) throws StorageException {
		return createBookmark(url, name, DEFAULT_DESCRIPTION, new ArrayList<String>(), favorite);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final List<String> keywords, final boolean favorite) throws StorageException {
		return createBookmark(url, name, DEFAULT_DESCRIPTION, keywords, favorite);
	}

	protected BookmarkBean createBookmark(final String url, final String name, final String description, final List<String> keywords, final boolean favorite) throws StorageException {
		final BookmarkBean bookmark = bookmarkDao.create();
		bookmark.setUrl(url);
		bookmark.setName(name);
		bookmark.setDescription(description);
		bookmark.setKeywords(keywords);
		bookmark.setFavorite(favorite);
		bookmark.setOwnerUsername("bborbe");
		return bookmark;
	}
}
