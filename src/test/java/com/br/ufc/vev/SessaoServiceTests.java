package com.br.ufc.vev;

import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.ufc.vev.Repository.SessaoRepository;
import com.br.ufc.vev.exceptions.ModelAlreadyExists;
import com.br.ufc.vev.exceptions.ModelNotFound;
import com.br.ufc.vev.exceptions.ValoresErradosException;
import com.br.ufc.vev.exceptions.ValoresNulosException;
import com.br.ufc.vev.model.Cinema;
import com.br.ufc.vev.model.Filme;
import com.br.ufc.vev.model.Sala;
import com.br.ufc.vev.model.Sessao;
import com.br.ufc.vev.service.CinemaService;
import com.br.ufc.vev.service.FilmeService;
import com.br.ufc.vev.service.SalaService;
import com.br.ufc.vev.service.SessaoService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class SessaoServiceTests {
	
	@Mock
	FilmeService filmeServicemMock;
	
	@Spy
	Filme filme1 = new Filme(), filme2 = new Filme(), filme3 = new Filme();
	
	private List<Filme> filmes = new ArrayList<Filme>();
	
	@Mock
	SalaService salaServicemMock;
	
	@Spy
	Sala sala1 = new Sala(), sala2 = new Sala(), sala3 = new Sala();
	
	@Mock
	CinemaService cinemaServicemMock;
	
	@Spy
	Cinema cinema = new Cinema();
	
	private List<Cinema> cinemas = new ArrayList<Cinema>();
	
	@Spy
	@Autowired
	SessaoRepository sessaoRepository;
	
	@InjectMocks
	SessaoService sessaoService;
	
	private List<Sala> salas = new ArrayList<Sala>();

	@Before
	public void setUp() {
		when(filme1.getFilmeId()).thenReturn(1L);
		when(filme2.getFilmeId()).thenReturn(2L);
		when(filme3.getFilmeId()).thenReturn(1L);
		filmes.add(filme1);
		filmes.add(filme2);
		filmes.add(filme3);
		when(filmeServicemMock.findAll()).thenReturn(filmes);
		when(filmeServicemMock.findOne(1L) ).thenReturn(filme1);
		when(filmeServicemMock.findOne(2L) ).thenReturn(filme2);
		when(filmeServicemMock.findOne(3L) ).thenReturn(filme3);
		when(filmeServicemMock.findByGenero(anyString())).thenReturn(new ArrayList<Filme>());
		when(filmeServicemMock.findByGenero("Comedia")).thenReturn(filmes);
		when(sala1.getSalaId()).thenReturn(1L);
		when(sala2.getSalaId()).thenReturn(2L);
		when(sala3.getSalaId()).thenReturn(3L);
		salas.add(sala1);
		salas.add(sala2);
		salas.add(sala3);
		when(salaServicemMock.findAll()).thenReturn(salas);
		when(salaServicemMock.findOne(1L) ).thenReturn(sala1);
		when(salaServicemMock.findOne(2L) ).thenReturn(sala2);
		when(salaServicemMock.findOne(3L) ).thenReturn(sala3);
		
		when(cinema.getSalas()).thenReturn(salas);
		cinemas.add(cinema);
		when(cinemaServicemMock.findByNomeOrCidade(anyString(), anyString())).thenReturn(new ArrayList<Cinema>());
		when(cinemaServicemMock.findByNomeOrCidade(  eq("Quixada"), anyString())).thenReturn(cinemas);
		when(cinemaServicemMock.findByNomeOrCidade(anyString(), eq("Pinheiro") )).thenReturn(cinemas);
		sessaoService.setRepository(sessaoRepository);
		sessaoService.setFilmeService(filmeServicemMock);
		sessaoService.setSalaService(salaServicemMock);
		sessaoService.setCinemaService(cinemaServicemMock);
		
	}
	//Testes do Service
	@Test
	public void addSessaoComSucesso() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		Sessao other = sessaoService.findOne(s.getSessaoId());
		assertEquals(s, other);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void addSessaoComFilmeNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		//s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void addSessaoComSalaNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		//s.setSalaId(1L);
		s = sessaoService.save(s);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void addSessaoComHorarioNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		//s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void addSessaoDataInicioNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		sessaoService.save(s);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void addSessaoComDataFim() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		//s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
	}
	
	@Test(expected = ModelAlreadyExists.class)
	public void addSessaoQueJaExiste() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		sessaoService.save(s);
		Sessao s2 = new Sessao();
		s2.setDataInicio( s.getDataInicio() );
		s2.setDataFim( s.getDataFim());
		s2.setHorario( s.getHorario() );
		s2.setFilmeId(1L);
		s2.setSalaId(1L);
		sessaoService.save(s2);
	}
	
	@Test
	public void buscaSessaoExistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		Sessao other = sessaoService.findOne(s.getSessaoId());
		assertEquals(s, other);
	}
	
	@Test(expected = ModelNotFound.class)
	public void buscaSessaoInexistente() throws ValoresNulosException, ValoresErradosException, ModelNotFound {
		sessaoService.findOne(1L);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void buscaSessaoIdNulo() throws ValoresNulosException, ValoresErradosException, ModelNotFound {
		sessaoService.findOne(null);
	}
	
	@Test(expected = ValoresErradosException.class)
	public void buscaSessaoIdErrado() throws ValoresNulosException, ValoresErradosException, ModelNotFound {
		sessaoService.findOne(-1L);
	}
	
	@Test(expected = ModelNotFound.class)
	public void excluirSessaoExistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		sessaoService.delete(s.getSessaoId());
		sessaoService.findOne(s.getSessaoId());
	}
	
	@Test(expected = ModelNotFound.class)
	public void excluirSessaoInexistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s.setSessaoId(1L);
		//s = sessaoService.save(s);
		sessaoService.delete(s.getSessaoId());
		//sessaoService.findOne(s.getSessaoId());
	}
	
	@Test(expected = ValoresNulosException.class)
	public void excluirSessaoIdNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		//s.setSessaoId(1L);
		//s = sessaoService.save(s);
		sessaoService.delete(s.getSessaoId());
		//sessaoService.findOne(s.getSessaoId());
	}
	
	@Test(expected = ValoresErradosException.class)
	public void excluirSessaoIdErrado() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s.setSessaoId(-1L);
		//s = sessaoService.save(s);
		sessaoService.delete(s.getSessaoId());
		//sessaoService.findOne(s.getSessaoId());
	}
	
	@Test
	public void atualizarSessaoExistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s = sessaoService.save(s);
		Sessao other = sessaoService.findOne(s.getSessaoId());
		assertEquals(s, other);
	}
	
	@Test(expected = ModelNotFound.class)
	public void atualizarSessaoInexistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s.setSessaoId(1L);
		s = sessaoService.save(s);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void atualizarSessaoDataInicioNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		s.setDataInicio(null);
		s = sessaoService.save(s);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void atualizarSessaoDataFimNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		s.setDataFim(null);
		s = sessaoService.save(s);
	}
	
	@Test(expected = ValoresNulosException.class)
	public void atualizarSessaoHorarioNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		s.setHorario(null);
		s = sessaoService.save(s);
	}
	@Test(expected = ValoresNulosException.class)
	public void atualizarSessaoFilmeIdNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		s.setFilmeId(null);
		s = sessaoService.save(s);
	}
	@Test(expected = ValoresNulosException.class)
	public void atualizarSessaoDataSalaIdNulo() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		s.setSalaId(null);
		s = sessaoService.save(s);
	}
	
	@Test(expected = ValoresErradosException.class)
	public void atualizarSessaoFilmeIdErrrado() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		s.setFilmeId(-1L);
		s = sessaoService.save(s);
	}
	@Test(expected = ValoresErradosException.class)
	public void atualizarSessaoDataSalaIdErrado() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		s.setSalaId(-1L);
		s = sessaoService.save(s);
	}
	
	@Test
	public void visualizarSessoes() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		List<Sessao> ss = sessaoService.findAll();
		assertEquals(0, ss.size());
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		ss = sessaoService.findAll();
		assertEquals(1, ss.size());
	}
	
	@Test
	public void visualizarSessoesEntreDatas() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s1 = new Sessao();
		Sessao s2 = new Sessao();
		Calendar c = Calendar.getInstance();
		Date dinicio1 = new Date( c.getTimeInMillis() );
		c.add(Calendar.DATE, 7);
		Date dfim1 = new Date( c.getTimeInMillis() );		
		Date dinicio2 = new Date( c.getTimeInMillis() );
		c.add(Calendar.DATE, 7);
		Date dfim2 = new Date( c.getTimeInMillis() );
		s1.setDataInicio(dinicio1);
		s1.setDataFim(dfim1);
		s1.setHorario(new Time(c.getTimeInMillis()));
		s1.setFilmeId(1L);
		s1.setSalaId(1L);
		s1 = sessaoService.save(s1);
		s2.setDataInicio(dinicio2);
		s2.setDataFim(dfim2);
		s2.setHorario(new Time(c.getTimeInMillis()));
		s2.setFilmeId(2L);
		s2.setSalaId(2L);
		s2 = sessaoService.save(s2);
		List<Sessao> ss = sessaoService.findByDataInicioDataFim(dinicio1, dfim2);
		assertEquals(2, ss.size());
		ss = sessaoService.findByDataInicioDataFim(dinicio1, dfim1);
		assertEquals(1, ss.size());
		ss = sessaoService.findByDataInicioDataFim(dinicio2, dfim2);
		assertEquals(1, ss.size());
		ss = sessaoService.findByDataInicioDataFim(dinicio1, null);
		assertEquals(2, ss.size());
		ss = sessaoService.findByDataInicioDataFim(dinicio2, null);
		assertEquals(1, ss.size());
	} 
	
	@Test(expected=ValoresNulosException.class)
	public void visualizarSessoesDataInicioNulo() throws ValoresNulosException {
		Calendar c = Calendar.getInstance();
		Date dfim = new Date( c.getTimeInMillis() );
		sessaoService.findByDataInicioDataFim(null, dfim);
	}
	
	@Test
	public void visualizarSessoesPorCinemaOuCidadeCorretamente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s1 = new Sessao();
		Sessao s2 = new Sessao();
		Calendar c = Calendar.getInstance();
		Date dinicio1 = new Date( c.getTimeInMillis() );
		c.add(Calendar.DATE, 7);
		Date dfim1 = new Date( c.getTimeInMillis() );		
		Date dinicio2 = new Date( c.getTimeInMillis() );
		c.add(Calendar.DATE, 7);
		Date dfim2 = new Date( c.getTimeInMillis() );
		s1.setDataInicio(dinicio1);
		s1.setDataFim(dfim1);
		s1.setHorario(new Time(c.getTimeInMillis()));
		s1.setFilmeId(1L);
		s1.setSalaId(1L);
		s1 = sessaoService.save(s1);
		s2.setDataInicio(dinicio2);
		s2.setDataFim(dfim2);
		s2.setHorario(new Time(c.getTimeInMillis()));
		s2.setFilmeId(2L);
		s2.setSalaId(2L);
		s2 = sessaoService.save(s2);
		Set<Sessao> ss = sessaoService.findByCinemaOrCidade("Quixada");
		assertEquals(2, ss.size());
		ss = sessaoService.findByCinemaOrCidade("Fortaleza");
		assertEquals(0, ss.size());
	}
	
	@Test(expected=ValoresNulosException.class)
	public void visualizarSessoesCidadeCinemaNulo() throws ValoresNulosException, ValoresErradosException {
		sessaoService.findByCinemaOrCidade(null);
	}
	
	@Test(expected=ValoresErradosException.class)
	public void visualizarSessoesCidadeCinemaErrado() throws ValoresNulosException, ValoresErradosException {
		sessaoService.findByCinemaOrCidade("123");
	}
	
	@Test
	public void visualizarSessoesPorFilmeId() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s1 = new Sessao();
		Sessao s2 = new Sessao();
		Calendar c = Calendar.getInstance();
		Date dinicio1 = new Date( c.getTimeInMillis() );
		c.add(Calendar.DATE, 7);
		Date dfim1 = new Date( c.getTimeInMillis() );		
		Date dinicio2 = new Date( c.getTimeInMillis() );
		c.add(Calendar.DATE, 7);
		Date dfim2 = new Date( c.getTimeInMillis() );
		s1.setDataInicio(dinicio1);
		s1.setDataFim(dfim1);
		s1.setHorario(new Time(c.getTimeInMillis()));
		s1.setFilmeId(1L);
		s1.setSalaId(1L);
		s1 = sessaoService.save(s1);
		s2.setDataInicio(dinicio2);
		s2.setDataFim(dfim2);
		s2.setHorario(new Time(c.getTimeInMillis()));
		s2.setFilmeId(1L);
		s2.setSalaId(2L);
		s2 = sessaoService.save(s2);
		Set<Sessao> ss = sessaoService.findByFilmeId(1L);
		assertEquals(2, ss.size());
	}
	
	@Test(expected = ValoresNulosException.class)
	public void visualizarSessoesPorFilmeIdNulo() throws ValoresNulosException, ValoresErradosException, ModelNotFound {
		sessaoService.findByFilmeId(null);
	}
	
	@Test(expected = ValoresErradosException.class)
	public void visualizarSessoesPorFilmeIdErrado() throws ValoresNulosException, ValoresErradosException, ModelNotFound {
		sessaoService.findByFilmeId(-1L);
	}
	
	@Test
	public void visualizarSessoesPorGenero() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s1 = new Sessao();
		Sessao s2 = new Sessao();
		Calendar c = Calendar.getInstance();
		Date dinicio1 = new Date( c.getTimeInMillis() );
		c.add(Calendar.DATE, 7);
		Date dfim1 = new Date( c.getTimeInMillis() );		
		Date dinicio2 = new Date( c.getTimeInMillis() );
		c.add(Calendar.DATE, 7);
		Date dfim2 = new Date( c.getTimeInMillis() );
		s1.setDataInicio(dinicio1);
		s1.setDataFim(dfim1);
		s1.setHorario(new Time(c.getTimeInMillis()));
		s1.setFilmeId(1L);
		s1.setSalaId(1L);
		s1 = sessaoService.save(s1);
		s2.setDataInicio(dinicio2);
		s2.setDataFim(dfim2);
		s2.setHorario(new Time(c.getTimeInMillis()));
		s2.setFilmeId(2L);
		s2.setSalaId(2L);
		s2 = sessaoService.save(s2);
		Set<Sessao> ss = sessaoService.findByGenero("Comedia");
		assertEquals(2, ss.size());
		ss = sessaoService.findByGenero("Terror");
		assertEquals(0, ss.size());
	}
	
	@Test(expected=ValoresNulosException.class)
	public void visualizarSessoesGeneroNulo() throws ValoresNulosException, ValoresErradosException {
		sessaoService.findByGenero(null);
	}
	
	@Test(expected=ValoresErradosException.class)
	public void visualizarSessoesGeneroErrado() throws ValoresNulosException, ValoresErradosException {
		sessaoService.findByGenero("123");
	}
	
	@Test(expected=ModelNotFound.class)
	public void addSessaoComFilmeInexistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(4L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
	}
	
	@Test(expected=ModelNotFound.class)
	public void addSessaoComSalaInexistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(4L);
		s = sessaoService.save(s);
	}
	
	@Test(expected=ModelNotFound.class)
	public void atualizaSessaoComFilmeInexistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s = sessaoService.save(s);
		s.setFilmeId(4L);
		//s.setSalaId(1L);
		s = sessaoService.save(s);
	}
	
	@Test(expected=ModelNotFound.class)
	public void atualizaSessaoComSalaInexistente() throws ModelNotFound, ModelAlreadyExists, ValoresNulosException, ValoresErradosException {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(4L);
		s = sessaoService.save(s);
		//s.setFilmeId(1L);
		s.setSalaId(4L);
		s = sessaoService.save(s);
	}

}
