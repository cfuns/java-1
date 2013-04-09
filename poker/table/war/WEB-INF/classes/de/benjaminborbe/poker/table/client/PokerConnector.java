package de.benjaminborbe.poker.table.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;

public class PokerConnector {

	@Inject
	public PokerConnector() {
	}

	public void getStatus() {
		final String postUrl = "/bb/poker/game/status/json?game_id=b3fc8e37-0cde-46cd-98af-f6fd4aed03cb";
		final String requestData = "";
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, postUrl);
		try {
			builder.sendRequest(requestData.toString(), new RequestCallback() {

				@Override
				public void onError(final Request request, final Throwable e) {
					Window.alert(e.getMessage());
				}

				@Override
				public void onResponseReceived(final Request request, final Response response) {
					if (200 == response.getStatusCode()) {
						// Window.alert(response.getText());

						final String jsonString = response.getText();
						final JSONValue jsonValue = JSONParser.parseStrict(jsonString);
						final JSONObject jsonObject = jsonValue.isObject();
						if (jsonObject != null) {
							final JSONValue value = jsonObject.get("gameActivePlayer");
							Window.alert("ActivePlayer: " + value.isString().stringValue());
						}
					}
					else {
						Window.alert("Received HTTP status code other than 200 : " + response.getStatusText());
					}
				}
			});
		}
		catch (final RequestException e) {
			// Couldn't connect to server
			Window.alert(e.getMessage());
		}
	}
}
