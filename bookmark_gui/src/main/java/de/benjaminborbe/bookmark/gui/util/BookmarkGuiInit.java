package de.benjaminborbe.bookmark.gui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.api.BookmarkServiceException;

public class BookmarkGuiInit {

	private static final String DEFAULT_DESCRIPTION = "-";

	private final BookmarkService bookmarkService;

	@Inject
	public BookmarkGuiInit(final BookmarkService bookmarkService) {
		this.bookmarkService = bookmarkService;
	}

	public void init(final SessionIdentifier sessionIdentifier) throws BookmarkServiceException, LoginRequiredException, ValidationException {
		// internal
		createBookmark(sessionIdentifier, "/bb/bookmark", "Local - BB - Bookmarks", Arrays.asList("bookmark", "lesezeichen"));
		createBookmark(sessionIdentifier, "/bb/worktime", "Local - BB - Worktimes");
		createBookmark(sessionIdentifier, "/bb/util/passwordGenerator", "Local - BB - PasswordGenerator");
		createBookmark(sessionIdentifier, "/bb/monitoring", "Local - BB - Monitoring");
		createBookmark(sessionIdentifier, "/bb/gwt/Home.html", "Local - BB - GWT");
		createBookmark(sessionIdentifier, "/bb/search", "Local - BB - Search");
		createBookmark(sessionIdentifier, "/bb/search/components", "Local - BB - Search Components");
		createBookmark(sessionIdentifier, "/bb/performance", "Local - BB - Performance");
		createBookmark(sessionIdentifier, "/bb/crawler", "Local - BB - Crawler");
		createBookmark(sessionIdentifier, "/bb/configuration", "Local - BB - Configuration");
		createBookmark(sessionIdentifier, "/bb/authentication/login", "Local - BB - Login");
		createBookmark(sessionIdentifier, "/bb/websearch/list", "Local - BB - Websearch - Pages");
		createBookmark(sessionIdentifier, "/bb/websearch/refresh", "Local - BB - Websearch - Refresh");

		// extern
		createBookmark(sessionIdentifier, "https://console.aws.amazon.com/ec2/home", "Amazon EC2");
		createBookmark(sessionIdentifier, "http://kleinanzeigen.ebay.de/", "Ebay Kleinanzeigen");
		createBookmark(sessionIdentifier, "http://confluence.benjamin-borbe.de", "Benjamin Borbe - Confluence - Wiki", Arrays.asList("wiki", "confluence", "rocketnews"));
		createBookmark(sessionIdentifier, "http://www.harteslicht.com", "Harteslicht", Arrays.asList("foto", "photo", "photography", "fotografie"));
		createBookmark(sessionIdentifier, "http://www.benjamin-borbe.de", "Benjamin Borbe", Arrays.asList("foto", "photo", "photography", "fotografie", "portfolio"));
		createBookmark(sessionIdentifier, "http://tomtom.de/gettingstarted", "TomTom");
		createBookmark(sessionIdentifier, "http://www.guenstiger.de", "Guenstiger", Arrays.asList("search"));
		createBookmark(sessionIdentifier, "http://geizhals.at/deutschland/", "Geizhals", Arrays.asList("search"));
		createBookmark(sessionIdentifier, "http://www.google.de/?hl=en", "Google");
		createBookmark(sessionIdentifier, "http://www.audible.de/", "Audible - Hörbücher", Arrays.asList("Hörbücher", "Hoerbuecher", "shop"));
		createBookmark(sessionIdentifier, "http://www.dwitte.de/", "Dennis Witte", Arrays.asList("Dennis Witte", "dwitte"));
		createBookmark(sessionIdentifier, "http://wuhrsteinalm.de/", "Wuhrsteinalm");
		createBookmark(sessionIdentifier, "http://www.postbank.de/", "Postbank", Arrays.asList("bank"));
		createBookmark(sessionIdentifier, "https://banking.dkb.de/", "DKB", Arrays.asList("bank"));
		createBookmark(sessionIdentifier, "http://www.tagesschau.de/", "Tagesschau", Arrays.asList("Tagesschau"));
		createBookmark(sessionIdentifier, "http://www.tagesschau.de/100sekunden/", "Tagesschau in 100 Sekunden", Arrays.asList("Tagesschau"));
		createBookmark(sessionIdentifier, "http://filestube.com", "Filestube", Arrays.asList("Download"));
		createBookmark(sessionIdentifier, "http://store.steampowered.com/", "Steam - Store", Arrays.asList("Steam", "Shop", "Store"));
		createBookmark(sessionIdentifier, "http://www.rocketnews.de/manager/html/list", "Rocketnews - Tomcat - Manager", Arrays.asList("Tomcat", "Manager"));
		createBookmark(sessionIdentifier, "https://www.icloud.com/", "iCloud", Arrays.asList("iCloud", "Apple"));
		createBookmark(sessionIdentifier, "https://www.icloud.com/#find", "iCloud - Find", Arrays.asList("iCloud", "Find", "Apple"));
		createBookmark(sessionIdentifier, "http://dict.leo.org/", "dict.leo.org", Arrays.asList("Dictionary", "Translate"));
		createBookmark(sessionIdentifier, "http://www.doodle.com/", "Doodle", Arrays.asList("Abstimmen"));
		createBookmark(sessionIdentifier, "http://tomcat.apache.org/tomcat-6.0-doc/config/manager.html", "Tomcat-6.0 Config Manager");
		createBookmark(sessionIdentifier, "http://www.google.com/analytics/", "Google Analytics");
		createBookmark(sessionIdentifier, "http://www.rmv.de/", "RMV", Arrays.asList("Bus"));

		// az
		createBookmark(sessionIdentifier, "https://code.allianz24.de/hudson/", "Allianz24 - Hudson / Jenkins",
				Arrays.asList("Hudson", "Jenkins", "Seibert-Media", "Allianz24", "Allsecur", "AZ24"), true);
		createBookmark(sessionIdentifier, "https://wiki.allianz24.de/cgi-bin/twiki/view/AZ24/AnsprechPartner", "Allianz24 - AnsprechPartner",
				Arrays.asList("Seibert-Media", "Allianz24", "Allsecur", "AZ24"));

		// local
		createBookmark(sessionIdentifier, "http://127.0.0.1:8180/sonar", "Local - Sonar");
		createBookmark(sessionIdentifier, "http://127.0.0.1:8180/manager/html/list", "Local - Tomcat Manager");
		createBookmark(sessionIdentifier, "http://127.0.0.1:8180/jenkins", "Local - Jenkins", Arrays.asList("Local", "Lokal", "Hudson", "Jenkins"));
		createBookmark(sessionIdentifier, "http://phpmyadmin/", "Local - phpMyAdmin");
		createBookmark(sessionIdentifier, "http://0.0.0.0:8161/admin/queues.jsp", "Local - ActiveMQ - JMS");

		// 20ft devel
		createBookmark(sessionIdentifier, "/bb/twentyfeetperformance", "Twentyfeet - Devel - Performance", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel"));
		createBookmark(sessionIdentifier, "http://20ft/", "Twentyfeet - Devel", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel"));
		createBookmark(sessionIdentifier, "http://20ft/app/admin", "Twentyfeet - Devel - Admin", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel"));
		createBookmark(sessionIdentifier, "http://20ft/app/?log_level=DEBUG", "Twentyfeet - Devel - App with Debug", Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel"));
		createBookmark(sessionIdentifier, "http://20ft/app/Home.html?gwt.codesvr=127.0.0.1:9997", "Twentyfeet - Devel - App in Developermode",
				Arrays.asList("TwentyFeet", "20ft", "Dev", "Devel"));

		// 20ft live
		createBookmark(sessionIdentifier, "https://www.twentyfeet.com/", "Twentyfeet - Live", Arrays.asList("TwentyFeet", "20ft", "Live"));
		createBookmark(sessionIdentifier, "https://www.twentyfeet.com/admin/queues.jsp", "Twentyfeet - Live - ActiveMQ - JMS", Arrays.asList("TwentyFeet", "20ft", "Live"), true);
		createBookmark(sessionIdentifier, "https://central.twentyfeet.com/phpmyadmin/", "Twentyfeet - Live - phpMyadmin", Arrays.asList("TwentyFeet", "20ft", "Live"));
		createBookmark(sessionIdentifier, "https://kunden.seibert-media.net/display/20ft", "Twentyfeet - Live - Wiki", Arrays.asList("TwentyFeet", "20ft", "Live"));

		// 20ft test
		createBookmark(sessionIdentifier, "https://test.twentyfeet.com/", "Twentyfeet - Test", Arrays.asList("TwentyFeet", "20ft", "Test"));
		createBookmark(sessionIdentifier, "https://test.twentyfeet.com/admin/queues.jsp", "Twentyfeet - Test - ActiveMQ - JMS", Arrays.asList("TwentyFeet", "20ft", "Test"));

		// seibert-media
		createBookmark(sessionIdentifier, "https://vpn.rp.seibert-media.net/", "Seibert-Media - VPN", Arrays.asList("Seibert-Media", "SM", "VPN"));
		createBookmark(sessionIdentifier, "https://timetracker.rp.seibert-media.net/", "Seibert-Media - Timetracker", Arrays.asList("Seibert-Media", "SM"), true);
		createBookmark(sessionIdentifier, "https://confluence.rp.seibert-media.net/", "Seibert-Media - Confluence - Wiki", Arrays.asList("Seibert-Media", "SM"), true);
		createBookmark(sessionIdentifier, "https://hudson.rp.seibert-media.net/", "Seibert-Media - Hudson / Jenkins", Arrays.asList("Hudson", "Jenkins", "Seibert-Media", "SM"));
		createBookmark(sessionIdentifier, "https://micro.rp.seibert-media.net", "Seibert-Media - Microblog", Arrays.asList("Seibert-Media", "SM"));
		createBookmark(sessionIdentifier, "http://nexus.rp.seibert-media.net/", "Seibert-Media - Nexus", Arrays.asList("Seibert-Media", "SM"));
		createBookmark(sessionIdentifier, "https://zimbra.rp.seibert-media.net/", "Seibert-Media - Zimbra", Arrays.asList("Seibert-Media", "SM"));
		createBookmark(sessionIdentifier, "https://projectile.rp.seibert-media.net/", "Seibert-Media - Projectile", Arrays.asList("Seibert-Media", "SM"));

		// Movie
		createBookmark(sessionIdentifier, "http://www.cinestar.de/de/kino/mainz-cinestar/", "Movie - Kino - Mainz - Cinestar", Arrays.asList("Movie", "Film", "Kino"));
		createBookmark(sessionIdentifier, "http://www.cineplex.de/kino/programm/city53/?scope=week", "Movie - Kino - Wiesbaden - Cineplex", Arrays.asList("Movie", "Film", "Kino"));
		createBookmark(sessionIdentifier, "http://www.filmstarts.de/", "Movie - Review - Filmstarts", Arrays.asList("Movie", "Film"));
		createBookmark(sessionIdentifier, "http://rogerebert.suntimes.com/", "Movie - Review - Roger Ebert", Arrays.asList("Movie", "Film"));
		createBookmark(sessionIdentifier, "http://imdb.com/", "Movie - Review - Imdb", Arrays.asList("Movie", "Film"));
	}

	protected void createBookmark(final SessionIdentifier sessionIdentifier, final String url, final String name) throws BookmarkServiceException, LoginRequiredException,
			ValidationException {
		createBookmark(sessionIdentifier, url, name, false);
	}

	protected void createBookmark(final SessionIdentifier sessionIdentifier, final String url, final String name, final List<String> keywords) throws BookmarkServiceException,
			LoginRequiredException, ValidationException {
		createBookmark(sessionIdentifier, url, name, keywords, false);
	}

	protected void createBookmark(final SessionIdentifier sessionIdentifier, final String url, final String name, final String description, final List<String> keywords)
			throws BookmarkServiceException, LoginRequiredException, ValidationException {
		createBookmark(sessionIdentifier, url, name, description, keywords, false);
	}

	protected void createBookmark(final SessionIdentifier sessionIdentifier, final String url, final String name, final boolean favorite) throws BookmarkServiceException,
			LoginRequiredException, ValidationException {
		createBookmark(sessionIdentifier, url, name, DEFAULT_DESCRIPTION, new ArrayList<String>(), favorite);
	}

	protected void createBookmark(final SessionIdentifier sessionIdentifier, final String url, final String name, final List<String> keywords, final boolean favorite)
			throws BookmarkServiceException, LoginRequiredException, ValidationException {
		createBookmark(sessionIdentifier, url, name, DEFAULT_DESCRIPTION, keywords, favorite);
	}

	protected void createBookmark(final SessionIdentifier sessionIdentifier, final String url, final String name, final String description, final List<String> keywords,
			final boolean favorite) throws BookmarkServiceException, LoginRequiredException, ValidationException {
		bookmarkService.createBookmark(sessionIdentifier, url, name, description, keywords, favorite);
	}
}
