package de.benjaminborbe.website.form;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.SingleTagWidget;
import de.benjaminborbe.website.util.TagWidget;
import de.benjaminborbe.website.widget.BrWidget;

public class FormInputBaseWidget extends SingleTagWidget implements FormInputWidget<FormInputBaseWidget> {

	private static final String TAG = "input";

	private String label;

	private String defaultValue;

	public FormInputBaseWidget(final String type, final String name) {
		super(TAG);
		addAttribute("name", name);
		addAttribute("type", type);
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		addAttribute("value", request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		{
			final ListWidget widgets = new ListWidget();
			if (label != null) {
				widgets.add(new TagWidget("label", label).addAttribute("for", getName()));
			}
			widgets.render(request, response, context);
		}
		super.render(request, response, context);
		{
			final ListWidget widgets = new ListWidget();
			widgets.add(new BrWidget());
			widgets.render(request, response, context);
		}
	}

	@Override
	public FormInputBaseWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public FormInputBaseWidget addDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	@Override
	public FormInputBaseWidget addPlaceholder(final String placeholder) {
		addAttribute("placeholder", placeholder);
		return this;
	}

	@Override
	public FormInputBaseWidget addId(final String id) {
		addAttribute("id", id);
		return this;
	}

	@Override
	public String getName() {
		return getAttribute("name");
	}

}
