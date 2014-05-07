package de.benjaminborbe.poker.table.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import de.benjaminborbe.poker.table.client.model.Game;

import java.util.ArrayList;

@RemoteServiceRelativePath("statusService")
public interface StatusService extends RemoteService {

	ArrayList<Game> getGames();

}