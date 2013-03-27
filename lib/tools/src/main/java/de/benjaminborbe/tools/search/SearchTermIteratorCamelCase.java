package de.benjaminborbe.tools.search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.benjaminborbe.api.IteratorWithoutException;
import de.benjaminborbe.tools.iterator.IteratorWithoutExceptionBase;

public class SearchTermIteratorCamelCase extends IteratorWithoutExceptionBase<String> {

	private final IteratorWithoutException<String> iterator;

	private final Queue<String> words = new LinkedList<String>();

	public SearchTermIteratorCamelCase(final IteratorWithoutException<String> iterator) {
		this.iterator = iterator;
	}

	@Override
	protected String buildNext() {
		if (words.isEmpty() && iterator.hasNext()) {
			final List<String> words = buildWords(iterator.next());
			for (final String word : words) {
				this.words.offer(word);
			}
		}
		if (!words.isEmpty()) {
			return words.poll();
		}
		else {
			return null;
		}
	}

	private List<String> buildWords(final String content) {
		final List<String> result = new ArrayList<String>();
		final char[] chars = content.toCharArray();
		if (chars.length > 1 && Character.isUpperCase(chars[0]) && Character.isLowerCase(chars[1])) {
			int startPos = 0;
			for (int i = 2; i < chars.length; ++i) {
				if (Character.isUpperCase(chars[i])) {
					result.add(content.substring(startPos, i));
					startPos = i;
				}
			}
			result.add(content.substring(startPos));
		}
		else {
			result.add(content);
		}
		return result;
	}
}
