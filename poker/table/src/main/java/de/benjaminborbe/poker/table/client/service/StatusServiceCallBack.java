package de.benjaminborbe.poker.table.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import de.benjaminborbe.poker.table.client.model.Game;
import de.benjaminborbe.poker.table.client.ui.DataSource;

public class StatusServiceCallBack implements AsyncCallback<Game> {

	public DataSource getDataSource() {
		return dataSource;
	}

	private DataSource dataSource;

	public StatusServiceCallBack(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void onFailure(Throwable caught) {
		GWT.log(caught.getMessage());
	}

	public void onSuccess(Game result) {
		dataSource.setGame(result);
	}

}
