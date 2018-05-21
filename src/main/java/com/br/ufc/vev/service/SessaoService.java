package com.br.ufc.vev.service;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.ufc.vev.Repository.SessaoRepository;
import com.br.ufc.vev.exceptions.ModelAlreadyExists;
import com.br.ufc.vev.exceptions.ModelNotFound;
import com.br.ufc.vev.exceptions.ValoresErradosException;
import com.br.ufc.vev.exceptions.ValoresNulosException;
import com.br.ufc.vev.model.Cinema;
import com.br.ufc.vev.model.Filme;
import com.br.ufc.vev.model.Sala;
import com.br.ufc.vev.model.Sessao;

@Service
public class SessaoService {
	
	@Autowired
	private SessaoRepository repository;
	
	@Mock
	private FilmeService filmeService;
	
	@Mock
	private SalaService salaService;
	
	@Mock
	private CinemaService cinemaService;
	
	public void setRepository(SessaoRepository repository) {
		this.repository = repository;
	}
	public void setSalaService(SalaService salaService) {
		this.salaService = salaService;
	}
	
	public void setFilmeService(FilmeService filmeService) {
		this.filmeService = filmeService;
	}
	
	public void setCinemaService(CinemaService cinemaService) {
		this.cinemaService = cinemaService;
	}
	
	public List<Sessao> findAll() {
		//System.out.println("teste");
        return repository.findAll();
    }
	
    public Sessao findOne(Long id) throws ModelNotFound, ValoresNulosException, ValoresErradosException {
    	if (id == null)
    		throw new ValoresNulosException("Sessao ID");
    	if (id <= 0 )
    		throw new ValoresErradosException("Sessao ID não pode ser menor ou igual a 0");
    	Optional<Sessao> optional =  repository.findById(id);
    	if (optional.isPresent()) {
    		return optional.get();
    	}
    	else {
    		throw new ModelNotFound("Sessao");
    	}
    }
     
    public Sessao save(Sessao sessao) throws  ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException{
    	if ( sessao.getFilmeId() == null )
    		throw new ValoresNulosException("Filme ID");
    	if ( sessao.getSalaId() == null )
    		throw new ValoresNulosException("Sala ID");
    	if ( sessao.getFilmeId() <= 0 )
    		throw new ValoresErradosException("Filme ID não pode ser menor ou igual a 0");
    	if ( sessao.getSalaId() <= 0 )
    		throw new ValoresErradosException("Sala ID não pode ser menor ou igual a 0");
    	if ( sessao.getHorario() == null )
    		throw new ValoresNulosException("Horario");
    	if ( sessao.getDataInicio() == null )
    		throw new ValoresNulosException("Data início");
    	if ( sessao.getDataFim() == null )
    		throw new ValoresNulosException("Data Fim");
    	if (filmeService.findOne(sessao.getFilmeId()) == null)
    		throw new ModelNotFound("Filme");
    	if (salaService.findOne(sessao.getSalaId()) == null) 
    		throw new ModelNotFound("Sala");
    	if ( sessao.getSessaoId() == null && !repository.findBySalaIdAndDataInicioAndHorario(sessao.getSalaId(), sessao.getDataInicio(), sessao.getHorario()).isEmpty())
    		throw new ModelAlreadyExists("Sessao");
    	if (sessao.getSessaoId() != null)
    		findOne(sessao.getSessaoId());
    	
    	return repository.saveAndFlush(sessao);
    }
     
    public void delete(Long id) throws ModelNotFound, ValoresNulosException, ValoresErradosException {
    	findOne(id);
        repository.deleteById(id);
    }
    
    public Set<Sessao> findByGenero(String genero) throws ValoresNulosException, ValoresErradosException {
    	if (genero == null)
    		throw new ValoresNulosException("Genero");
    	if (!genero.equals("") &&  !genero.chars().allMatch(Character::isLetter))
    		throw new ValoresErradosException("Genero não pode conter caracteres especiais");
    	Set<Sessao> retorno = new HashSet<Sessao>();
    	List<Filme> lista = filmeService.findByGenero(genero);
    	for (Filme f : lista)
    		retorno.addAll(repository.findByFilmeId(f.getFilmeId()));
    	
    	return retorno;
    }
    
    public Set<Sessao> findByCinemaOrCidade(String nome) throws ValoresNulosException, ValoresErradosException {
    	if (nome == null )
    		throw new ValoresNulosException("Cinema ou Cidade");
    	if (!nome.equals("") && !nome.chars().allMatch(Character::isLetter))
    		throw new ValoresErradosException("Cinema ou Cidade não pode conter caracteres especiais");
    	Set<Sessao> retorno = new HashSet<Sessao>();
    	List<Cinema> lista = cinemaService.findByNomeOrCidade(nome, nome);
    	for (Cinema c : lista) {
    		List<Sala> salas = c.getSalas();
    		for (Sala s: salas) {
    				List<Sessao> sessoes = repository.findBySalaId(s.getSalaId());
    				retorno.addAll(sessoes);
    			}
    	}
    	
    	return retorno;
    }
    
    public Set<Sessao> findByFilmeId(Long filmeId) throws ValoresNulosException, ValoresErradosException, ModelNotFound {
    	if (filmeId == null)
    		throw new ValoresNulosException("Filme ID");
    	if (filmeId <= 0)
    		throw new ValoresErradosException("Filme ID não pode ser < ou igual a 0");
    	if (filmeService.findOne(filmeId) == null)
    		throw new ModelNotFound("Filme");
    	return repository.findByFilmeId(filmeId);
    }
    
    public List<Sessao> findBySalaId(Long salaId) throws ValoresNulosException, ValoresErradosException, ModelNotFound {
    	if (salaId == null)
    		throw new ValoresNulosException("Sala ID");
    	if (salaId <= 0)
    		throw new ValoresErradosException("Sala ID não pode ser < ou igual a 0");
    	if (salaService.findOne(salaId) == null)
    		throw new ModelNotFound("Sala");
    	return repository.findBySalaId(salaId);
    }
    
    public List<Sessao> findByDataInicioDataFim(Date dataInicio, Date dataFim) throws ValoresNulosException {
    	if (dataInicio == null)
    		throw new ValoresNulosException("Data Início");
    	if (dataFim == null)
    		return repository.findByDataInicioGreaterThanEqualOrderByDataInicio(dataInicio);
    	else
    		return repository.findByDataInicioGreaterThanEqualAndDataFimLessThanEqualOrderByDataInicio(dataInicio, dataFim);
    }

}
