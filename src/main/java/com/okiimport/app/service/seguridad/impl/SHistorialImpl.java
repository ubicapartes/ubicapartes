package com.okiimport.app.service.seguridad.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;

import com.okiimport.app.dao.seguridad.HistoryLoginRepository;
import com.okiimport.app.model.HistoryLogin;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.seguridad.SHistorial;

public class SHistorialImpl extends AbstractServiceImpl implements SHistorial {

	@Autowired
	private HistoryLoginRepository historyLoginRepository;
	
	//1. Historial de Session
	public HistoryLogin historicoSessionNoTerminada(Integer username){
		return this.historyLoginRepository.findByDateLogoutIsNullAndUsuarioId(username);
	}
	
	public void registrarHistorialSession(Usuario usuario){
		HistoryLogin history = historicoSessionNoTerminada(usuario.getId());
		if(history==null){
			history = new HistoryLogin();
			history.setUsuario(usuario);
			history.setDateLogin(new Timestamp(calendar.getTime().getTime()));
			this.historyLoginRepository.save(history);
		}
	}
	
	public void cerarHistorialSession(Usuario usuario){
		HistoryLogin history = historicoSessionNoTerminada(usuario.getId());
		if(history!=null){
			history.setDateLogout(new Timestamp(calendar.getTime().getTime()));
			this.historyLoginRepository.save(history);
		}
	}
}
