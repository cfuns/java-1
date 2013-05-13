package de.benjaminborbe.selenium.gui.widget;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.selenium.api.SeleniumExecutionProtocol;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SeleniumGuiExecuteWidget extends CompositeWidget {

	private final SeleniumExecutionProtocol seleniumExecutionProtocol;

	public SeleniumGuiExecuteWidget(final SeleniumExecutionProtocol seleniumExecutionProtocol) {
		this.seleniumExecutionProtocol = seleniumExecutionProtocol;
	}

	@Override
	protected Widget createWidget(
		final HttpServletRequest request, final HttpServletResponse response, final HttpContext context
	) throws Exception {
		final ListWidget widgets = new ListWidget();

		widgets.add("completed: " + seleniumExecutionProtocol.isCompleted());
		widgets.add(new BrWidget());
		widgets.add("hasErrors: " + seleniumExecutionProtocol.hasErrors());
		widgets.add(new BrWidget());
		{
			widgets.add(new H2Widget("Messages"));
			final UlWidget ul = new UlWidget();
			for (final String message : seleniumExecutionProtocol.getMessages()) {
				ul.add(message);
			}
			widgets.add(ul);
		}
		return widgets;
	}
}
