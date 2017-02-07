package com.okiimport.app.service.mail;


public interface MailAdmin {

	void enviarInformacionContacto(final String correoAdmin, final String nombre, final String tlf, final String correoContacto, final String mensaje, final MailService mailService);
}
