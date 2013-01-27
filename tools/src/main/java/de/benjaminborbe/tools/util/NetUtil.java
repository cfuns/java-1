package de.benjaminborbe.tools.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

public class NetUtil {

	@Inject
	public NetUtil() {
	}

	public String getHostname() throws SocketException {
		String lastHostname = null;
		for (final String hostname : getHostnames()) {
			if (hostname.indexOf('.') != -1) {
				return hostname;
			}
			lastHostname = hostname;
		}
		return lastHostname;
	}

	public Collection<String> getHostnames() throws SocketException {
		final Set<String> result = new HashSet<String>();
		final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			final NetworkInterface nic = interfaces.nextElement();
			final Enumeration<InetAddress> addresses = nic.getInetAddresses();
			while (addresses.hasMoreElements()) {
				final InetAddress address = addresses.nextElement();
				if (!address.isLoopbackAddress()) {
					final String hostname = address.getHostName();
					if (hostname != null) {
						result.add(hostname);
					}
				}
			}
		}
		return result;
	}
}
