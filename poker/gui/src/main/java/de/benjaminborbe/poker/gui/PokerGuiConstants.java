package de.benjaminborbe.poker.gui;

public interface PokerGuiConstants {

	long DEFAULT_BLIND = 100l;

	long DEFAULT_CREDITS = 10000l;

	String NAME = "poker";

	String PARAMETER_AMOUNT = "amount";

	String PARAMETER_GAME_ID = "game_id";

	String PARAMETER_GAME_NAME = "game_name";

	String PARAMETER_GAME_START_CREDITS = "game_start_credits";

	String PARAMETER_TOKEN = "token";

	String PARAMETER_PLAYER_ID = "player_id";

	String PARAMETER_PLAYER_NAME = "player_name";

	String PARAMETER_REFERER = "referer";

	String PARAMETER_PLAYER_AMOUNT = "player_amount";

	String PARAMETER_PLAYER_OWNERS = "player_owners";

	String URL_ACTION_CALL = "/call";

	String URL_ACTION_CALL_JSON = "/call/json";

	String URL_ACTION_FOLD = "/fold";

	String URL_ACTION_FOLD_JSON = "/fold/json";

	String URL_ACTION_RAISE = "/raise";

	String URL_ACTION_RAISE_JSON = "/raise/json";

	String URL_CSS = "/css";

	String URL_CSS_STYLE = "/css/style.css";

	String URL_GAME_CREATE = "/game/create";

	String URL_GAME_DELETE = "/game/player";

	String URL_GAME_JOIN = "/game/join";

	String URL_GAME_LEAVE = "/game/leave";

	String URL_GAME_LIST = "/game/list";

	String URL_GAME_START = "/game/start";

	String URL_GAME_STOP = "/game/stop";

	String URL_GAME_VIEW = "/game/view";

	String URL_HOME = "/";

	String URL_PLAYER_CREATE = "/player/create";

	String URL_PLAYER_DELETE = "/player/delete";

	String URL_PLAYER_LIST = "/player/list";

	String URL_PLAYER_VIEW = "/player/view";

	String URL_API_HELP = "/api/help";

	String URL_GAME_UPDATE = "/game/update";

	String URL_PLAYER_UPDATE = "/player/update";

	String URL_PLAYER_STATUS_JSON = "/status/json";

	String URL_GAME_STATUS_JSON = "/game/status/json";

	String URL_GAME_ADD_ALL_AVAILABLE_PLAYERS = "/game/add_all_available_players";

	String PARAMETER_GAME_BIG_BLIND = "game_start_big_blind";

	String PARAMETER_PLAYER_SCORE = "player_score";

	String PARAMETER_GAME_AUTO_JOIN_AND_RESTART = "game_auto_join_and_restart";

	boolean DEFAULT_GAME_AUTO_JOIN_AND_RESTART = false;

	String PARAMETER_GAME_AUTO_FOLD_TIMEOUT = "game_auto_fold_timeout";

	String URL_GAME_TABLE = "/poker_table/Home.html";

	String URL_EVENT_RESET = "/reset";

	String URL_PLAYER_CREATE_FOR_CURRENT_USER = "/player/createForCurrentUser";
}
