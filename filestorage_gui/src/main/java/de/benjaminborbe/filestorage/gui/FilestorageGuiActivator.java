package de.benjaminborbe.filestorage.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.filestorage.gui.guice.FilestorageGuiModules;
import de.benjaminborbe.filestorage.gui.servlet.FilestorageGuiDownloadServlet;
import de.benjaminborbe.filestorage.gui.servlet.FilestorageGuiServlet;
import de.benjaminborbe.filestorage.gui.servlet.FilestorageGuiUploadServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class FilestorageGuiActivator extends HttpBundleActivator {

	@Inject
	private FilestorageGuiServlet filestorageGuiServlet;

	@Inject
	private FilestorageGuiUploadServlet filestorageGuiUploadServlet;

	@Inject
	private FilestorageGuiDownloadServlet filestorageGuiDownloadServlet;

	public FilestorageGuiActivator() {
		super(FilestorageGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new FilestorageGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(filestorageGuiServlet, "/"));
		result.add(new ServletInfo(filestorageGuiUploadServlet, "/upload"));
		result.add(new ServletInfo(filestorageGuiDownloadServlet, "/download"));
		return result;
	}


}
