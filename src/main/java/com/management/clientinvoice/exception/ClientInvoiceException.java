package com.biz4solutions.clientinvoice.exception;

import org.springframework.http.HttpStatus;

public class ClientInvoiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int errCode;

	/**
	 * true if the message string is not a key & doesn't need to be parsed from messages properties file
	 * false if the message string is a key & needs to be parsed from messages properties file
	 */
	private boolean isKeyMsg = true;

	/**
	 * Use this flag to send 200 as success code instead of Bad Request.
	 */
	private boolean sendSuccess200 = true;

	public ClientInvoiceException() {
		super();
		this.errCode = HttpStatus.BAD_REQUEST.value();
	}

	public ClientInvoiceException(String message) {
		super(message);
		this.errCode = HttpStatus.BAD_REQUEST.value();
	}

	public ClientInvoiceException(String message, int errCode) {
		super(message);
		this.errCode = errCode;
	}

	public ClientInvoiceException(Throwable arg0) {
		super(arg0);
		this.errCode = HttpStatus.BAD_REQUEST.value();
	}

	public ClientInvoiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		this.errCode = HttpStatus.BAD_REQUEST.value();
	}

	public ClientInvoiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		this.errCode = HttpStatus.BAD_REQUEST.value();
	}

	public ClientInvoiceException(String message, boolean isKeyMsg, int errCode) {
		super(message);
		this.isKeyMsg = isKeyMsg;
		this.errCode = errCode;
	}

	public ClientInvoiceException(String message, boolean isKeyMsg, int errCode, boolean sendSuccess200) {
		super(message);
		this.isKeyMsg = isKeyMsg;
		this.errCode = errCode;
		this.sendSuccess200 = sendSuccess200;
	}

	/**
	 * @return the errCode
	 */
	public int getErrCode() {
		return errCode;
	}

	public boolean isKeyMsg() {
		return isKeyMsg;
	}

	public void setKeyMsg(boolean keyMsg) {
		isKeyMsg = keyMsg;
	}

	public boolean isSendSuccess200() {
		return sendSuccess200;
	}
}