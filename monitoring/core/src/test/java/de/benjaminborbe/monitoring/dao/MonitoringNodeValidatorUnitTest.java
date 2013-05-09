package de.benjaminborbe.monitoring.dao;

import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;
import de.benjaminborbe.monitoring.check.MonitoringCheckHttp;
import de.benjaminborbe.monitoring.check.MonitoringCheckNop;
import de.benjaminborbe.monitoring.check.MonitoringCheckRegistry;
import de.benjaminborbe.monitoring.check.MonitoringCheckRemote;
import de.benjaminborbe.monitoring.check.MonitoringCheckTcp;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.json.JSONParserSimple;
import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Base64UtilImpl;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MonitoringNodeValidatorUnitTest {

	@Test
	public void testValidateInactive() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		final DurationUtil durationUtil = new DurationUtil(null);
		final Base64Util base64Util = new Base64UtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		final MonitoringCheckHttp monitoringCheckHttp = new MonitoringCheckHttp(logger, parseUtil, validationConstraintValidator, httpdownloaderService, httpUtil);
		final MonitoringCheckNop monitoringCheckNop = new MonitoringCheckNop();
		final MonitoringCheckTcp monitoringCheckTcp = new MonitoringCheckTcp(logger, parseUtil, validationConstraintValidator);
		final UrlUtil urlUtil = new UrlUtilImpl();
		final JSONParser jsonParserSimple = new JSONParserSimple();
		final MonitoringCheckRemote monitoringCheckRemote = new MonitoringCheckRemote(logger, parseUtil, jsonParserSimple, validationConstraintValidator, urlUtil, httpdownloaderService, httpUtil);
		final MonitoringCheckRegistry monitoringCheckFactory = new MonitoringCheckRegistry(monitoringCheckHttp, monitoringCheckNop, monitoringCheckTcp, monitoringCheckRemote);

		final MonitoringNodeDao monitoringNodeDao = EasyMock.createMock(MonitoringNodeDao.class);
		EasyMock.expect(monitoringNodeDao.exists(new MonitoringNodeIdentifier("23"))).andReturn(true);
		EasyMock.replay(monitoringNodeDao);

		final MonitoringCheckIdentifier type = EasyMock.createMock(MonitoringCheckIdentifier.class);
		EasyMock.replay(type);

		final Object[] mocks = new Object[]{httpdownloaderService, httpUtil};
		EasyMock.replay(mocks);

		final MonitoringNodeValidator va = new MonitoringNodeValidator(validationConstraintValidator, monitoringNodeDao, monitoringCheckFactory);
		final MonitoringNodeBean bean = new MonitoringNodeBean();
		assertThat(va.validate(bean).size(), is(4));
		bean.setCheckType(type);
		assertThat(va.validate(bean).size(), is(3));
		bean.setId(new MonitoringNodeIdentifier("1337"));
		assertThat(va.validate(bean).size(), is(2));
		bean.setName("bla");
		assertThat(va.validate(bean).size(), is(1));
		bean.setParameter(new HashMap<String, String>());
		assertThat(va.validate(bean).size(), is(0));
		bean.setParentId(new MonitoringNodeIdentifier(null));
		assertThat(va.validate(bean).size(), is(1));
		bean.setParentId(new MonitoringNodeIdentifier("23"));
		assertThat(va.validate(bean).size(), is(0));

		EasyMock.verify(mocks);
	}

	@Test
	public void testParentIdNotId() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		final DurationUtil durationUtil = new DurationUtil(null);
		final Base64Util base64Util = new Base64UtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		final MonitoringCheckHttp monitoringCheckHttp = new MonitoringCheckHttp(logger, parseUtil, validationConstraintValidator, httpdownloaderService, httpUtil);
		final MonitoringCheckNop monitoringCheckNop = new MonitoringCheckNop();
		final MonitoringCheckTcp monitoringCheckTcp = new MonitoringCheckTcp(logger, parseUtil, validationConstraintValidator);
		final UrlUtil urlUtil = new UrlUtilImpl();
		final JSONParser jsonParserSimple = new JSONParserSimple();
		final MonitoringCheckRemote monitoringCheckRemote = new MonitoringCheckRemote(logger, parseUtil, jsonParserSimple, validationConstraintValidator, urlUtil, httpdownloaderService, httpUtil);
		final MonitoringCheckRegistry monitoringCheckFactory = new MonitoringCheckRegistry(monitoringCheckHttp, monitoringCheckNop, monitoringCheckTcp, monitoringCheckRemote);

		final MonitoringNodeDao monitoringNodeDao = EasyMock.createMock(MonitoringNodeDao.class);
		EasyMock.expect(monitoringNodeDao.exists(new MonitoringNodeIdentifier("1336"))).andReturn(true);
		EasyMock.expect(monitoringNodeDao.exists(new MonitoringNodeIdentifier("1337"))).andReturn(true);
		EasyMock.replay(monitoringNodeDao);

		final MonitoringCheckIdentifier type = EasyMock.createMock(MonitoringCheckIdentifier.class);
		EasyMock.replay(type);

		final Object[] mocks = new Object[]{httpdownloaderService, httpUtil};
		EasyMock.replay(mocks);

		final MonitoringNodeValidator va = new MonitoringNodeValidator(validationConstraintValidator, monitoringNodeDao, monitoringCheckFactory);
		final MonitoringNodeBean bean = new MonitoringNodeBean();
		assertThat(va.validate(bean).size(), is(4));
		bean.setCheckType(type);
		assertThat(va.validate(bean).size(), is(3));
		bean.setId(new MonitoringNodeIdentifier("1337"));
		assertThat(va.validate(bean).size(), is(2));
		bean.setName("bla");
		assertThat(va.validate(bean).size(), is(1));
		bean.setParameter(new HashMap<String, String>());
		assertThat(va.validate(bean).size(), is(0));
		bean.setParentId(new MonitoringNodeIdentifier("1337"));
		assertThat(va.validate(bean).size(), is(1));
		bean.setParentId(new MonitoringNodeIdentifier("1336"));
		assertThat(va.validate(bean).size(), is(0));

		EasyMock.verify(mocks);
	}

	@Test
	public void testParentIdExists() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		final DurationUtil durationUtil = new DurationUtil(null);
		final Base64Util base64Util = new Base64UtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final HttpdownloaderService httpdownloaderService = EasyMock.createMock(HttpdownloaderService.class);
		final HttpUtil httpUtil = EasyMock.createMock(HttpUtil.class);
		final MonitoringCheckHttp monitoringCheckHttp = new MonitoringCheckHttp(logger, parseUtil, validationConstraintValidator, httpdownloaderService, httpUtil);
		final MonitoringCheckNop monitoringCheckNop = new MonitoringCheckNop();
		final MonitoringCheckTcp monitoringCheckTcp = new MonitoringCheckTcp(logger, parseUtil, validationConstraintValidator);
		final UrlUtil urlUtil = new UrlUtilImpl();
		final JSONParser jsonParserSimple = new JSONParserSimple();
		final MonitoringCheckRemote monitoringCheckRemote = new MonitoringCheckRemote(logger, parseUtil, jsonParserSimple, validationConstraintValidator, urlUtil, httpdownloaderService, httpUtil);
		final MonitoringCheckRegistry monitoringCheckFactory = new MonitoringCheckRegistry(monitoringCheckHttp, monitoringCheckNop, monitoringCheckTcp, monitoringCheckRemote);

		final MonitoringNodeDao monitoringNodeDao = EasyMock.createMock(MonitoringNodeDao.class);
		EasyMock.expect(monitoringNodeDao.exists(new MonitoringNodeIdentifier("23"))).andReturn(false);
		EasyMock.expect(monitoringNodeDao.exists(new MonitoringNodeIdentifier("42"))).andReturn(true);
		EasyMock.replay(monitoringNodeDao);

		final MonitoringCheckIdentifier type = EasyMock.createMock(MonitoringCheckIdentifier.class);
		EasyMock.replay(type);

		final Object[] mocks = new Object[]{httpdownloaderService, httpUtil};
		EasyMock.replay(mocks);

		final MonitoringNodeValidator va = new MonitoringNodeValidator(validationConstraintValidator, monitoringNodeDao, monitoringCheckFactory);
		final MonitoringNodeBean bean = new MonitoringNodeBean();
		assertThat(va.validate(bean).size(), is(4));
		bean.setCheckType(type);
		assertThat(va.validate(bean).size(), is(3));
		bean.setId(new MonitoringNodeIdentifier("1337"));
		assertThat(va.validate(bean).size(), is(2));
		bean.setName("bla");
		assertThat(va.validate(bean).size(), is(1));
		bean.setParameter(new HashMap<String, String>());
		assertThat(va.validate(bean).size(), is(0));
		bean.setParentId(new MonitoringNodeIdentifier("23"));
		assertThat(va.validate(bean).size(), is(1));
		bean.setParentId(new MonitoringNodeIdentifier("42"));
		assertThat(va.validate(bean).size(), is(0));

		EasyMock.verify(mocks);
	}
}
