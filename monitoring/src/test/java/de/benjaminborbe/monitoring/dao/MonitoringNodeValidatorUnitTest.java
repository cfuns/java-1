package de.benjaminborbe.monitoring.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.monitoring.api.MonitoringCheckType;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.check.MonitoringCheckFactory;
import de.benjaminborbe.monitoring.check.MonitoringCheckHttp;
import de.benjaminborbe.monitoring.check.MonitoringCheckNop;
import de.benjaminborbe.monitoring.check.MonitoringCheckTcp;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderImpl;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.tools.util.StreamUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;

public class MonitoringNodeValidatorUnitTest {

	@Test
	public void testValidateInactive() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();

		final StreamUtil streamUtil = new StreamUtil();
		final DurationUtil durationUtil = new DurationUtil(null);
		final Base64Util base64Util = new Base64UtilImpl();
		final HttpDownloader a = new HttpDownloaderImpl(logger, streamUtil, durationUtil, base64Util);
		final HttpDownloadUtil b = new HttpDownloadUtil(logger);
		final ParseUtil c = new ParseUtilImpl();
		final MonitoringCheckHttp monitoringCheckHttp = new MonitoringCheckHttp(logger, a, b, c, validationConstraintValidator);
		final MonitoringCheckNop monitoringCheckNop = new MonitoringCheckNop();
		final MonitoringCheckTcp monitoringCheckTcp = new MonitoringCheckTcp(logger, c, validationConstraintValidator);

		final MonitoringCheckFactory monitoringCheckFactory = new MonitoringCheckFactory(monitoringCheckHttp, monitoringCheckNop, monitoringCheckTcp);
		final MonitoringNodeValidator va = new MonitoringNodeValidator(validationConstraintValidator, monitoringCheckFactory);
		final MonitoringNodeBean bean = new MonitoringNodeBean();
		assertThat(va.validate(bean).size(), is(4));
		bean.setCheckType(MonitoringCheckType.NOP);
		assertThat(va.validate(bean).size(), is(3));
		bean.setId(new MonitoringNodeIdentifier("1337"));
		assertThat(va.validate(bean).size(), is(2));
		bean.setName("bla");
		assertThat(va.validate(bean).size(), is(1));
		bean.setParameter(new HashMap<String, String>());
		assertThat(va.validate(bean).size(), is(0));
		bean.setParentId(new MonitoringNodeIdentifier((String) null));
		assertThat(va.validate(bean).size(), is(1));
		bean.setParentId(new MonitoringNodeIdentifier("23"));
		assertThat(va.validate(bean).size(), is(0));
	}
}
