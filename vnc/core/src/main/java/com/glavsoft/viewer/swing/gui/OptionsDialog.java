// Copyright (C) 2010, 2011, 2012 GlavSoft LLC.
// All rights reserved.
//
//-------------------------------------------------------------------------
// This file is part of the TightVNC software.  Please visit our Web site:
//
//                       http://www.tightvnc.com/
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation, Inc.,
// 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
//-------------------------------------------------------------------------
//

package com.glavsoft.viewer.swing.gui;

import com.glavsoft.rfb.encoding.EncodingType;
import com.glavsoft.rfb.protocol.LocalPointer;
import com.glavsoft.rfb.protocol.ProtocolSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Options dialog
 */
@SuppressWarnings({"serial"})
public class OptionsDialog extends JDialog {

	private JSlider jpegQuality;

	private JSlider compressionLevel;

	private JCheckBox viewOnlyCheckBox;

	private ProtocolSettings settings;

	private JCheckBox sharedSession;

	private RadioButtonSelectedState<LocalPointer> mouseCursorTrackSelected;

	private Map<LocalPointer, JRadioButton> mouseCursorTrackMap;

	private JCheckBox useCompressionLevel;

	private JCheckBox useJpegQuality;

	private JLabel jpegQualityPoorLabel;

	private JLabel jpegQualityBestLabel;

	private JLabel compressionLevelFastLabel;

	private JLabel compressionLevelBestLabel;

	private JCheckBox allowCopyRect;

	private JComboBox encodings;

	private JCheckBox disableClipboardTransfer;

	private JComboBox colorDepth;

	public OptionsDialog(final Window owner) {
		super(owner, "Connection Options", ModalityType.DOCUMENT_MODAL);
		final WindowAdapter onClose = new WindowAdapter() {

			@Override
			public void windowClosing(final WindowEvent e) {
				setVisible(false);
			}
		};
		addWindowListener(onClose);

		final JPanel optionsPane = new JPanel(new GridLayout(0, 2));
		add(optionsPane, BorderLayout.CENTER);

		optionsPane.add(createLeftPane());
		optionsPane.add(createRightPane());

		addButtons(onClose);

		pack();
	}

	public void initControlsFromSettings(final ProtocolSettings settings, final boolean isOnConnect) {
		this.settings = settings;

		viewOnlyCheckBox.setSelected(settings.isViewOnly());

		int i = 0;
		boolean isNotSetEncoding = true;
		while (encodings.getItemAt(i) != null) {
			final EncodingType item = ((EncodingSelectItem) encodings.getItemAt(i)).type;
			if (item.equals(settings.getPreferredEncoding())) {
				encodings.setSelectedIndex(i);
				isNotSetEncoding = false;
				break;
			}
			++i;
		}
		if (isNotSetEncoding) {
			encodings.setSelectedItem(0);
		}

		sharedSession.setSelected(settings.isShared());
		sharedSession.setEnabled(isOnConnect);

		mouseCursorTrackMap.get(settings.getMouseCursorTrack()).setSelected(true);
		mouseCursorTrackSelected.setSelected(settings.getMouseCursorTrack());

		final int bpp = settings.getBitsPerPixel();
		i = 0;
		boolean isNotSet = true;
		while (colorDepth.getItemAt(i) != null) {
			final int itemBpp = ((ColorDepthSelectItem) colorDepth.getItemAt(i)).bpp;
			if (itemBpp == bpp) {
				colorDepth.setSelectedIndex(i);
				isNotSet = false;
				break;
			}
			++i;
		}
		if (isNotSet) {
			colorDepth.setSelectedItem(0);
		}

		useCompressionLevel.setSelected(settings.getCompressionLevel() > 0);
		compressionLevel.setValue(Math.abs(settings.getCompressionLevel()));
		setCompressionLevelPaneEnable();

		useJpegQuality.setSelected(settings.getJpegQuality() > 0);
		jpegQuality.setValue(Math.abs(settings.getJpegQuality()));
		setJpegQualityPaneEnable();

		allowCopyRect.setSelected(settings.isAllowCopyRect());
		disableClipboardTransfer.setSelected(!settings.isAllowClipboardTransfer());
	}

	private void setSettingsFromControls() {
		settings.setViewOnly(viewOnlyCheckBox.isSelected());
		settings.setPreferredEncoding(((EncodingSelectItem) encodings.getSelectedItem()).type);

		settings.setSharedFlag(sharedSession.isSelected());
		settings.setMouseCursorTrack(mouseCursorTrackSelected.getSelected());

		settings.setBitsPerPixel(((ColorDepthSelectItem) colorDepth.getSelectedItem()).bpp);

		settings.setCompressionLevel(useCompressionLevel.isSelected() ? compressionLevel.getValue() : -Math.abs(settings.getCompressionLevel()));
		settings.setJpegQuality(useJpegQuality.isSelected() ? jpegQuality.getValue() : -Math.abs(settings.getJpegQuality()));
		settings.setAllowCopyRect(allowCopyRect.isSelected());
		settings.setAllowClipboardTransfer(!disableClipboardTransfer.isSelected());
		settings.fireListeners();
	}

	private Component createLeftPane() {
		final Box box = Box.createVerticalBox();
		box.setAlignmentX(LEFT_ALIGNMENT);

		box.add(createEncodingsPanel());

		box.add(Box.createVerticalGlue());
		return box;
	}

	private Component createRightPane() {
		final Box box = Box.createVerticalBox();
		box.setAlignmentX(LEFT_ALIGNMENT);

		box.add(createRestrictionsPanel());
		// box.add(createDisplayPanel());
		box.add(createMouseCursorPanel());
		// box.add(createLocalShapePanel());

		sharedSession = new JCheckBox("Request shared session");
		box.add(new JPanel(new FlowLayout(FlowLayout.LEFT)).add(sharedSession));

		box.add(Box.createVerticalGlue());
		return box;
	}

	private JPanel createRestrictionsPanel() {
		final JPanel restrictionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// restrictionsPanel.setAlignmentX(LEFT_ALIGNMENT);
		restrictionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Restrictions"));

		final Box restrictionsBox = Box.createVerticalBox();
		restrictionsBox.setAlignmentX(LEFT_ALIGNMENT);
		restrictionsPanel.add(restrictionsBox);
		viewOnlyCheckBox = new JCheckBox("View only (inputs ignored)");
		viewOnlyCheckBox.setAlignmentX(LEFT_ALIGNMENT);
		restrictionsBox.add(viewOnlyCheckBox);

		disableClipboardTransfer = new JCheckBox("Disable clipboard transfer");
		disableClipboardTransfer.setAlignmentX(LEFT_ALIGNMENT);
		restrictionsBox.add(disableClipboardTransfer);

		return restrictionsPanel;
	}

	private JPanel createEncodingsPanel() {
		final JPanel encodingsPanel = new JPanel();
		encodingsPanel.setAlignmentX(LEFT_ALIGNMENT);
		encodingsPanel.setLayout(new BoxLayout(encodingsPanel, BoxLayout.Y_AXIS));
		encodingsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Format and Encodings"));

		final JPanel encPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		encPane.setAlignmentX(LEFT_ALIGNMENT);
		encPane.add(new JLabel("Preferred encoding: "));

		encodings = new JComboBox();
		encodings.addItem(new EncodingSelectItem(EncodingType.TIGHT));
		encodings.addItem(new EncodingSelectItem(EncodingType.HEXTILE));

		// encodings.addItem(new EncodingSelectItem(EncodingType.RRE));
		// encodings.addItem(new EncodingSelectItem(EncodingType.ZLIB));

		encodings.addItem(new EncodingSelectItem(EncodingType.ZRLE));
		encodings.addItem(new EncodingSelectItem(EncodingType.RAW_ENCODING));
		encPane.add(encodings);
		encodingsPanel.add(encPane);

		encodingsPanel.add(createColorDepthPanel());

		addCompressionLevelPane(encodingsPanel);
		addJpegQualityLevelPane(encodingsPanel);

		allowCopyRect = new JCheckBox("Allow CopyRect encoding");
		allowCopyRect.setAlignmentX(LEFT_ALIGNMENT);
		encodingsPanel.add(allowCopyRect);

		return encodingsPanel;
	}

	private static class EncodingSelectItem {

		final EncodingType type;

		public EncodingSelectItem(final EncodingType type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type.getName();
		}
	}

	private static class ColorDepthSelectItem {

		final int bpp;

		final String title;

		public ColorDepthSelectItem(final int bpp, final String title) {
			this.bpp = bpp;
			this.title = title;
		}

		@Override
		public String toString() {
			return title;
		}
	}

	private JPanel createColorDepthPanel() {
		final JPanel colorDepthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		colorDepthPanel.setAlignmentX(LEFT_ALIGNMENT);
		colorDepthPanel.add(new JLabel("Color format: "));

		colorDepth = new JComboBox();
		colorDepth.addItem(new ColorDepthSelectItem(ProtocolSettings.BPP_SERVER_SETTINGS, "Server's default"));
		colorDepth.addItem(new ColorDepthSelectItem(ProtocolSettings.BPP_32, "16 777 216 colors"));
		colorDepth.addItem(new ColorDepthSelectItem(ProtocolSettings.BPP_16, "65 536 colors"));
		colorDepth.addItem(new ColorDepthSelectItem(ProtocolSettings.BPP_8, "256 colors"));
		colorDepth.addItem(new ColorDepthSelectItem(ProtocolSettings.BPP_6, "64 colors"));
		colorDepth.addItem(new ColorDepthSelectItem(ProtocolSettings.BPP_3, "8 colors"));

		colorDepthPanel.add(colorDepth);
		colorDepth.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				final ColorDepthSelectItem selectedItem = (ColorDepthSelectItem) colorDepth.getSelectedItem();
				setEnabled(selectedItem.bpp > ProtocolSettings.BPP_8 || selectedItem.bpp == ProtocolSettings.BPP_SERVER_SETTINGS, useJpegQuality);
				setEnabled(useJpegQuality.isSelected() && (selectedItem.bpp > ProtocolSettings.BPP_8 || selectedItem.bpp == ProtocolSettings.BPP_SERVER_SETTINGS), jpegQuality,
					jpegQualityPoorLabel, jpegQualityBestLabel);
			}
		});
		return colorDepthPanel;
	}

	private void addJpegQualityLevelPane(final JPanel encodingsPanel) {
		useJpegQuality = new JCheckBox("Allow JPEG, set quality level:");
		useJpegQuality.setAlignmentX(LEFT_ALIGNMENT);
		encodingsPanel.add(useJpegQuality);

		final JPanel jpegQualityPane = new JPanel();
		jpegQualityPane.setAlignmentX(LEFT_ALIGNMENT);
		jpegQualityPoorLabel = new JLabel("poor");
		jpegQualityPane.add(jpegQualityPoorLabel);
		jpegQuality = new JSlider(1, 9, 9);
		jpegQualityPane.add(jpegQuality);
		jpegQuality.setPaintTicks(true);
		jpegQuality.setMinorTickSpacing(1);
		jpegQuality.setMajorTickSpacing(1);
		jpegQuality.setPaintLabels(true);
		jpegQuality.setSnapToTicks(true);
		jpegQuality.setFont(jpegQuality.getFont().deriveFont((float) 8));
		jpegQualityBestLabel = new JLabel("best");
		jpegQualityPane.add(jpegQualityBestLabel);
		encodingsPanel.add(jpegQualityPane);

		jpegQualityPoorLabel.setFont(jpegQualityPoorLabel.getFont().deriveFont((float) 10));
		jpegQualityBestLabel.setFont(jpegQualityBestLabel.getFont().deriveFont((float) 10));

		useJpegQuality.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				setJpegQualityPaneEnable();
			}
		});
	}

	protected void setJpegQualityPaneEnable() {
		if (useJpegQuality != null && colorDepth != null) {
			final int bpp = ((ColorDepthSelectItem) colorDepth.getSelectedItem()).bpp;
			setEnabled(bpp > ProtocolSettings.BPP_8 || bpp == ProtocolSettings.BPP_SERVER_SETTINGS, useJpegQuality);
			setEnabled(useJpegQuality.isSelected() && (bpp > ProtocolSettings.BPP_8 || bpp == ProtocolSettings.BPP_SERVER_SETTINGS), jpegQuality, jpegQualityPoorLabel,
				jpegQualityBestLabel);
		}
	}

	private void addCompressionLevelPane(final JPanel encodingsPanel) {
		useCompressionLevel = new JCheckBox("Custom compression level:");
		useCompressionLevel.setAlignmentX(LEFT_ALIGNMENT);
		encodingsPanel.add(useCompressionLevel);

		final JPanel compressionLevelPane = new JPanel();
		compressionLevelPane.setAlignmentX(LEFT_ALIGNMENT);
		compressionLevelFastLabel = new JLabel("fast");
		compressionLevelPane.add(compressionLevelFastLabel);
		compressionLevel = new JSlider(1, 9, 1);
		compressionLevelPane.add(compressionLevel);
		compressionLevel.setPaintTicks(true);
		compressionLevel.setMinorTickSpacing(1);
		compressionLevel.setMajorTickSpacing(1);
		compressionLevel.setPaintLabels(true);
		compressionLevel.setSnapToTicks(true);
		compressionLevel.setFont(compressionLevel.getFont().deriveFont((float) 8));
		compressionLevelBestLabel = new JLabel("best");
		compressionLevelPane.add(compressionLevelBestLabel);
		encodingsPanel.add(compressionLevelPane);

		compressionLevelFastLabel.setFont(compressionLevelFastLabel.getFont().deriveFont((float) 10));
		compressionLevelBestLabel.setFont(compressionLevelBestLabel.getFont().deriveFont((float) 10));

		useCompressionLevel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				setEnabled(useCompressionLevel.isSelected(), compressionLevel, compressionLevelFastLabel, compressionLevelBestLabel);
			}
		});
		setCompressionLevelPaneEnable();
	}

	protected void setCompressionLevelPaneEnable() {
		setEnabled(useCompressionLevel.isSelected(), compressionLevel, compressionLevelFastLabel, compressionLevelBestLabel);
	}

	private void setEnabled(final boolean isEnabled, final JComponent... comp) {
		for (final JComponent c : comp) {
			c.setEnabled(isEnabled);
		}
	}

	private JPanel createMouseCursorPanel() {
		final JPanel mouseCursorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		mouseCursorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Mouse Cursor"));
		final Box mouseCursorBox = Box.createVerticalBox();
		mouseCursorPanel.add(mouseCursorBox);

		final ButtonGroup mouseCursorTrackGroup = new ButtonGroup();

		mouseCursorTrackSelected = new RadioButtonSelectedState<LocalPointer>();
		mouseCursorTrackMap = new HashMap<LocalPointer, JRadioButton>();

		addRadioButton("Track remote cursor locally", LocalPointer.ON, mouseCursorTrackSelected, mouseCursorTrackMap, mouseCursorBox, mouseCursorTrackGroup);
		addRadioButton("Let remote server deal with mouse cursor", LocalPointer.OFF, mouseCursorTrackSelected, mouseCursorTrackMap, mouseCursorBox, mouseCursorTrackGroup);
		addRadioButton("Don't show remote cursor", LocalPointer.HIDE, mouseCursorTrackSelected, mouseCursorTrackMap, mouseCursorBox, mouseCursorTrackGroup);
		return mouseCursorPanel;
	}

	private static class RadioButtonSelectedState<T> {

		private T state;

		public void setSelected(final T state) {
			this.state = state;
		}

		public T getSelected() {
			return state;
		}

	}

	private <T> JRadioButton addRadioButton(
		final String text, final T state, final RadioButtonSelectedState<T> selected, final Map<T, JRadioButton> state2buttonMap,
		final JComponent component, final ButtonGroup group
	) {
		final JRadioButton radio = new JRadioButton(text);
		radio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				selected.setSelected(state);
			}
		});
		component.add(radio);
		group.add(radio);
		state2buttonMap.put(state, radio);
		return radio;
	}

	private void addButtons(final WindowListener onClose) {
		final JPanel buttonPanel = new JPanel();
		final JButton loginButton = new JButton("Ok");
		buttonPanel.add(loginButton);
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				setSettingsFromControls();
				setVisible(false);
			}
		});

		final JButton closeButton = new JButton("Cancel");
		buttonPanel.add(closeButton);
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				onClose.windowClosing(null);
			}
		});
		add(buttonPanel, BorderLayout.SOUTH);
	}

}
