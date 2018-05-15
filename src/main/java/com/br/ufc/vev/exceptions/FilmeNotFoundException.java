package com.br.ufc.vev.exceptions;

public class FilmeNotFoundException extends Exception{
	
	private static final long serialVersionUID = 1L;
	public FilmeNotFoundException() { super("Filme not Found"); }
	public FilmeNotFoundException(String message) { super(message); }
	public FilmeNotFoundException(String message, Throwable cause) { super(message, cause); }
	public FilmeNotFoundException(Throwable cause) { super(cause); }


}
