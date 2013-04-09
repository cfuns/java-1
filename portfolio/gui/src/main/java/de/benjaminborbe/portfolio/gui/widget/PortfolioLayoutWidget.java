package de.benjaminborbe.portfolio.gui.widget;

import com.google.inject.Inject;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.html.api.CssResource;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.portfolio.gui.PortfolioGuiConstants;
import de.benjaminborbe.portfolio.gui.util.PortfolioGuiLinkFactory;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.table.TableCellWidget;
import de.benjaminborbe.website.table.TableRowWidget;
import de.benjaminborbe.website.table.TableWidget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.CssResourceImpl;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BodyWidget;
import de.benjaminborbe.website.widget.GoogleAnalyticsScriptWidget;
import de.benjaminborbe.website.widget.HeadWidget;
import de.benjaminborbe.website.widget.HtmlWidget;
import de.benjaminborbe.website.widget.ImageWidget;
import de.benjaminborbe.website.widget.VoidWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;

public class PortfolioLayoutWidget extends CompositeWidget implements Widget {

	private String title;

	private final TopWidget topWidget;

	private final BottomWidget footerWidget;

	private Widget content;

	private final Logger logger;

	private final PortfolioGuiLinkFactory portfolioLinkFactory;

	private Collection<GalleryEntry> galleryEntries = new HashSet<GalleryEntry>();

	@Inject
	public PortfolioLayoutWidget(
		final TopWidget topWidget,
		final BottomWidget footerWidget,
		final PortfolioGuiLinkFactory portfolioLinkFactory,
		final Logger logger) {
		this.topWidget = topWidget;
		this.footerWidget = footerWidget;
		this.portfolioLinkFactory = portfolioLinkFactory;
		this.logger = logger;
	}

	private CssResource buildCssResource(final HttpServletRequest request, final String path) {
		return new CssResourceImpl(request.getContextPath() + "/" + PortfolioGuiConstants.NAME + "/css/" + path);
	}

	private JavascriptResource buildJavascriptResource(final HttpServletRequest request, final String path) {
		return new JavascriptResourceImpl(request.getContextPath() + "/" + PortfolioGuiConstants.NAME + "/js/" + path);
	}

	private Widget getContent() {
		return content != null ? content : new VoidWidget();
	}

	public PortfolioLayoutWidget addContent(final Widget content) {
		this.content = content;
		return this;
	}

	public PortfolioLayoutWidget addTitle(final String title) {
		this.title = title;
		return this;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final HeadWidget headWidget = new HeadWidget();
		headWidget.addTitle(title);

		headWidget.addJavascriptResource(buildJavascriptResource(request, "prototype.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "scriptaculous.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "builder.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "effects.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "lightbox.js"));
		headWidget.addJavascriptResource(buildJavascriptResource(request, "script.js"));

		headWidget.addCssResource(buildCssResource(request, "style.css"));
		headWidget.addCssResource(buildCssResource(request, "lightbox.css"));

		headWidget.add(new GoogleAnalyticsScriptWidget());

		final ListWidget widgets = new ListWidget();
		widgets.add(topWidget);
		widgets.add(new DivWidget(createContentLayoutWidget(request)).addAttribute("id", "content"));
		widgets.add(footerWidget);

		final BodyWidget bodyWidget = new BodyWidget(widgets);
		return new HtmlWidget(headWidget, bodyWidget);
	}

	private Widget createContentLayoutWidget(final HttpServletRequest request) throws GalleryServiceException,
		UnsupportedEncodingException, AuthenticationServiceException {

		final TableWidget table = new TableWidget();
		table.addId("images");

		final TableRowWidget row = new TableRowWidget();
		table.addRow(row);
		final TableCellWidget firstCell = new TableCellWidget();
		firstCell.addClass("node");
		firstCell.addClass("start");
		final DivWidget content = new DivWidget();
		content.addClass("content");
		content.addContent(getContent());
		firstCell.setContent(content);
		row.addCell(firstCell);

		final Collection<GalleryEntry> entries = getGalleryEntries();
		logger.debug("entries: " + entries.size());
		for (final GalleryEntry entry : entries) {
			final String imageUrl = portfolioLinkFactory.imageLink(request, entry.getImageIdentifier());
			final ImageWidget previewImage = new ImageWidget(portfolioLinkFactory.imageLink(request, entry.getPreviewImageIdentifier()));
			previewImage.addAlt(imageUrl);
			final LinkWidget link = new LinkWidget(imageUrl, previewImage);
			link.addAttribute("rel", "lightbox[set]");
			final TableCellWidget cell = new TableCellWidget(link);
			cell.addClass("node");
			cell.addClass("image");
			row.addCell(cell);
		}
		final TableCellWidget lastcell = new TableCellWidget();
		lastcell.addClass("node");
		lastcell.addClass("end");
		lastcell.setContent(new DivWidget().addClass("content").addContent(new LinkWidget("javascript:resetPosition();", "end")));
		row.addCell(lastcell);
		return table;
	}

	private Collection<GalleryEntry> getGalleryEntries() {
		return galleryEntries;
	}

	public void setGalleryEntries(final Collection<GalleryEntry> galleryEntries) {
		this.galleryEntries = galleryEntries;
	}

}
