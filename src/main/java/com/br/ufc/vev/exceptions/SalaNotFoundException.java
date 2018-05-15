package com.br.ufc.vev.exceptions;

public class SalaNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;
	public SalaNotFoundException() { super("Sala not Found"); }
	public SalaNotFoundException(String message) { super(message); }
	public SalaNotFoundException(String message, Throwable cause) { super(message, cause); }
	public SalaNotFoundException(Throwable cause) { super(cause); }


}
