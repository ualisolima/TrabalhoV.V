package com.br.ufc.vev.exceptions;

public class ModelNotFound extends  Exception{
	
	private static final long serialVersionUID = 1L;
	public ModelNotFound() { super("Model Not Found"); }
	public ModelNotFound(String message) { super(message + "Not Found"); }
	public ModelNotFound(String message, Throwable cause) { super(message, cause); }
	public ModelNotFound(Throwable cause) { super(cause); }

}
