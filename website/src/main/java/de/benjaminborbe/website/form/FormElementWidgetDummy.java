package de.benjaminborbe.website.form;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.widget.BrWidget;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FormElementWidgetDummy extends CompositeWidget implements FormElementWidget, HasLabel<FormElementWidgetDummy> {

	private String label;

	private String content;

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final ListWidget widgets = new ListWidget();
		if (label != null) {
			widgets.add(new TagWidget("label", label));
		}
		widgets.add(new SpanWidget(content));
		widgets.add(new BrWidget());
		return widgets;
	}

	@Override
	public FormElementWidgetDummy addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public FormElementWidgetDummy addContent(final String content) {
		this.content = content;
		return this;
	}
}
