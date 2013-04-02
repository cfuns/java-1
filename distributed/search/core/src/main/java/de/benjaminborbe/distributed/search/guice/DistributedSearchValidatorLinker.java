package de.benjaminborbe.distributed.search.guice;

import com.google.inject.Inject;
import de.benjaminborbe.distributed.search.dao.DistributedSearchPageValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class DistributedSearchValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final DistributedSearchPageValidator a) {
		validatorRegistry.register(a);
	}
}
