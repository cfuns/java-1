package de.benjaminborbe.selenium.configuration.xml.dao;

import com.google.inject.Provider;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class SeleniumConfigurationXmlDaoStorage extends DaoStorage<SeleniumConfigurationXmlBean, SeleniumConfigurationIdentifier> implements SeleniumConfigurationXmlDao {

	private static final String COLUMN_FAMILY = "selenium_xml";

	@Inject
	public SeleniumConfigurationXmlDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<SeleniumConfigurationXmlBean> beanProvider,
		final SeleniumConfigurationXmlBeanMapper mapper,
		final SeleniumConfigurationIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public Collection<SeleniumConfigurationIdentifier> list() throws StorageException {
		try {
			final List<SeleniumConfigurationIdentifier> result = new ArrayList<SeleniumConfigurationIdentifier>();
			final IdentifierIterator<SeleniumConfigurationIdentifier> i = getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (IdentifierIteratorException e) {
			throw new StorageException(e);
		}
	}
}
