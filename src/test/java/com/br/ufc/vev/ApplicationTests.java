package com.br.ufc.vev;

import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.br.ufc.vev.Repository.SessaoRepository;
import com.br.ufc.vev.exceptions.FilmeNotFoundException;
import com.br.ufc.vev.exceptions.SalaNotFoundException;
import com.br.ufc.vev.model.Filme;
import com.br.ufc.vev.model.Sala;
import com.br.ufc.vev.model.Sessao;
import com.br.ufc.vev.service.FilmeService;
import com.br.ufc.vev.service.SalaService;
import com.br.ufc.vev.service.SessaoService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class ApplicationTests {
	
	@Mock
	FilmeService filmeServicemMock;
	
	@Spy
	Filme filme1 = new Filme(), filme2 = new Filme(), filme3 = new Filme();
	
	private List<Filme> filmes = new ArrayList<Filme>();
	
	@Mock
	SalaService salaServicemMock;
	
	@Spy
	Sala sala1 = new Sala(), sala2 = new Sala(), sala3 = new Sala();
	
	@Spy
	@Autowired
	SessaoRepository sessaoRepository;
	
	@InjectMocks
	SessaoService sessaoService;
	
	private List<Sala> salas = new ArrayList<Sala>();

	@Before
	public void setUp() {
		sessaoService.setRepository(sessaoRepository);
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
		when(filmeServicemMock.findByGenero(anyString())).thenReturn(filmes);
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
		
	}
	
	@Test
	public void addSessaoComSucesso() throws FilmeNotFoundException, SalaNotFoundException {
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
	
	@Test(expected=FilmeNotFoundException.class)
	public void addSessaoComFilmeInexistente() throws FilmeNotFoundException, SalaNotFoundException {
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

}
