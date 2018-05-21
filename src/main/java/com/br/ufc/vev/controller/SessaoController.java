package com.br.ufc.vev.controller;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.br.ufc.vev.exceptions.ModelAlreadyExists;
import com.br.ufc.vev.exceptions.ModelNotFound;
import com.br.ufc.vev.exceptions.ValoresErradosException;
import com.br.ufc.vev.exceptions.ValoresNulosException;
import com.br.ufc.vev.model.Sessao;
import com.br.ufc.vev.service.SessaoService;

@Controller
public class SessaoController {
	
	@Autowired
	private SessaoService sessaoService;
	
	public void setSessaoService(SessaoService sessaoService) {
		this.sessaoService = sessaoService;
	}
	
	@GetMapping("/")
    public ModelAndView findAll(String message) {
        ModelAndView mv = new ModelAndView("/sessao");
        mv.addObject("message", message);
        mv.addObject("sessoes", sessaoService.findAll());    
        return mv;
    }
	
	@PostMapping("/search")
    public ModelAndView findAll(@RequestParam(value="filmeId",required=false) Long filmeId, 
    		@RequestParam(value="salaId",required=false) Long salaId,
    		@RequestParam(value="dataInicio",required=false) Date dataInicio,
    		@RequestParam(value="dataFim",required=false) Date dataFim,
    		@RequestParam(value="cidade",required=false) String cidade,
    		@RequestParam(value="genero",required=false) String genero,
    		String message) {
		try {
			ModelAndView mv = new ModelAndView("/sessao");
	        mv.addObject("message", message);
	        System.out.println(cidade);
	        System.out.println(cidade);
	        Set<Sessao> sessoes = new HashSet<Sessao>();
			if (dataInicio != null)
				sessoes.addAll(sessaoService.findByDataInicioDataFim(dataInicio, dataFim));
			if (filmeId != null)
				sessoes.addAll(sessaoService.findByFilmeId(filmeId));
			if (salaId != null)
				sessoes.addAll(sessaoService.findBySalaId(salaId));
			if (genero != null)
				sessoes.addAll(sessaoService.findByGenero(genero));
			if (cidade != null)
				sessoes.addAll(sessaoService.findByCinemaOrCidade(cidade));
			mv.addObject("sessoes", sessoes);    
	        return mv;
		} catch (ValoresNulosException | ValoresErradosException | ModelNotFound e) {
			// TODO Auto-generated catch block
			return findAll(e.getMessage());
		}
    }
	
	@GetMapping("/add")
    public ModelAndView add(Sessao sessao, String message) {
         
        ModelAndView mv = new ModelAndView("/sessaoAdd");
        mv.addObject("sessao", sessao);
        mv.addObject("message", message);
         
        return mv;
    }
     
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
    	try {
			return add(sessaoService.findOne(id), null);
		} catch (ModelNotFound | ValoresNulosException | ValoresErradosException e) {
			// TODO Auto-generated catch block
			return findAll(e.getMessage());
		}
    }
     
    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
         
        try {
			sessaoService.delete(id);
			return findAll(null);
		} catch (ModelNotFound | ValoresNulosException | ValoresErradosException e) {
			// TODO Auto-generated catch block
			return findAll(e.getMessage());
		}
         
    
    }
 
    @PostMapping("/save")
    public ModelAndView save(@Valid Sessao sessao, BindingResult result) {
         
        if(result != null && result.hasErrors()) {
            return add(sessao, null);
        }
         
        try {
        	Sessao s = sessaoService.save(sessao);
        	sessao.setSessaoId(s.getSessaoId());
		} catch (ModelNotFound | ValoresNulosException | ModelAlreadyExists | ValoresErradosException e) {
			// TODO Auto-generated catch block
			return add(sessao, e.getMessage());
		}
         
        return findAll(null);
    }
	

}
