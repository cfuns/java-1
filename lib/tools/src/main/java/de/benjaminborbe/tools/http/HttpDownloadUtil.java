package de.benjaminborbe.tools.http;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

@Singleton
public class HttpDownloadUtil {

    private static final String DEFAULT_ENCODING = "UTF8";

    private final Logger logger;

    @Inject
    public HttpDownloadUtil(final Logger logger) {
        this.logger = logger;
    }

    public String getContent(final HttpDownloadResult result) throws UnsupportedEncodingException {
        if (result.getContent() == null) {
            logger.debug("content is null => return null");
            return null;
        } else if (result.getContentEncoding() != null && result.getContentEncoding().getEncoding() != null) {
            logger.trace("use encoding from result: " + result.getContentEncoding().getEncoding());
            return new String(result.getContent(), result.getContentEncoding().getEncoding());
        } else {
            final String htmlContent = new String(result.getContent());
            final Document document = Jsoup.parse(htmlContent);
            final Elements heads = document.getElementsByTag("head");
            for (final Element head : heads) {
                final Elements metas = head.getElementsByTag("meta");
                for (final Element meta : metas) {
                    if ("Content-Type".equalsIgnoreCase(meta.attr("http-equiv"))) {
                        final String contentType = meta.attr("content");
                        if (contentType != null) {
                            final String s = "charset=";
                            final int charsetPos = contentType.indexOf(s);
                            if (charsetPos != -1) {
                                final String encoding = contentType.substring(charsetPos + s.length());
                                logger.debug("use encoding from content: " + encoding);
                                return new String(result.getContent(), encoding);
                            }
                        }
                    }
                }
            }

            logger.debug("unknown encoding, using default encoding");
            final String content = new String(result.getContent(), DEFAULT_ENCODING);
            logger.trace("content.length: " + content.length());
            return content;
        }
    }
}
