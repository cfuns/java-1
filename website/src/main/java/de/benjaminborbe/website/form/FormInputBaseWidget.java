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

	private String value;

	private boolean br = true;

	public FormInputBaseWidget(final String type, final String name) {
		super(TAG);
		addAttribute("name", name);
		addAttribute("type", type);
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final String value = this.value != null ? this.value : (request.getParameter(getName()) != null ? request.getParameter(getName()) : defaultValue);
		addAttribute("value", value);
		{
			final ListWidget widgets = new ListWidget();
			if (label != null) {
				widgets.add(new TagWidget("label", label).addAttribute("for", getName()));
			}
			widgets.render(request, response, context);
		}
		super.render(request, response, context);
		{
			if (br) {
				final ListWidget widgets = new ListWidget();
				widgets.add(new BrWidget());
				widgets.render(request, response, context);
			}
		}
	}

	public FormInputBaseWidget setBr(final boolean br) {
		this.br = br;
		return this;
	}

	@Override
	public FormInputBaseWidget addLabel(final String label) {
		this.label = label;
		return this;
	}

	@Override
	public FormInputBaseWidget addDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue != null ? String.valueOf(defaultValue) : null;
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

	@Override
	public FormInputBaseWidget addValue(final Object value) {
		this.value = value != null ? String.valueOf(value) : null;
		return this;
	}

}
