package com.okiimport.app.service.mail.impl;

import java.util.HashMap;
import java.util.Map;

import com.okiimport.app.service.mail.MailAdmin;
import com.okiimport.app.service.mail.MailService;

public class MailAdminImpl extends AbstractMailImpl implements MailAdmin {

	
	public void enviarInformacionContacto(final String correoAdmin, final String nombre,
			final String tlf, final String correoContacto, final String mensaje,
			final MailService mailService) {
		super.sendMail(new Runnable(){
			@Override
			public void run() {
				try {
					System.out.println("entre al metodo");
					System.out.println("datos a enviar: "+correoAdmin+" "+nombre+" "+tlf+" "+correoContacto+" "+mensaje);
					final Map<String, Object> model = new HashMap<String, Object>();
					model.put("fechaEnvio", dateFormat.format(calendar.getTime()));
					model.put("nombre", nombre);
					model.put("telefono", tlf);
					model.put("correo", correoContacto);
					model.put("mensaje", mensaje);

					
					mailService.send(correoAdmin, "Mensaje de usuario a contactar",
							"contactarAdmin.html", model);
					System.out.println("envie correo.");
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
	}

	

}
