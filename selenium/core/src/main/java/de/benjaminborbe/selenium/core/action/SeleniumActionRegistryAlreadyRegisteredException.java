package de.benjaminborbe.selenium.core.action;

public class SeleniumActionRegistryAlreadyRegisteredException extends RuntimeException {

    private static final long serialVersionUID = 5466340452521350398L;

    public SeleniumActionRegistryAlreadyRegisteredException(final Throwable cause) {
        super(cause);
    }

    public SeleniumActionRegistryAlreadyRegisteredException(final String message) {
        super(message);
    }

    public SeleniumActionRegistryAlreadyRegisteredException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
