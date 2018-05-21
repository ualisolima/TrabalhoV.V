package com.br.ufc.vev.service;

import java.util.List;

import com.br.ufc.vev.model.Cinema;

public interface CinemaService {
	
	public List<Cinema> findByNomeOrCidade(String nome, String cidade);

}
