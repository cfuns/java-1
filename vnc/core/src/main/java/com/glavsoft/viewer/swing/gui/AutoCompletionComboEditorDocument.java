package com.glavsoft.viewer.swing.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

/**
 * @author dime at tightvnc.com
 * 
 *         Using idea by Thomas Bierhance from http://www.orbital-computer.de/JComboBox/
 */
public class AutoCompletionComboEditorDocument extends PlainDocument {

	private static final long serialVersionUID = -3763461783463836821L;

	private final ComboBoxModel<?> model;

	private boolean selecting;

	private final JComboBox<?> comboBox;

	private final boolean hidePopupOnFocusLoss;

	private final JTextComponent editor;

	public AutoCompletionComboEditorDocument(final JComboBox<?> comboBox) {
		this.comboBox = comboBox;
		this.model = comboBox.getModel();
		this.editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		editor.setDocument(this);
		comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				if (!selecting)
					highlightCompletedText(0);
			}
		});

		final Object selectedItem = comboBox.getSelectedItem();
		if (selectedItem != null) {
			setText(selectedItem.toString());
			highlightCompletedText(0);
		}
		hidePopupOnFocusLoss = System.getProperty("java.version").startsWith("1.5");
		editor.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(final FocusEvent e) {
				if (hidePopupOnFocusLoss)
					comboBox.setPopupVisible(false);
			}
		});
	}

	@Override
	public void remove(final int offs, final int len) throws BadLocationException {
		if (selecting)
			return;
		super.remove(offs, len);
	}

	@Override
	public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
		if (selecting)
			return;
		super.insertString(offs, str, a);
		final Object item = lookupItem(getText(0, getLength()));
		if (item != null) {
			setSelectedItem(item);
			setText(item.toString());
			highlightCompletedText(offs + str.length());
			if (comboBox.isDisplayable())
				comboBox.setPopupVisible(true);
		}
	}

	private void setText(final String text) {
		try {
			super.remove(0, getLength());
			super.insertString(0, text, null);
		}
		catch (final BadLocationException e) {
			throw new RuntimeException(e);
		}
	}

	private void setSelectedItem(final Object item) {
		selecting = true;
		model.setSelectedItem(item);
		selecting = false;
	}

	private void highlightCompletedText(final int offs) {
		final JTextComponent editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
		editor.setSelectionStart(offs);
		editor.setSelectionEnd(getLength());
	}

	private Object lookupItem(final String pattern) {
		final Object selectedItem = model.getSelectedItem();
		if (selectedItem != null && startsWithIgnoreCase(selectedItem, pattern)) {
			return selectedItem;
		}
		else {
			for (int i = 0, n = model.getSize(); i < n; i++) {
				final Object currentItem = model.getElementAt(i);
				if (startsWithIgnoreCase(currentItem, pattern)) {
					return currentItem;
				}
			}
		}
		return null;
	}

	private boolean startsWithIgnoreCase(final Object currentItem, final String pattern) {
		return currentItem.toString().toLowerCase().startsWith(pattern.toLowerCase());
	}

}
