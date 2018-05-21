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
import org.springframework.web.servlet.ModelAndView;

import com.br.ufc.vev.Repository.SessaoRepository;
import com.br.ufc.vev.controller.SessaoController;
import com.br.ufc.vev.exceptions.ModelNotFound;
import com.br.ufc.vev.model.Cinema;
import com.br.ufc.vev.model.Filme;
import com.br.ufc.vev.model.Sala;
import com.br.ufc.vev.model.Sessao;
import com.br.ufc.vev.service.CinemaService;
import com.br.ufc.vev.service.FilmeService;
import com.br.ufc.vev.service.SalaService;
import com.br.ufc.vev.service.SessaoService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class SessaoControllerTests {
	
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
	
	@Spy
	@InjectMocks
	SessaoService sessaoServiceSpy;
	
	@InjectMocks
	SessaoController sessaoController;
	
	
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
		sessaoServiceSpy.setRepository(sessaoRepository);
		sessaoServiceSpy.setFilmeService(filmeServicemMock);
		sessaoServiceSpy.setSalaService(salaServicemMock);
		sessaoServiceSpy.setCinemaService(cinemaServicemMock);
		sessaoController.setSessaoService(sessaoServiceSpy);
		
	}
	//Testes do Service
	@Test
	public void addSessaoComSucesso()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals(mv.getViewName(), "/sessao");
	}
	
	@Test
	public void addSessaoComFilmeNulo() {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		//s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals(mv.getViewName(), "/sessaoAdd");
		assertEquals("Filme ID não pode ser nulo", mv.getModel().get("message"));
		
	}
//	
	@Test
	public void addSessaoComSalaNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		//s.setSalaId(1L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals(mv.getViewName(), "/sessaoAdd");
		assertEquals("Sala ID não pode ser nulo", mv.getModel().get("message"));
	}
//	
	@Test
	public void addSessaoComHorarioNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		//s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals(mv.getViewName(), "/sessaoAdd");
		assertEquals("Horario não pode ser nulo", mv.getModel().get("message"));
	}
//	
	@Test
	public void addSessaoDataInicioNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals(mv.getViewName(), "/sessaoAdd");
		assertEquals("Data início não pode ser nulo", mv.getModel().get("message"));
	}
//	
	@Test
	public void addSessaoComDataFim()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		//s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals(mv.getViewName(), "/sessaoAdd");
		assertEquals("Data Fim não pode ser nulo", mv.getModel().get("message"));
	}
//	
	@Test
	public void addSessaoQueJaExiste() {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		Sessao s2 = new Sessao();
		s2.setDataInicio( s.getDataInicio() );
		s2.setDataFim( s.getDataFim());
		s2.setHorario( s.getHorario() );
		s2.setFilmeId(1L);
		s2.setSalaId(1L);
		ModelAndView mv2 = sessaoController.save(s2, null);
		assertNotEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("SessaoAlready Exists", mv2.getModel().get("message"));
	}
//	
	@Test
	public void buscaSessaoExistente() {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		sessaoController.save(s, null);
		ModelAndView mv = sessaoController.edit(s.getSessaoId());
		assertEquals(mv.getViewName(), "/sessaoAdd");
	}
//	
	@Test
	public void buscaSessaoInexistente()  {
		ModelAndView mv = sessaoController.edit(1L);
		assertEquals(mv.getViewName(), "/sessao");
		assertEquals("SessaoNot Found", mv.getModel().get("message"));
	}
//	
	@Test
	public void buscaSessaoIdNulo()  {
		ModelAndView mv = sessaoController.edit(null);
		assertEquals(mv.getViewName(), "/sessao");
		assertEquals("Sessao ID não pode ser nulo", mv.getModel().get("message"));
	}
//	
	@Test
	public void buscaSessaoIdErrado()  {
		ModelAndView mv = sessaoController.edit(-1L);
		assertEquals(mv.getViewName(), "/sessao");
		assertEquals("Sessao ID não pode ser menor ou igual a 0", mv.getModel().get("message"));
	}
//	
	@Test
	public void excluirSessaoExistente()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		ModelAndView mv2 = sessaoController.delete(s.getSessaoId());
		assertEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals(null, mv2.getModel().get("message"));
		
	}
//	
	@Test
	public void excluirSessaoInexistente()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s.setSessaoId(1L);
		//ModelAndView mv1 = sessaoController.save(s, null);
		ModelAndView mv2 = sessaoController.delete(s.getSessaoId());
		//assertEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("SessaoNot Found", mv2.getModel().get("message"));
	}
//	
	@Test
	public void excluirSessaoIdNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		//s.setSessaoId(1L);
		//ModelAndView mv1 = sessaoController.save(s, null);
		ModelAndView mv2 = sessaoController.delete(s.getSessaoId());
		//assertEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("Sessao ID não pode ser nulo", mv2.getModel().get("message"));
	}
//	
	@Test
	public void excluirSessaoIdErrado()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s.setSessaoId(-1L);
		//ModelAndView mv1 = sessaoController.save(s, null);
		ModelAndView mv2 = sessaoController.delete(s.getSessaoId());
		//assertEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("Sessao ID não pode ser menor ou igual a 0", mv2.getModel().get("message"));
	}
//	
	@Test
	public void atualizarSessaoExistente()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		ModelAndView mv2 = sessaoController.save(s, null);
		assertEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		assertEquals(null, mv2.getModel().get("message"));
	}
//	
	@Test
	public void atualizarSessaoInexistente()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		s.setSessaoId(1L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals("/sessaoAdd", mv.getViewName());
		assertEquals("SessaoNot Found", mv.getModel().get("message"));
	}
//	
	@Test
	public void atualizarSessaoDataInicioNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		s.setDataInicio(null);
		ModelAndView mv2 = sessaoController.save(s, null);
		assertNotEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("Data início não pode ser nulo", mv2.getModel().get("message"));
	}
//	
	@Test
	public void atualizarSessaoDataFimNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		s.setDataFim(null);
		ModelAndView mv2 = sessaoController.save(s, null);
		assertNotEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("Data Fim não pode ser nulo", mv2.getModel().get("message"));
	}
//	
	@Test
	public void atualizarSessaoHorarioNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		s.setHorario(null);
		ModelAndView mv2 = sessaoController.save(s, null);
		assertNotEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("Horario não pode ser nulo", mv2.getModel().get("message"));
	}
	@Test
	public void atualizarSessaoFilmeIdNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		s.setFilmeId(null);
		ModelAndView mv2 = sessaoController.save(s, null);
		assertNotEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("Filme ID não pode ser nulo", mv2.getModel().get("message"));
	}
	@Test
	public void atualizarSessaoDataSalaIdNulo()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		s.setSalaId(null);
		ModelAndView mv2 = sessaoController.save(s, null);
		assertNotEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("Sala ID não pode ser nulo", mv2.getModel().get("message"));
	}
//	
	@Test
	public void atualizarSessaoFilmeIdErrrado()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		s.setFilmeId(-1L);;
		ModelAndView mv2 = sessaoController.save(s, null);
		assertNotEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("Filme ID não pode ser menor ou igual a 0", mv2.getModel().get("message"));
	}
	@Test
	public void atualizarSessaoDataSalaIdErrado()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		s.setSalaId(-1L);;
		ModelAndView mv2 = sessaoController.save(s, null);
		assertNotEquals(mv1.getViewName(), mv2.getViewName());
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("Sala ID não pode ser menor ou igual a 0", mv2.getModel().get("message"));
	}
//	
	@Test
	public void visualizarSessoes()  {
		ModelAndView mv1 = sessaoController.findAll(null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		assertEquals(0, ((List<Sessao>) mv1.getModel().get("sessoes")).size());
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv2 = sessaoController.save(s, null);
		assertEquals("/sessao", mv2.getViewName());
		assertEquals(null, mv2.getModel().get("message"));
		assertEquals(1, ((List<Sessao>) mv2.getModel().get("sessoes")).size());
	}
//	
	@Test
	public void visualizarSessoesEntreDatas()  {
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
		ModelAndView mv1 = sessaoController.save(s1, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		assertEquals(1, ((List<Sessao>) mv1.getModel().get("sessoes")).size());
		s2.setDataInicio(dinicio2);
		s2.setDataFim(dfim2);
		s2.setHorario(new Time(c.getTimeInMillis()));
		s2.setFilmeId(2L);
		s2.setSalaId(2L);
		ModelAndView mv2 = sessaoController.save(s2, null);
		assertEquals("/sessao", mv2.getViewName());
		assertEquals(null, mv2.getModel().get("message"));
		assertEquals(2, ((List<Sessao>) mv2.getModel().get("sessoes")).size());
		
		ModelAndView mv3 = sessaoController.findAll(null, null, dinicio1, dfim2, null, null, null);
		assertEquals("/sessao", mv3.getViewName());
		assertEquals(null, mv3.getModel().get("message"));
		assertEquals(2, ((Set<Sessao>) mv3.getModel().get("sessoes")).size());
		
		ModelAndView mv4 = sessaoController.findAll(null, null, dinicio1, dfim1, null, null, null);
		assertEquals("/sessao", mv4.getViewName());
		assertEquals(null, mv4.getModel().get("message"));
		assertEquals(1, ((Set<Sessao>) mv4.getModel().get("sessoes")).size());
		
		ModelAndView mv5 = sessaoController.findAll(null, null, dinicio2, dfim2, null, null, null);
		assertEquals("/sessao", mv5.getViewName());
		assertEquals(null, mv5.getModel().get("message"));
		assertEquals(1, ((Set<Sessao>) mv5.getModel().get("sessoes")).size());
	} 
//	
	@Test
	public void visualizarSessoesPorCinemaOuCidadeCorretamente()  {
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
		ModelAndView mv1 = sessaoController.save(s1, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		assertEquals(1, ((List<Sessao>) mv1.getModel().get("sessoes")).size());
		s2.setDataInicio(dinicio2);
		s2.setDataFim(dfim2);
		s2.setHorario(new Time(c.getTimeInMillis()));
		s2.setFilmeId(2L);
		s2.setSalaId(2L);
		ModelAndView mv2 = sessaoController.save(s2, null);
		assertEquals("/sessao", mv2.getViewName());
		assertEquals(null, mv2.getModel().get("message"));
		assertEquals(2, ((List<Sessao>) mv2.getModel().get("sessoes")).size());
		ModelAndView mv3 = sessaoController.findAll(null, null, null, null, "Quixada", null, null);
		assertEquals("/sessao", mv3.getViewName());
		assertEquals(null, mv3.getModel().get("message"));
		assertEquals(2, ((Set<Sessao>) mv3.getModel().get("sessoes")).size());
	}
//	
	@Test
	public void visualizarSessoesCidadeCinemaErrado()  {
		ModelAndView mv3 = sessaoController.findAll(null, null, null, null, "123", null, null);
		assertEquals("/sessao", mv3.getViewName());
		assertEquals("Cinema ou Cidade não pode conter caracteres especiais", mv3.getModel().get("message"));
	}
//	
	@Test
	public void visualizarSessoesPorFilmeId()  {
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
		ModelAndView mv1 = sessaoController.save(s1, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		assertEquals(1, ((List<Sessao>) mv1.getModel().get("sessoes")).size());
		s2.setDataInicio(dinicio2);
		s2.setDataFim(dfim2);
		s2.setHorario(new Time(c.getTimeInMillis()));
		s2.setFilmeId(1L);
		s2.setSalaId(2L);
		ModelAndView mv2 = sessaoController.save(s2, null);
		assertEquals("/sessao", mv2.getViewName());
		assertEquals(null, mv2.getModel().get("message"));
		assertEquals(2, ((List<Sessao>) mv2.getModel().get("sessoes")).size());
		ModelAndView mv3 = sessaoController.findAll(1L, null, null, null, null, null, null);
		assertEquals("/sessao", mv3.getViewName());
		assertEquals(null, mv3.getModel().get("message"));
		assertEquals(2, ((Set<Sessao>) mv3.getModel().get("sessoes")).size());
	}
//	
	@Test
	public void visualizarSessoesPorFilmeIdErrado()  {
		ModelAndView mv3 = sessaoController.findAll(-1L, null, null, null, null, null, null);
		assertEquals("/sessao", mv3.getViewName());
		assertEquals("Filme ID não pode ser < ou igual a 0", mv3.getModel().get("message"));
	}
//	
	@Test
	public void visualizarSessoesPorGenero()  {
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
		ModelAndView mv1 = sessaoController.save(s1, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		assertEquals(1, ((List<Sessao>) mv1.getModel().get("sessoes")).size());
		s2.setDataInicio(dinicio2);
		s2.setDataFim(dfim2);
		s2.setHorario(new Time(c.getTimeInMillis()));
		s2.setFilmeId(1L);
		s2.setSalaId(2L);
		ModelAndView mv2 = sessaoController.save(s2, null);
		assertEquals("/sessao", mv2.getViewName());
		assertEquals(null, mv2.getModel().get("message"));
		assertEquals(2, ((List<Sessao>) mv2.getModel().get("sessoes")).size());
		ModelAndView mv3 = sessaoController.findAll(null, null, null, null, null, "Comedia", null);
		assertEquals("/sessao", mv3.getViewName());
		assertEquals(null, mv3.getModel().get("message"));
		assertEquals(2, ((Set<Sessao>) mv3.getModel().get("sessoes")).size());
	}
//	
//	
	@Test
	public void visualizarSessoesGeneroErrado()  {
		ModelAndView mv3 = sessaoController.findAll(null, null, null, null, null, "123", null);
		assertEquals("/sessao", mv3.getViewName());
		assertEquals("Genero não pode conter caracteres especiais", mv3.getModel().get("message"));
	}
	
	@Test
	public void addSessaoComFilmeInexistente()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(4L);
		s.setSalaId(1L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals("/sessaoAdd", mv.getViewName());
		assertEquals("FilmeNot Found", mv.getModel().get("message"));
	}
	
	@Test
	public void addSessaoComSalaInexistente()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(4L);
		ModelAndView mv = sessaoController.save(s, null);
		assertEquals("/sessaoAdd", mv.getViewName());
		assertEquals("SalaNot Found", mv.getModel().get("message"));
	}
	
	@Test
	public void atualizaSessaoComFilmeInexistente()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		s.setFilmeId(4L);
		//s.setSalaId(1L);
		ModelAndView mv2 = sessaoController.save(s, null);
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("FilmeNot Found", mv2.getModel().get("message"));
	}
	
	@Test
	public void atualizaSessaoComSalaInexistente()  {
		Sessao s = new Sessao();
		Calendar c = Calendar.getInstance();
		s.setDataInicio(new Date( c.getTimeInMillis() ) );
		c.add(Calendar.DATE, 7);
		s.setDataFim( new Date( c.getTimeInMillis() ));
		s.setHorario(new Time(c.getTimeInMillis()));
		s.setFilmeId(1L);
		s.setSalaId(1L);
		ModelAndView mv1 = sessaoController.save(s, null);
		assertEquals("/sessao", mv1.getViewName());
		assertEquals(null, mv1.getModel().get("message"));
		//s.setFilmeId(1L);
		s.setSalaId(4L);
		ModelAndView mv2 = sessaoController.save(s, null);
		assertEquals("/sessaoAdd", mv2.getViewName());
		assertEquals("SalaNot Found", mv2.getModel().get("message"));
	}

}
