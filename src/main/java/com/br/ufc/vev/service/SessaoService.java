package com.br.ufc.vev.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ufc.vev.Repository.SessaoRepository;
import com.br.ufc.vev.exceptions.FilmeNotFoundException;
import com.br.ufc.vev.exceptions.SalaNotFoundException;
import com.br.ufc.vev.exceptions.SessaoJaExisteException;
import com.br.ufc.vev.exceptions.ValoresNulosException;
import com.br.ufc.vev.model.Filme;
import com.br.ufc.vev.model.Sessao;

@Service
public class SessaoService {
	
	@Autowired
	private SessaoRepository repository;
	
	@Autowired
	private FilmeService filmeService;
	
	@Autowired
	private SalaService salaService;
	
	public void setRepository(SessaoRepository repository) {
		this.repository = repository;
	}
	
	public List<Sessao> findAll() {
		//System.out.println("teste");
        return repository.findAll();
    }
	
    public Sessao findOne(Long id) {
    	Optional<Sessao> optional =  repository.findById(id);
    	if (optional.isPresent()) {
    		return optional.get();
    	}
    	else {
    		return null;
    	}
    }
     
    public Sessao save(Sessao sessao) throws FilmeNotFoundException, SalaNotFoundException, SessaoJaExisteException, ValoresNulosException{
    	if (filmeService.findOne(sessao.getFilmeId()) == null) {
    		throw new FilmeNotFoundException();
    	}
    	if (salaService.findOne(sessao.getSalaId()) == null) {
    		throw new SalaNotFoundException();
    	}
    	if ( sessao.getSessaoId() == null && !repository.findBySalaIdAndDataInicioAndHorario(sessao.getSalaId(), sessao.getDataInicio(), sessao.getHorario()).isEmpty())
    		throw new SessaoJaExisteException();
    	if ( sessao.getDataInicio() == null )
    		throw new ValoresNulosException("Data início");
    	return repository.saveAndFlush(sessao);
    }
     
    public void delete(Long id) {
        repository.deleteById(id);
    }
    
    public Set<Sessao> findByGenero(String genero) {
    	Set<Sessao> retorno = new HashSet<Sessao>();
    	List<Filme> lista = filmeService.findByGenero(genero);
    	for (Filme f : lista)
    		retorno.addAll(repository.findByFilmeId(f.getFilmeId()));
    	
    	return retorno;
    }

}
