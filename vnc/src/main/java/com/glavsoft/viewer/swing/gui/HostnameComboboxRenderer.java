package com.glavsoft.viewer.swing.gui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import com.glavsoft.viewer.swing.ConnectionParams;

/**
 * @author dime at tightvnc.com
 */
public class HostnameComboboxRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = -3195050988105070588L;

	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
		final String stringValue = renderListItem((ConnectionParams) value);
		setText(stringValue);
		setFont(getFont().deriveFont(Font.PLAIN));
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}

	public String renderListItem(final ConnectionParams cp) {
		String s = "<html><b>" + cp.hostName + "</b>:" + cp.getPortNumber();
		if (cp.useSsh()) {
			s += " <i>(via ssh://" + cp.sshUserName + "@" + cp.sshHostName + ":" + cp.getSshPortNumber() + ")</i>";
		}
		return s + "</html>";
	}
}
