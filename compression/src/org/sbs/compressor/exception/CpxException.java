package org.sbs.compressor.exception;

/**
 * @author shenbaise
 *
 */
public class CpxException extends Exception {

	private static final long serialVersionUID = 4133072075835514794L;


	public CpxException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public CpxException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public CpxException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CpxException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
