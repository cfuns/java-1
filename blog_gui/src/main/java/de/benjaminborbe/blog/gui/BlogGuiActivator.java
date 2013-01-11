package de.benjaminborbe.blog.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.blog.gui.guice.BlogGuiModules;
import de.benjaminborbe.blog.gui.servlet.BlogGuiAtomServlet;
import de.benjaminborbe.blog.gui.servlet.BlogGuiCreatePostServlet;
import de.benjaminborbe.blog.gui.servlet.BlogGuiDeletePostServlet;
import de.benjaminborbe.blog.gui.servlet.BlogGuiLatestPostsServlet;
import de.benjaminborbe.blog.gui.servlet.BlogGuiUpdatePostServlet;
import de.benjaminborbe.blog.gui.util.BlogGuiNavigationEntry;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class BlogGuiActivator extends HttpBundleActivator {

	@Inject
	private BlogGuiAtomServlet blogGuiAtomServlet;

	@Inject
	private BlogGuiUpdatePostServlet blogGuiUpdatePostServlet;

	@Inject
	private BlogGuiLatestPostsServlet blogGuiLatestPostsServlet;

	@Inject
	private BlogGuiCreatePostServlet blogGuiAddPostServlet;

	@Inject
	private BlogGuiDeletePostServlet blogGuiDeletePostServlet;

	@Inject
	private BlogGuiNavigationEntry blogGuiNavigationEntry;

	public BlogGuiActivator() {
		super(BlogGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new BlogGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(blogGuiLatestPostsServlet, BlogGuiConstants.URL_HOME));
		result.add(new ServletInfo(blogGuiAddPostServlet, BlogGuiConstants.POST_ADD_URL));
		result.add(new ServletInfo(blogGuiDeletePostServlet, BlogGuiConstants.POST_DELETE_URL));
		result.add(new ServletInfo(blogGuiUpdatePostServlet, BlogGuiConstants.POST_UPDATE_URL));
		result.add(new ServletInfo(blogGuiAtomServlet, BlogGuiConstants.ATOM_URL));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, blogGuiNavigationEntry));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/images", "images"));
		result.add(new ResourceInfo("/css", "css"));
		result.add(new ResourceInfo("/js", "js"));
		return result;
	}
}
