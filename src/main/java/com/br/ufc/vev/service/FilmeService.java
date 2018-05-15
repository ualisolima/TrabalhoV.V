package com.br.ufc.vev.service;

import java.util.List;

import com.br.ufc.vev.model.Filme;

public interface FilmeService {
	
	public List<Filme> findAll();
	
    public Filme findOne(Long id);
     
    public Filme save(Filme filme);
    
    public void delete(Long id);
    
    public List<Filme> findByGenero(String genero);

}
