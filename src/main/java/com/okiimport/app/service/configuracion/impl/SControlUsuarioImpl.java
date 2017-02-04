package com.okiimport.app.service.configuracion.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.GrantedAuthorityImpl;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



import com.okiimport.app.dao.configuracion.MenuRepository;
import com.okiimport.app.dao.configuracion.UsuarioRepository;
import com.okiimport.app.dao.configuracion.impl.UsuarioDAO;
import com.okiimport.app.model.Menu;
import com.okiimport.app.model.Persona;
import com.okiimport.app.model.Usuario;
import com.okiimport.app.model.factory.persona.EstatusAnalistaFactory;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.resource.service.PasswordGenerator;
import com.okiimport.app.service.configuracion.SControlConfiguracion;
import com.okiimport.app.service.configuracion.SControlUsuario;
import com.okiimport.app.service.maestros.SMaestros;

@Service
public class SControlUsuarioImpl extends AbstractServiceImpl implements SControlUsuario {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MenuRepository menuRepository;

	public SControlUsuarioImpl() {
	}
	
	//1.Usuario
	@Override
	public Usuario consultarUsuario(Integer id){
		return this.usuarioRepository.findOne(id);
	}
	
	@Override
	public Usuario consultarUsuario(String correo){
		return this.usuarioRepository.findByPersonaCorreo(correo);
	}
	
	@Override
	public Usuario consultarUsuario(String username, String clave, SControlConfiguracion sControlConfiguracion) {
		Usuario usuario = null;
		if(clave != null)
			usuario = this.usuarioRepository.findByUsernameIgnoreCaseAndPaswordIgnoreCase(username, clave);
		else
			usuario = this.usuarioRepository.findByUsernameIgnoreCase(username);
		
		if(usuario!=null && sControlConfiguracion!=null)
			sControlConfiguracion.consultarActualConversion(usuario.getPersona());
		
		return usuario;
	}
	
	@Override
	public Usuario grabarUsuario(Usuario usuario, SMaestros sMaestros) {
		//usuario.setPasword(this.bcryptEncoder.encode(usuario.getPasword()));
		Persona persona = usuario.getPersona();
		persona = sMaestros.acutalizarPersona(persona);
		usuario.setPersona(persona);
		usuario.setUsername(persona.getCorreo());
		return actualizarUsuario(usuario, true);
	}
	
	@Override
	public Usuario actualizarUsuario(Usuario usuario, boolean encriptar){
		/*if(encriptar)
			usuario.setPasword(this.bcryptEncoder.encode(usuario.getPasword()));*/
		return this.usuarioRepository.save(usuario);
	}
	
	@Override
	public Boolean cambiarEstadoUsuario(Usuario usuario, boolean estado){
		if((usuario=consultarUsuario(usuario.getId()))!=null) {
			usuario.setActivo(estado);
			actualizarUsuario(usuario, false);
			return true;
		}
		
		return false;
	}
	
	@Override
	public Map<String, Object> consultarUsuarios(Usuario usuarioF, String fieldSort, Boolean sortDirection, 
			int pagina, int limit) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		Integer total = 0;
		List<Usuario> usuarios = null;
		Sort sortUsuario = new Sort(getDirection(sortDirection, Sort.Direction.ASC), getFieldSort(fieldSort, "id"));
		Specification<Usuario> specfUsuario = (new UsuarioDAO()).consultarUsuarios(usuarioF);
		if(limit != -1){
			Page<Usuario> pageUsuario = this.usuarioRepository.findAll(specfUsuario, new PageRequest(pagina, limit, sortUsuario));
			total = Long.valueOf(pageUsuario.getTotalElements()).intValue();
			usuarios = pageUsuario.getContent();
		}
		else {
			usuarios = this.usuarioRepository.findAll(specfUsuario, sortUsuario);
			total = usuarios.size();
		}
		parametros.put("total", total);
		parametros.put("usuarios", usuarios);
		return parametros;
	}
	
	@Override
	public Usuario crearUsuario(Persona persona, SMaestros sMaestros){
		persona.setiEstatus(EstatusAnalistaFactory.getEstatusActivo());
		Usuario usuario = new Usuario();
		persona.setUsuario(usuario);
		usuario.setPersona(persona);
		usuario.setUsername(buscarUsername(persona));
		usuario.setPasword(PasswordGenerator.getPassword(PasswordGenerator.MINUSCULAS+PasswordGenerator.MAYUSCULAS
				+PasswordGenerator.NUMEROS,10));
		usuario = grabarUsuario(usuario, sMaestros);
		sMaestros.acutalizarPersona(persona);
		return usuario;
	}
	
	@Override
	public String buscarUsername(Persona persona) {
		boolean noValido = true;
		String usuario = persona.getNombre().split(" ")[0].toLowerCase();
		String username = usuario;
		while (noValido) {
			noValido = verificarUsername(username);
			if (noValido)
				username = usuario
						+ PasswordGenerator.getPassword(
								PasswordGenerator.NUMEROS + PasswordGenerator.MAYUSCULAS, 3);
		}
		return username;
	}
	
	@Override
	public boolean verificarUsername(String username){
		Usuario usuario = consultarUsuario(username, null, null);
		return (usuario!=null);
	}
	
	@Override
	public Usuario consultarUsuario(int idPersona) {
		return this.usuarioRepository.findByPersonaId(idPersona);
	}
	
	//2. Menus
	@Override
	public List<Menu> consultarPadresMenuUsuario(Integer tipo) {
		Sort sortMenu = new Sort(Sort.Direction.ASC, "idMenu");
		return this.menuRepository.findByPadreNullAndTipo(tipo, sortMenu);
	}

	@Override
	public List<Menu> consultarHijosMenuUsuario(Integer tipo) {
		Sort sortMenu = new Sort(Sort.Direction.ASC, "idMenu");
		return this.menuRepository.findByHijosNullAndTipo(tipo, sortMenu);
	}
}
