package com.busap.vcs.base;

/**
 * Created by
 * User: djyin
 * Date: 12/6/13
 * Time: 1:58 PM
 */
public class FastException extends Throwable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4596206925047775041L;

	/**
     * Instantiates a new Fast exception.
     */
    public FastException() {
        super();
    }

    /**
     * Instantiates a new Fast exception.
     *
     * @param message
     *         the message
     */
    public FastException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Fast exception.
     *
     * @param message
     *         the message
     * @param cause
     *         the cause
     */
    public FastException(String message, Throwable cause) {
        super(message, cause);
    }

    public FastException(Throwable cause) {
        super(cause);
    }
}
