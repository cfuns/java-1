package de.benjaminborbe.monitoring.check;

import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

public class CheckLinker {

	private CheckLinker() {

	}

	@Inject
	public static void link(final Injector injector, final Logger logger, final CheckRegistry registry) {
		final List<Binding<Check>> bindings = injector.findBindingsByType(TypeLiteral.get(Check.class));
		for (final Binding<Check> binding : bindings) {
			logger.trace("CheckLinker.link() - bind: " + binding);
			registry.register(binding.getProvider().get());
		}
	}

}
