package de.benjaminborbe.distributed.search.util;

import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class StopWordsProvider implements Provider<StopWords> {

	private final Provider<StopWordsImpl> stopWordsImplProvider;

	@Inject
	public StopWordsProvider(final Provider<StopWordsImpl> stopWordsImplProvider) {
		this.stopWordsImplProvider = stopWordsImplProvider;
	}

	@Override
	public StopWords get() {
		final StopWords stopWords = stopWordsImplProvider.get();
		final ResourceBundle resourceBundle = ResourceBundle.getBundle("stopwords");
		final Enumeration<String> e = resourceBundle.getKeys();
		while (e.hasMoreElements()) {
			stopWords.add(e.nextElement());
		}
		return stopWords;
	}

}
