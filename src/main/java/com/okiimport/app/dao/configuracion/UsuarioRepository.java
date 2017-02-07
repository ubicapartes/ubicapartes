package com.okiimport.app.dao.configuracion;

import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface UsuarioRepository extends IGenericJPARepository<Usuario, Integer> {
	Usuario findByUsernameIgnoreCaseAndPaswordIgnoreCase(String username, String pasword);
	Usuario findByUsernameIgnoreCase(String username);
	Usuario findByPersonaCorreo(String correo);
	Usuario findByPersonaId(Integer id);
	Usuario findByToken(String token);
}
