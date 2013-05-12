package de.benjaminborbe.selenium.api;

import java.util.List;

public interface SeleniumExecutionProtocol {

	List<String> getMessages();

	List<String> getInfos();

	List<String> getErrors();

	boolean isCompleted();

	boolean hasErrors();
}
