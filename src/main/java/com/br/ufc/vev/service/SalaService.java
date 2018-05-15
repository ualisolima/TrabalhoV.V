package com.br.ufc.vev.service;

import java.util.List;

import com.br.ufc.vev.model.Sala;

public interface SalaService {
	
	public List<Sala> findAll();
	
    public Sala findOne(Long id);
     
    public Sala save(Sala sala);
     
    public void delete(Long id);

}
