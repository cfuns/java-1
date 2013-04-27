package de.benjaminborbe.note;

import de.benjaminborbe.note.api.NoteService;
import de.benjaminborbe.note.guice.NoteModules;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.BaseBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NoteActivator extends BaseBundleActivator {

	@Inject
	private NoteService noteService;

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NoteModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NoteService.class, noteService));
		return result;
	}

}
