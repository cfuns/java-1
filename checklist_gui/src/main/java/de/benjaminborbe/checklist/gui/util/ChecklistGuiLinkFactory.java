package de.benjaminborbe.checklist.gui.util;

import javax.servlet.http.HttpServletRequest;

import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistList;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;

public class ChecklistGuiLinkFactory {

	public String listEntries(final HttpServletRequest request, final ChecklistList checklistList) {
		return null;
	}

	public String updateList(final HttpServletRequest request, final ChecklistListIdentifier id) {
		return null;
	}

	public String deleteList(final HttpServletRequest request, final ChecklistListIdentifier id) {
		return null;
	}

	public String createList(final HttpServletRequest request) {
		return null;
	}

	public String updateEntry(final HttpServletRequest request, final ChecklistEntryIdentifier id) {
		return null;
	}

	public String deleteEntry(final HttpServletRequest request, final ChecklistEntryIdentifier id) {
		return null;
	}

	public String createEntry(final HttpServletRequest request) {
		return null;
	}

}
