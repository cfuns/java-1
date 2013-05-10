package de.benjaminborbe.analytics.dao;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AnalyticsReportValidatorUnitTest {

	@Test
	public void testValidateName() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();

		final AnalyticsReportValidator va = new AnalyticsReportValidator(validationConstraintValidator);
		final AnalyticsReportBean bean = new AnalyticsReportBean();

		assertThat(va.validate(bean).size(), is(3));

		bean.setId(new AnalyticsReportIdentifier("1337"));
		assertThat(va.validate(bean).size(), is(2));

		bean.setAggregation(AnalyticsReportAggregation.SUM);
		assertThat(va.validate(bean).size(), is(1));

		bean.setName("testReport");
		assertThat(va.validate(bean).size(), is(0));

		bean.setName("testReport1");
		assertThat(va.validate(bean).size(), is(0));

		bean.setName("testReport" + AnalyticsReportDao.SEPERATOR);
		assertThat(va.validate(bean).size(), is(1));

		bean.setName("test-test-test");
		assertThat(va.validate(bean).size(), is(0));

	}
}
