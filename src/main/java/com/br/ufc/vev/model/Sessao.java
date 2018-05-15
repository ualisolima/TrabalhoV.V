package com.br.ufc.vev.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Sessao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long sessaoId;
	
	@Column
	private long filmeId;
	
	@Column
	private long salaId;
	
	@Column(nullable = false)
	@NotBlank(message = "Horario da sessão é uma informação obrigatória")
	private Time horario;
	
	@Column(nullable = false)
	@NotBlank(message = "Início da sessão é uma informação obrigatória")
	private Date dataInicio;
	
	@Column(nullable = false)
	@NotBlank(message = "Fim da sessão é uma informação obrigatória")
	private Date dataFim;

	public Long getSessaoId() {
		return sessaoId;
	}

	public void setSessaoId(Long sessaoId) {
		this.sessaoId = sessaoId;
	}

	public long getFilmeId() {
		return filmeId;
	}

	public void setFilmeId(long filmeId) {
		this.filmeId = filmeId;
	}

	public long getSalaId() {
		return salaId;
	}

	public void setSalaId(long salaId) {
		this.salaId = salaId;
	}

	public Time getHorario() {
		return horario;
	}

	public void setHorario(Time horario) {
		this.horario = horario;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	

}
