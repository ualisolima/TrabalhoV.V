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
import org.mockito.junit.MockitoJUnitRunner;

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

@RunWith(MockitoJUnitRunner.Silent.class)
//@SpringBootTest
public class ApplicationTests {
	
	@Mock
	FilmeService filmeServicemMock;
	
	@Mock
	Filme filme1, filme2, filme3;
	
	private List<Filme> filmes = new ArrayList<Filme>();
	
	@Mock
	SalaService salaServicemMock;
	
	@Mock
	Sala sala1, sala2, sala3;
	
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
		sessaoService.save(s);
		Sessao other = sessaoService.findOne(1L);
		assertEquals(1L, other.getSessaoId());
	}

}
