package com.br.ufc.vev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.br.ufc.vev.service.SessaoService;

@Controller
public class SessaoController {
	
	@Autowired
	private SessaoService sessaoService;
	
	@GetMapping("/")
    public ModelAndView findAll() {
         
        ModelAndView mv = new ModelAndView("/sessao");
        mv.addObject("sessoes", sessaoService.findAll());    
        return mv;
    }

}
