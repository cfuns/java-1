package de.benjaminborbe.lunch.connector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.rpc.ServiceException;

import net.seibertmedia.kunden.ConfluenceSoapService;
import net.seibertmedia.kunden.ConfluenceSoapServiceServiceLocator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.InvalidSessionException;
import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.rpc.soap.beans.RemotePage;
import com.atlassian.confluence.rpc.soap.beans.RemotePageSummary;
import com.google.inject.Inject;

import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.bean.LunchBean;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;

public class WikiConnector {

	private final DateUtil dateUtil;

	private final HtmlUtil htmlUtil;

	private final Logger logger;

	// https://developer.atlassian.com/display/CONFDEV/Confluence+XML-RPC+and+SOAP+APIs
	@Inject
	public WikiConnector(final Logger logger, final DateUtil dateUtil, final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.dateUtil = dateUtil;
		this.htmlUtil = htmlUtil;
	}

	public Collection<Lunch> extractLunchs(final String spaceKey, final String username, final String password, final String fullname) throws ServiceException,
			AuthenticationFailedException, RemoteException, java.rmi.RemoteException, ParseException {

		final List<Lunch> result = new ArrayList<Lunch>();

		final ConfluenceSoapServiceServiceLocator serviceLocator = new ConfluenceSoapServiceServiceLocator();
		final ConfluenceSoapService service = serviceLocator.getConfluenceserviceV1();
		final String token = service.login(username, password);
		final RemotePageSummary[] remotePageSummaries = service.getPages(token, spaceKey);
		for (final RemotePageSummary remotePageSummary : remotePageSummaries) {
			if (isLunchPage(remotePageSummary)) {
				logger.trace("'" + remotePageSummary.getTitle() + "' is lunch page");
				final Lunch lunch = createLunch(service, token, remotePageSummary, fullname);
				result.add(lunch);
			}
			else {
				logger.trace("'" + remotePageSummary.getTitle() + "' is not lunch page");
			}
		}
		return result;
	}

	protected Lunch createLunch(final ConfluenceSoapService service, final String token, final RemotePageSummary remotePageSummary, final String fullname) throws ParseException,
			InvalidSessionException, RemoteException, java.rmi.RemoteException {
		final LunchBean lunch = new LunchBean();
		final RemotePage page = service.getPage(token, remotePageSummary.getId());
		final String htmlContent = service.renderContent(token, page.getSpace(), page.getId(), page.getContent());
		lunch.setName(extractLunchName(htmlContent));
		lunch.setSubscribed(extractLunchSubscribed(htmlContent, fullname));
		lunch.setDate(extractDate(remotePageSummary.getTitle()));
		lunch.setUrl(buildUrl(remotePageSummary.getUrl()));
		return lunch;
	}

	private URL buildUrl(final String url) {
		try {
			return new URL(url);
		}
		catch (final MalformedURLException e) {
			logger.debug("build WikiPageUrl failed!", e);
			return null;
		}
	}

	protected boolean extractLunchSubscribed(final String htmlContent, final String fullname) {
		final Document document = Jsoup.parse(htmlContent);
		final Elements tables = document.getElementsByClass("confluenceTable");
		for (final Element table : tables) {
			final Elements tds = table.getElementsByTag("td");
			for (final Element td : tds) {
				if (td.html().indexOf(fullname) != -1) {
					return true;
				}
			}
		}
		return false;
	}

	private String extractLunchName(final String htmlContent) {
		final Document document = Jsoup.parse(htmlContent);
		final Elements elements = document.getElementsByClass("tipMacro");
		for (final Element element : elements) {
			final Elements tds = element.getElementsByTag("td");
			for (final Element td : tds) {
				final String innerHtml = td.html();
				final String result = htmlUtil.filterHtmlTages(innerHtml);
				if (result != null && result.length() > 0) {
					return result;
				}
			}
		}
		return null;
	}

	protected Date extractDate(final String title) throws ParseException {
		final String[] parts = title.split(" ");
		return dateUtil.parseDate(parts[0]);
	}

	protected boolean isLunchPage(final RemotePageSummary remotePageSummary) {
		return remotePageSummary != null && remotePageSummary.getTitle() != null && remotePageSummary.getTitle().matches("\\d+-\\d+-\\d+ Bastians (Mittagessen|Wiesbaden)");
	}

}
