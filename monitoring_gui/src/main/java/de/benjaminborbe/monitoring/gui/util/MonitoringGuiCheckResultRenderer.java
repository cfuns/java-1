package de.benjaminborbe.monitoring.gui.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;

public class MonitoringGuiCheckResultRenderer extends CompositeWidget {

	private final MonitoringNode checkResult;

	private final MonitoringGuiLinkFactory monitoringGuiLinkFactory;

	public MonitoringGuiCheckResultRenderer(final MonitoringNode checkResult, final MonitoringGuiLinkFactory monitoringGuiLinkFactory) {
		this.checkResult = checkResult;
		this.monitoringGuiLinkFactory = monitoringGuiLinkFactory;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();
		widgets.add("[");
		if (checkResult.getResult() == null) {
			widgets.add(new SpanWidget("???").addClass("checkResultUnknown"));
		}
		else if (Boolean.TRUE.equals(checkResult.getResult())) {
			widgets.add(new SpanWidget("OK").addClass("checkResultOk"));
		}
		else {
			widgets.add(new SpanWidget("FAIL").addClass("checkResultFail"));
		}
		widgets.add("] ");

		widgets.add(checkResult.getDescription());
		widgets.add(" (");
		widgets.add(checkResult.getName());
		widgets.add(") ");
		if (Boolean.FALSE.equals(checkResult.getResult())) {
			widgets.add("(");
			widgets.add(checkResult.getMessage() != null ? checkResult.getMessage() : "-");
			widgets.add(")");
			widgets.add(" ");
		}
		widgets.add(monitoringGuiLinkFactory.nodeSilent(request, checkResult.getId()));
		return widgets;
	}
}
