package com.br.ufc.vev.exceptions;

public class SessaoJaExisteException extends Exception{
	
	private static final long serialVersionUID = 1L;
	public SessaoJaExisteException() { super("Sessão já Existe"); }
	public SessaoJaExisteException(String message) { super(message); }
	public SessaoJaExisteException(String message, Throwable cause) { super(message, cause); }
	public SessaoJaExisteException(Throwable cause) { super(cause); }

}
