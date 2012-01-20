package de.benjaminborbe.microblog.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;

@Singleton
public class MicroblogPostMailerImpl implements MicroblogPostMailer {

	// 3 sec
	private static final int TIMEOUT = 3 * 1000;

	private final Logger logger;

	private final MailService mailService;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	@Inject
	public MicroblogPostMailerImpl(final Logger logger, final MailService mailService, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil) {
		this.logger = logger;
		this.mailService = mailService;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
	}

	@Override
	public void mailPost(final long rev) throws MicroblogPostMailerException {

		logger.trace("send rev = " + rev);
		try {
			final Mail mail = buildMail(rev);
			mailService.send(mail);
		}
		catch (final MalformedURLException e) {
			logger.error("MalformedURLException", e);
			throw new MicroblogPostMailerException("MalformedURLException", e);
		}
		catch (final IOException e) {
			logger.error("IOException", e);
			throw new MicroblogPostMailerException("IOException", e);
		}
		catch (final MailSendException e) {
			logger.error("MailSendException", e);
			throw new MicroblogPostMailerException("MailSendException", e);
		}
	}

	protected Mail buildMail(final long rev) throws MalformedURLException, IOException {
		final String url = "https://micro.rp.seibert-media.net/notice/" + rev;
		final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(new URL(url), TIMEOUT);
		final String pageContent = httpDownloadUtil.getContent(result);
		final String content = extractContent(pageContent);
		final StringBuffer mailContent = new StringBuffer();
		mailContent.append(content);
		mailContent.append("\n\n");
		mailContent.append(url);

		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "Micro: " + preview(content);
		return new Mail(from, to, subject, mailContent.toString());
	}

	protected String extractContent(final String pageContent) {
		final String startPattern = "<p class=\"entry-content\">";
		final String endPattern = "</p>";
		final int startPos = pageContent.indexOf(startPattern);
		if (startPos == -1)
			return null;
		final int endPos = pageContent.indexOf(endPattern, startPos);
		if (endPos == -1)
			return null;
		return filterHtmlTages(pageContent.substring(startPos + startPattern.length(), endPos));
	}

	protected String filterHtmlTages(final String content) {
		return content.replaceAll("<.*?>", " ").replaceAll("\\s+", " ").trim();
	}

	protected String preview(final String content) {
		if (content != null && content.length() > 80) {
			return content.substring(0, 80);
		}
		return content;
	}
}
