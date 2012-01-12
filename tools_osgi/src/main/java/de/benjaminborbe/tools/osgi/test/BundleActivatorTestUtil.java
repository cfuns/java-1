package de.benjaminborbe.tools.osgi.test;

import org.easymock.EasyMock;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

public class BundleActivatorTestUtil {

	public ExtHttpServiceMock startBundle(final BundleActivator bundleActivator) throws Exception {
		final long bundleId = 1337l;

		final Bundle bundle = EasyMock.createMock(Bundle.class);
		EasyMock.expect(bundle.getBundleId()).andReturn(bundleId);
		EasyMock.replay(bundle);

		final Filter filter = EasyMock.createMock(Filter.class);
		EasyMock.replay(filter);

		final ExtHttpServiceMock extBundle = new ExtHttpServiceMock();

		final ServiceReference serviceReference = EasyMock.createMock(ServiceReference.class);
		EasyMock.expect(serviceReference.getBundle()).andReturn(extBundle);
		EasyMock.replay(serviceReference);

		final ServiceRegistration serviceRegistration = EasyMock.createMock(ServiceRegistration.class);
		EasyMock.replay(serviceRegistration);

		final ServiceReference[] serviceReferences = new ServiceReference[] { serviceReference };

		final BundleContext context = EasyMock.createMock(BundleContext.class);
		context.addServiceListener(EasyMock.anyObject(ServiceListener.class), EasyMock.anyObject(String.class));
		EasyMock.expect(context.getBundle()).andReturn(bundle);
		EasyMock.expect(context.createFilter(EasyMock.anyObject(String.class))).andReturn(filter).anyTimes();
		EasyMock.expect(context.getServiceReferences(EasyMock.anyObject(String.class), EasyMock.anyObject(String.class))).andReturn(serviceReferences).anyTimes();
		EasyMock.expect(context.getService(serviceReference)).andReturn(extBundle);
		EasyMock.replay(context);

		bundleActivator.start(context);
		return extBundle;
	}
}
