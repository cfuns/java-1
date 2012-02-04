package de.benjaminborbe.translate.api;

public interface TranslateService {

	String translate(String text, Language source, Language target);
}
