package com.glavsoft.viewer.swing.gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.slf4j.Logger;

import com.glavsoft.core.SettingsChangedEvent;
import com.glavsoft.rfb.IChangeSettingsListener;
import com.glavsoft.rfb.protocol.ProtocolSettings;
import com.glavsoft.utils.Strings;
import com.glavsoft.viewer.swing.ConnectionParams;

/**
 * @author dime at tightvnc.com
 */
public class ConnectionsHistory implements IChangeSettingsListener {

	private static int MAX_ITEMS = 32;

	public static final String CONNECTIONS_HISTORY_ROOT_NODE = "com/glavsoft/viewer/connectionsHistory";

	public static final String NODE_HOST_NAME = "hostName";

	public static final String NODE_PORT_NUMBER = "portNumber";

	public static final String NODE_SSH_USER_NAME = "sshUserName";

	public static final String NODE_SSH_HOST_NAME = "sshHostName";

	public static final String NODE_SSH_PORT_NUMBER = "sshPortNumber";

	public static final String NODE_USE_SSH = "useSsh";

	public static final String NODE_PROTOCOL_SETTINGS = "protocolSettings";

	private final Map<ConnectionParams, ProtocolSettings> settingsMap;

	private final LinkedList<ConnectionParams> connections;

	private final ConnectionParams workingConnectionParams;

	private final Logger logger;

	public ConnectionsHistory(final Logger logger, final ConnectionParams workingConnectionParams) {
		this.logger = logger;
		this.workingConnectionParams = workingConnectionParams;
		settingsMap = new HashMap<ConnectionParams, ProtocolSettings>();
		connections = new LinkedList<ConnectionParams>();
		retrieve();
	}

	private void retrieve() {
		final Preferences root = Preferences.userRoot();
		final Preferences connectionsHistoryNode = root.node(CONNECTIONS_HISTORY_ROOT_NODE);
		try {
			final byte[] emptyByteArray = new byte[0];
			final String[] orderNums;
			orderNums = connectionsHistoryNode.childrenNames();
			final SortedMap<Integer, ConnectionParams> conns = new TreeMap<Integer, ConnectionParams>();
			final HashSet<ConnectionParams> uniques = new HashSet<ConnectionParams>();
			for (final String orderNum : orderNums) {
				int num = 0;
				try {
					num = Integer.parseInt(orderNum);
				}
				catch (final NumberFormatException skip) {
					// nop
				}
				final Preferences node = connectionsHistoryNode.node(orderNum);
				final String hostName = node.get(NODE_HOST_NAME, null);
				if (null == hostName)
					continue; // skip entries without hostName field
				final ConnectionParams cp = new ConnectionParams(hostName, node.getInt(NODE_PORT_NUMBER, 0), node.getBoolean(NODE_USE_SSH, false), node.get(NODE_SSH_HOST_NAME, ""),
						node.getInt(NODE_SSH_PORT_NUMBER, 0), node.get(NODE_SSH_USER_NAME, ""));
				if (uniques.contains(cp))
					continue; // skip duplicates
				uniques.add(cp);
				conns.put(num, cp);
				final byte[] bytes = node.getByteArray(NODE_PROTOCOL_SETTINGS, emptyByteArray);
				if (bytes.length != 0) {
					try {
						final ProtocolSettings settings = (ProtocolSettings) (new ObjectInputStream(new ByteArrayInputStream(bytes))).readObject();
						settings.refine();
						settingsMap.put(cp, settings);
					}
					catch (final IOException e) {
						logger.debug("Cannot deserialize ProtocolSettings: " + e.getMessage());
					}
					catch (final ClassNotFoundException e) {
						logger.debug("Cannot deserialize ProtocolSettings : " + e.getMessage());
					}
				}
			}
			int itemsCount = 0;
			for (final ConnectionParams cp : conns.values()) {
				if (itemsCount < MAX_ITEMS) {
					connections.add(cp);
				}
				else {
					connectionsHistoryNode.node(cp.hostName).removeNode();
				}
				++itemsCount;
			}
		}
		catch (final BackingStoreException e) {
			logger.debug("Cannot retrieve connections history info: " + e.getMessage());
		}
	}

	public LinkedList<ConnectionParams> getConnectionsList() {
		return connections;
	}

	public ProtocolSettings getSettings(final ConnectionParams cp) {
		return settingsMap.get(cp);
	}

	public void save() {
		final Preferences root = Preferences.userRoot();
		final Preferences connectionsHistoryNode = root.node(CONNECTIONS_HISTORY_ROOT_NODE);
		final String[] hosts;
		try {
			hosts = connectionsHistoryNode.childrenNames();
			for (final String host : hosts) {
				connectionsHistoryNode.node(host).removeNode();
			}
		}
		catch (final BackingStoreException e) {
			logger.debug("Cannot remove node: " + e.getMessage());
		}
		int num = 0;
		for (final ConnectionParams cp : connections) {
			if (num >= MAX_ITEMS)
				break;
			if (!Strings.isTrimmedEmpty(cp.hostName)) {
				addNode(cp, connectionsHistoryNode, num++);
			}
		}
	}

	private void addNode(final ConnectionParams connectionParams, final Preferences connectionsHistoryNode, final int orderNum) {
		final ProtocolSettings settings = settingsMap.get(connectionParams);
		final Preferences node = connectionsHistoryNode.node(String.valueOf(orderNum));
		node.put(NODE_HOST_NAME, connectionParams.hostName);
		node.putInt(NODE_PORT_NUMBER, connectionParams.getPortNumber());
		if (connectionParams.useSsh()) {
			node.putBoolean(NODE_USE_SSH, connectionParams.useSsh());
			node.put(NODE_SSH_USER_NAME, connectionParams.sshUserName != null ? connectionParams.sshUserName : "");
			node.put(NODE_SSH_HOST_NAME, connectionParams.sshHostName != null ? connectionParams.sshHostName : "");
			node.putInt(NODE_SSH_PORT_NUMBER, connectionParams.getSshPortNumber());
		}
		if (settings != null) {
			try {
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
				objectOutputStream.writeObject(settings);
				node.putByteArray(NODE_PROTOCOL_SETTINGS, byteArrayOutputStream.toByteArray());
			}
			catch (final IOException e) {
				logger.debug("Cannot serialize ProtocolSettings: " + e.getMessage());
			}
		}
		try {
			node.flush();
		}
		catch (final BackingStoreException e) {
			logger.debug("Cannot retrieve connections history info: " + e.getMessage());
		}
	}

	void reorderConnectionsList(final ConnectionParams connectionParams, final ProtocolSettings settings) {
		while (connections.remove(connectionParams)) {/* empty - remove all occurrence */
		}
		final LinkedList<ConnectionParams> cpList = new LinkedList<ConnectionParams>();
		cpList.addAll(connections);

		connections.clear();
		connections.add(new ConnectionParams(connectionParams));
		connections.addAll(cpList);
		storeSettings(connectionParams, settings);
	}

	private void storeSettings(final ConnectionParams connectionParams, final ProtocolSettings settings) {
		final ProtocolSettings savedSettings = settingsMap.get(connectionParams);
		if (savedSettings != null) {
			savedSettings.copySerializedFieldsFrom(settings);
		}
		else {
			settingsMap.put(new ConnectionParams(connectionParams), new ProtocolSettings(logger, settings));
		}
	}

	public ConnectionParams getMostSuitableConnection(final ConnectionParams orig) {
		ConnectionParams res = connections.isEmpty() ? orig : connections.get(0);
		if (null == orig || null == orig.hostName)
			return res;
		for (final ConnectionParams cp : connections) {
			if (orig.equals(cp))
				return cp;
			if (compareTextFields(orig.hostName, res.hostName, cp.hostName)) {
				res = cp;
				continue;
			}
			if (orig.hostName.equals(cp.hostName) && comparePorts(orig.getPortNumber(), res.getPortNumber(), cp.getPortNumber())) {
				res = cp;
				continue;
			}
			if (orig.hostName.equals(cp.hostName) && orig.getPortNumber() == cp.getPortNumber() && orig.useSsh() == cp.useSsh() && orig.useSsh() != res.useSsh()) {
				res = cp;
				continue;
			}
			if (orig.hostName.equals(cp.hostName) && orig.getPortNumber() == cp.getPortNumber() && orig.useSsh() && cp.useSsh()
					&& compareTextFields(orig.sshHostName, res.sshHostName, cp.sshHostName)) {
				res = cp;
				continue;
			}
			if (orig.hostName.equals(cp.hostName) && orig.getPortNumber() == cp.getPortNumber() && orig.useSsh() && cp.useSsh() && orig.sshHostName != null
					&& orig.sshHostName.equals(cp.hostName) && comparePorts(orig.getSshPortNumber(), res.getSshPortNumber(), cp.getSshPortNumber())) {
				res = cp;
				continue;
			}
			if (orig.hostName.equals(cp.hostName) && orig.getPortNumber() == cp.getPortNumber() && orig.useSsh() && cp.useSsh() && orig.sshHostName != null
					&& orig.sshHostName.equals(cp.hostName) && orig.getSshPortNumber() == cp.getSshPortNumber() && compareTextFields(orig.sshUserName, res.sshUserName, cp.sshUserName)) {
				res = cp;
			}
		}
		return res;
	}

	private boolean comparePorts(final int orig, final int res, final int test) {
		return orig == test && orig != res;
	}

	private boolean compareTextFields(final String orig, final String res, final String test) {
		return (orig != null && test != null && res != null) && orig.equals(test) && !orig.equals(res);
	}

	@Override
	public void settingsChanged(final SettingsChangedEvent event) {
		storeSettings(workingConnectionParams, (ProtocolSettings) event.getSource());
		save();
	}
}
