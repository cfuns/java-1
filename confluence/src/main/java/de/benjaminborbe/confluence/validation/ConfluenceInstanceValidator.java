package de.benjaminborbe.confluence.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.validation.Validator;

public class ConfluenceInstanceValidator implements Validator<ConfluenceInstanceBean> {

	private final UrlUtil urlUtil;

	@Inject
	public ConfluenceInstanceValidator(final UrlUtil urlUtil) {
		this.urlUtil = urlUtil;
	}

	@Override
	public Class<ConfluenceInstanceBean> getType() {
		return ConfluenceInstanceBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final Object object) {
		final ConfluenceInstanceBean bean = (ConfluenceInstanceBean) object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		final String url = bean.getUrl();
		{
			if (url == null || url.length() == 0) {
				result.add(new ValidationErrorSimple("url missing"));
			}
			else if (!urlUtil.isUrl(url)) {
				result.add(new ValidationErrorSimple("url invalid"));
			}

		}

		return result;
	}

}
