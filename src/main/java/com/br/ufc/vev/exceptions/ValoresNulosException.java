package com.br.ufc.vev.exceptions;

public class ValoresNulosException extends Exception{
	
	private static final long serialVersionUID = 1L;
	public ValoresNulosException() { super("Campo não pode ser nulo"); }
	public ValoresNulosException(String campo) { super(campo + " não pode ser nulo"); }
	public ValoresNulosException(String message, Throwable cause) { super(message, cause); }
	public ValoresNulosException(Throwable cause) { super(cause); }

}
