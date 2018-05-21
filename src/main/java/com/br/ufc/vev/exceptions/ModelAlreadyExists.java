package com.br.ufc.vev.exceptions;

public class ModelAlreadyExists extends Exception{
	
	private static final long serialVersionUID = 1L;
	public ModelAlreadyExists() { super("Model Already Exists"); }
	public ModelAlreadyExists(String message) { super(message + "Already Exists"); }
	public ModelAlreadyExists(String message, Throwable cause) { super(message, cause); }
	public ModelAlreadyExists(Throwable cause) { super(cause); }

}
