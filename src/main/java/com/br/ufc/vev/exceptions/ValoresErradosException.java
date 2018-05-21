package com.br.ufc.vev.exceptions;

public class ValoresErradosException extends Exception{

	private static final long serialVersionUID = 1L;
	public ValoresErradosException() { super("Campo possui valor errado"); }
	public ValoresErradosException(String message) { super(message); }
	public ValoresErradosException(String message, Throwable cause) { super(message, cause); }
	public ValoresErradosException(Throwable cause) { super(cause); }
	
}
