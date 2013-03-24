package de.benjaminborbe.microblog.conversation;

public class MicroblogConversationNotifierException extends Exception {

	private static final long serialVersionUID = -2197031003765161159L;

	public MicroblogConversationNotifierException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MicroblogConversationNotifierException(final String message) {
		super(message);
	}

	public MicroblogConversationNotifierException(final Throwable cause) {
		super(cause);
	}

}
