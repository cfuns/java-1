package de.benjaminborbe.lunch.wikiconnector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.xml.rpc.ServiceException;

import net.seibert_media.kunden.rpc.soap_axis.confluenceservice_v2.ConfluenceSoapService;
import net.seibert_media.kunden.rpc.soap_axis.confluenceservice_v2.ConfluenceSoapServiceServiceLocator;

import org.slf4j.Logger;

import com.atlassian.confluence.rpc.soap.beans.RemotePage;
import com.atlassian.confluence.rpc.soap.beans.RemotePageSummary;

import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.bean.LunchBean;
import de.benjaminborbe.lunch.util.LunchParseUtil;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;

public class LunchWikiConnectorImpl implements LunchWikiConnector {

    private final Logger logger;

    private final LunchParseUtil lunchParseUtil;

    private final HtmlUtil htmlUtil;

    private final CalendarUtil calendarUtil;

    private final TimeZoneUtil timeZoneUtil;

    // https://developer.atlassian.com/display/CONFDEV/Confluence+XML-RPC+and+SOAP+APIs
    @Inject
    public LunchWikiConnectorImpl(
            final Logger logger,
            final LunchParseUtil lunchParseUtil,
            final HtmlUtil htmlUtil,
            final CalendarUtil calendarUtil,
            final TimeZoneUtil timeZoneUtil) {
        this.logger = logger;
        this.lunchParseUtil = lunchParseUtil;
        this.htmlUtil = htmlUtil;
        this.calendarUtil = calendarUtil;
        this.timeZoneUtil = timeZoneUtil;
    }

    @Override
    public Collection<Lunch> extractLunchs(
            final String spaceKey,
            final String username,
            final String password,
            final String fullname,
            final Calendar date
            ) throws ServiceException,
                    java.rmi.RemoteException, ParseException {

        final ConfluenceSoapService service = getService();
        final String token = service.login(username, password);
        final RemotePageSummary[] remotePageSummaries = service.getPages(token, spaceKey);

        final List<Lunch> result = new ArrayList<Lunch>();
        for (final RemotePageSummary remotePageSummary : remotePageSummaries) {
            if (isLunchPage(remotePageSummary) && isLunchDateTodayOrFuture(remotePageSummary, date)) {
                logger.trace("'" + remotePageSummary.getTitle() + "' is lunch page");
                final Lunch lunch = createLunch(service, token, remotePageSummary, fullname);
                result.add(lunch);
            } else {
                logger.trace("'" + remotePageSummary.getTitle() + "' is not lunch page");
            }
        }
        return result;
    }

    private ConfluenceSoapService getService() throws ServiceException {
        final ConfluenceSoapServiceServiceLocator serviceLocator = new ConfluenceSoapServiceServiceLocator();
        return serviceLocator.getConfluenceserviceV2();
    }

    private boolean isLunchDateTodayOrFuture(final RemotePageSummary remotePageSummary, final Calendar date) throws ParseException {
        if (date == null) {
            return true;
        }
        final Calendar pageDate = extractDate(remotePageSummary.getTitle());
        logger.debug("compare " + calendarUtil.toDateTimeString(date) + " <=> " + calendarUtil.toDateTimeString(pageDate));

        return date.compareTo(pageDate) != 1;
    }

    private boolean isLunchDate(final RemotePageSummary remotePageSummary, final Calendar date) throws ParseException {
        final Calendar pageDate = extractDate(remotePageSummary.getTitle());
        logger.debug("compare " + calendarUtil.toDateTimeString(date) + " <=> " + calendarUtil.toDateTimeString(pageDate));

        return date.compareTo(pageDate) == 0;
    }

    protected Lunch createLunch(
            final ConfluenceSoapService service,
            final String token,
            final RemotePageSummary remotePageSummary,
            final String fullname
            ) throws ParseException,
                    java.rmi.RemoteException {
        final RemotePage page = service.getPage(token, remotePageSummary.getId());
        final String htmlContent = htmlUtil.unescapeHtml(service.renderContent(token, page.getSpace(), page.getId(), page.getContent()));
        final LunchBean lunch = new LunchBean();
        lunch.setName(lunchParseUtil.extractLunchName(htmlContent));
        lunch.setSubscribed(lunchParseUtil.extractLunchSubscribed(page.getContent(), fullname));
        lunch.setDate(extractDate(remotePageSummary.getTitle()).getTime());
        lunch.setUrl(buildUrl(remotePageSummary.getUrl()));
        return lunch;
    }

    private URL buildUrl(final String url) {
        try {
            return new URL(url);
        } catch (final MalformedURLException e) {
            logger.debug("build WikiPageUrl failed!", e);
            return null;
        }
    }

    protected Calendar extractDate(final String title) throws ParseException {
        final String[] parts = title.split(" ");
        return calendarUtil.parseDate(timeZoneUtil.getUTCTimeZone(), parts[0]);
    }

    protected boolean isLunchPage(final RemotePageSummary remotePageSummary) {
        return remotePageSummary != null && isLunchTitle(remotePageSummary.getTitle());
    }

    protected boolean isLunchTitle(String title) {
        return title != null && title.matches("\\d{4}-\\d{2}-\\d{2}\\s+.*?");
    }

    @Override
    public Collection<String> extractSubscriptions(
            final String spaceKey,
            final String username,
            final String password,
            final Calendar date
            ) throws ServiceException,
                    java.rmi.RemoteException, ParseException {

        final ConfluenceSoapService service = getService();
        final String token = service.login(username, password);
        final RemotePageSummary[] remotePageSummaries = service.getPages(token, spaceKey);
        logger.debug("found " + remotePageSummaries + " pages");
        final List<String> result = new ArrayList<String>();
        for (final RemotePageSummary remotePageSummary : remotePageSummaries) {
            if (isLunchPage(remotePageSummary) && isLunchDate(remotePageSummary, date)) {
                logger.debug("found lunchpage for date " + calendarUtil.toDateString(date) + " => " + remotePageSummary.getTitle());
                final RemotePage page = service.getPage(token, remotePageSummary.getId());
                final String htmlContent = htmlUtil.unescapeHtml(service.renderContent(token, page.getSpace(), page.getId(), page.getContent()));
                result.addAll(lunchParseUtil.extractSubscribedUser(htmlContent));
            }
        }
        return result;

    }
}
