package de.benjaminborbe.authorization.gui;

public interface AuthorizationGuiConstants {

	String NAME = "authorization";

	String PARAMETER_PERMISSION_ID = "permission_id";

	String PARAMETER_ROLE_ID = "role_id";

	String PARAMETER_USER_ID = "user_id";

	String URL_ADD_ROLE = "/user/addRole";

	String URL_PERMISSION_LIST = "/permission";

	String URL_PERMISSION_DENIED = "/permissionDenied";

	String URL_ROLE_ADD_PERMISSION = "/role/addPermission";

	String URL_ROLE_CREATE = "/role/create";

	String URL_ROLE_INFO = "/role/info";

	String URL_ROLE_LIST = "/role";

	String URL_ROLE_DELETE = "/role/remove";

	String URL_ROLE_PERMISSION_REMOVE = "/role/removePermission";

	String URL_SLASH = "/";

	String URL_USER_LIST = "/user";

	String URL_USER_INFO = "/user/info";

	String URL_USER_REMOVE_ROLE = "/user/removeRole";

	String PARAMETER_ROLE_NAME = "role_name";

	String PARAMETER_REFERER = "referer";

}
