package de.benjaminborbe.lunch.wikiconnector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.xml.rpc.ServiceException;

import net.seibert_media.kunden.rpc.soap_axis.confluenceservice_v2.ConfluenceSoapService;
import net.seibert_media.kunden.rpc.soap_axis.confluenceservice_v2.ConfluenceSoapServiceServiceLocator;

import org.slf4j.Logger;

import com.atlassian.confluence.rpc.AuthenticationFailedException;
import com.atlassian.confluence.rpc.InvalidSessionException;
import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.rpc.soap.beans.RemotePage;
import com.atlassian.confluence.rpc.soap.beans.RemotePageSummary;
import com.google.inject.Inject;

import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.bean.LunchBean;
import de.benjaminborbe.lunch.util.LunchParseUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;

public class LunchWikiConnectorImpl implements LunchWikiConnector {

	private final DateUtil dateUtil;

	private final Logger logger;

	private final LunchParseUtil lunchParseUtil;

	private final HtmlUtil htmlUtil;

	// https://developer.atlassian.com/display/CONFDEV/Confluence+XML-RPC+and+SOAP+APIs
	@Inject
	public LunchWikiConnectorImpl(final Logger logger, final DateUtil dateUtil, final LunchParseUtil lunchParseUtil, final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.dateUtil = dateUtil;
		this.lunchParseUtil = lunchParseUtil;
		this.htmlUtil = htmlUtil;
	}

	@Override
	public Collection<Lunch> extractLunchs(final String spaceKey, final String username, final String password, final String fullname, final Date date) throws ServiceException,
			AuthenticationFailedException, RemoteException, java.rmi.RemoteException, ParseException {

		final ConfluenceSoapService service = getService();
		final String token = service.login(username, password);
		final RemotePageSummary[] remotePageSummaries = service.getPages(token, spaceKey);

		final List<Lunch> result = new ArrayList<Lunch>();
		for (final RemotePageSummary remotePageSummary : remotePageSummaries) {
			if (isLunchPage(remotePageSummary) && isLunchDate(remotePageSummary, date)) {
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

	private ConfluenceSoapService getService() throws ServiceException {
		final ConfluenceSoapServiceServiceLocator serviceLocator = new ConfluenceSoapServiceServiceLocator();
		return serviceLocator.getConfluenceserviceV2();
	}

	private boolean isLunchDate(final RemotePageSummary remotePageSummary, final Date date) throws ParseException {
		if (date == null) {
			return true;
		}
		final Date pageDate = extractDate(remotePageSummary.getTitle());
		logger.trace("compare " + dateUtil.dateTimeString(date) + " <=> " + dateUtil.dateTimeString(pageDate));
		return date.compareTo(pageDate) != 1;
	}

	protected Lunch createLunch(final ConfluenceSoapService service, final String token, final RemotePageSummary remotePageSummary, final String fullname) throws ParseException,
			InvalidSessionException, RemoteException, java.rmi.RemoteException {
		final RemotePage page = service.getPage(token, remotePageSummary.getId());
		final String htmlContent = htmlUtil.unescapeHtml(service.renderContent(token, page.getSpace(), page.getId(), page.getContent()));
		final LunchBean lunch = new LunchBean();
		lunch.setName(lunchParseUtil.extractLunchName(htmlContent));
		lunch.setSubscribed(lunchParseUtil.extractLunchSubscribed(page.getContent(), fullname));
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

	protected Date extractDate(final String title) throws ParseException {
		final String[] parts = title.split(" ");
		return dateUtil.parseDate(parts[0]);
	}

	protected boolean isLunchPage(final RemotePageSummary remotePageSummary) {
		return remotePageSummary != null && remotePageSummary.getTitle() != null && remotePageSummary.getTitle().matches("\\d+-\\d+-\\d+ Bastians (Mittagessen|Wiesbaden)");
	}

	@Override
	public Collection<String> extractSubscriptions(final String spaceKey, final String username, final String password, final Date date) throws ServiceException,
			AuthenticationFailedException, RemoteException, java.rmi.RemoteException, ParseException {

		final ConfluenceSoapService service = getService();
		final String token = service.login(username, password);
		final RemotePageSummary[] remotePageSummaries = service.getPages(token, spaceKey);

		final List<String> result = new ArrayList<String>();
		for (final RemotePageSummary remotePageSummary : remotePageSummaries) {
			if (isLunchPage(remotePageSummary) && isLunchDate(remotePageSummary, date)) {
				final RemotePage page = service.getPage(token, remotePageSummary.getId());
				final String htmlContent = htmlUtil.unescapeHtml(service.renderContent(token, page.getSpace(), page.getId(), page.getContent()));
				result.addAll(lunchParseUtil.extractSubscribedUser(htmlContent));
			}
		}
		return result;

	}

}
