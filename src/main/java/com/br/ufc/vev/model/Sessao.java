package com.br.ufc.vev.model;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sessao {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long sessaoId;
	
	@Column
	private Long filmeId;
	
	@Column
	private Long salaId;
	
	@Column
	private Time horario;
	
	@Column
	private Date dataInicio;
	
	@Column
	private Date dataFim;

	public Long getSessaoId() {
		return sessaoId;
	}

	public void setSessaoId(Long sessaoId) {
		this.sessaoId = sessaoId;
	}

	public Long getFilmeId() {
		return filmeId;
	}

	public void setFilmeId(Long filmeId) {
		this.filmeId = filmeId;
	}

	public Long getSalaId() {
		return salaId;
	}

	public void setSalaId(Long salaId) {
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
	
	@Override
	public boolean equals(Object obj) {
		Sessao other = (Sessao) obj;
		return this.getSessaoId().equals(other.getSessaoId()) 
				&& this.getSalaId().equals(other.getSalaId()) 
				&& this.getFilmeId().equals(other.getFilmeId())
				&& this.getDataInicio().getTime() == other.getDataInicio().getTime()
				&& this.getDataInicio().getTime() == other.getDataInicio().getTime()
				&& this.getDataFim().getTime() == other.getDataFim().getTime()
				&& this.getHorario().getTime() == other.getHorario().getTime();
	}
	
	

}
