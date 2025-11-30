package de.relaxogames.exceptions;

/**
 * Thrown when a database connection required by the RelaxoGames system
 * has been lost or is no longer usable.
 * <p>
 * This exception typically indicates that the SQL driver cannot retrieve
 * a valid connection, that the underlying connection has closed, or that
 * communication with the database has been interrupted.
 * </p>
 * <p>
 * Note: The overridden methods in this class simply delegate to their
 * superclass implementations and do not provide additional behavior. They
 * can be removed unless there is a specific reason to keep them.
 * </p>
 */
public class DriverLostConnection extends RuntimeException {

    /**
     * Creates a new {@code DriverLostConnection} with the specified detail message.
     *
     * @param message a description of the connection error
     */
    public DriverLostConnection(String message) {
        super(message);
    }

    /**
     * Creates a new {@code DriverLostConnection} with the specified message
     * and underlying cause.
     *
     * @param message a description of the connection error
     * @param cause   the exception that caused the connection failure
     */
    public DriverLostConnection(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Returns the cause of this exception.
     * <p>
     * This implementation simply delegates to {@link Throwable#getCause()}.
     * </p>
     *
     * @return the cause, or {@code null} if none exists
     */
    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    /**
     * Returns the detail message associated with this exception.
     * <p>
     * This implementation simply delegates to {@link Throwable#getMessage()}.
     * </p>
     *
     * @return the exception message
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * Prints this exception's stack trace to the standard error stream.
     * <p>
     * This implementation simply delegates to {@link Throwable#printStackTrace()}.
     * </p>
     */
    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
