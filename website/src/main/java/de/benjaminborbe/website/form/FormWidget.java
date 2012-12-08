package de.benjaminborbe.website.form;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.tools.html.Target;
import de.benjaminborbe.website.util.CompositeWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.TagWidget;

public class FormWidget extends CompositeWidget implements Widget, HasClass<FormWidget> {

	private final String action;

	private final ListWidget formInputWidgets = new ListWidget();

	private FormMethod method;

	private Target target;

	private FormEncType encType;

	private final Set<String> classes = new HashSet<String>();

	public FormWidget(final String action) {
		this.action = action;
	}

	public FormWidget() {
		this("");
	}

	public FormWidget addFormInputWidget(final FormElementWidget formInputWidget) {
		if (formInputWidget == null) {
			throw new NullPointerException("can't add null FormElementWidget");
		}
		formInputWidgets.add(formInputWidget);
		return this;
	}

	public FormWidget addMethod(final FormMethod method) {
		this.method = method;
		return this;
	}

	public FormWidget addTarget(final Target target) {
		this.target = target;
		return this;
	}

	public FormWidget addEncType(final FormEncType encType) {
		this.encType = encType;
		return this;
	}

	@Override
	public FormWidget addClass(final String clazz) {
		classes.add(clazz);
		return this;
	}

	@Override
	public FormWidget removeClass(final String clazz) {
		classes.remove(clazz);
		return this;
	}

	@Override
	public Collection<String> getClasses() {
		return classes;
	}

	@Override
	protected Widget createWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws Exception {
		final TagWidget form = new TagWidget("form");
		if (encType != null) {
			form.addAttribute("enctype", encType);
		}
		if (target != null) {
			form.addAttribute("target", target);
		}
		if (method != null) {
			form.addAttribute("method", method);
		}
		if (action != null) {
			form.addAttribute("action", action);
		}
		if (classes.size() > 0) {
			form.addAttribute("class", StringUtils.join(classes, " "));
		}
		final TagWidget fieldset = new TagWidget("fieldset", formInputWidgets);
		form.addContent(fieldset);
		return form;
	}
}
