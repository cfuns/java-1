package de.benjaminborbe.shortener.dao;

import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;
import de.benjaminborbe.storage.tools.Dao;

public interface ShortenerUrlDao extends Dao<ShortenerUrlBean, ShortenerUrlIdentifier> {

}
