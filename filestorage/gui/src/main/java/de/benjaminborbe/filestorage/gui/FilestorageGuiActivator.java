package de.benjaminborbe.filestorage.gui;

import de.benjaminborbe.filestorage.gui.guice.FilestorageGuiModules;
import de.benjaminborbe.filestorage.gui.servlet.FilestorageGuiDownloadServlet;
import de.benjaminborbe.filestorage.gui.servlet.FilestorageGuiUploadServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FilestorageGuiActivator extends HttpBundleActivator {

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
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(filestorageGuiUploadServlet, FilestorageGuiConstants.URL_UPLOAD));
		result.add(new ServletInfo(filestorageGuiDownloadServlet, FilestorageGuiConstants.URL_DOWNLOAD));
		return result;
	}

}
