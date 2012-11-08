package de.benjaminborbe.website.form;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.website.util.SingleTagWidget;

public class FormCheckboxWidget implements FormElementWidget, HasName<FormCheckboxWidget>, HasOnClick<FormCheckboxWidget> {

	private String name;

	private String onclick;

	public FormCheckboxWidget(final String name) {
		this.name = name;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		final SingleTagWidget widget = new SingleTagWidget("input");
		widget.addAttribute("type", "checkbox");
		widget.addAttribute("name", name);
		if (onclick != null) {
			widget.addAttribute("onclick", onclick);
		}
		widget.render(request, response, context);
	}

	@Override
	public FormCheckboxWidget addName(final String name) {
		this.name = name;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public FormCheckboxWidget addOnClick(final String onclick) {
		this.onclick = onclick;
		return this;
	}

	@Override
	public String getOnClick() {
		return onclick;
	}
}
