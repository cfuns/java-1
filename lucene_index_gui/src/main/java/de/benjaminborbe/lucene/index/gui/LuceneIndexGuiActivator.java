package de.benjaminborbe.lucene.index.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.lucene.index.gui.guice.LuceneIndexGuiModules;
import de.benjaminborbe.lucene.index.gui.servlet.LuceneIndexGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class LuceneIndexGuiActivator extends HttpBundleActivator {

	@Inject
	private LuceneIndexGuiServlet indexGuiServlet;

	public LuceneIndexGuiActivator() {
		super(LuceneIndexGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new LuceneIndexGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(indexGuiServlet, "/"));
		return result;
	}

}
