package de.benjaminborbe.website.table;

import de.benjaminborbe.html.api.Widget;

public class TableCellHeadWidget extends TableCellWidget {

	public TableCellHeadWidget() {
		super();
	}

	public TableCellHeadWidget(final String content) {
		super(content);
	}

	public TableCellHeadWidget(final Widget content) {
		super(content);
	}

	@Override
	protected String getTag() {
		return "th";
	}
}
