package de.benjaminborbe.microblog.conversation;

public class MicroblogConversationMailerException extends Exception {

	private static final long serialVersionUID = -2197031003765161159L;

	public MicroblogConversationMailerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public MicroblogConversationMailerException(final String message) {
		super(message);
	}

	public MicroblogConversationMailerException(final Throwable cause) {
		super(cause);
	}

}
