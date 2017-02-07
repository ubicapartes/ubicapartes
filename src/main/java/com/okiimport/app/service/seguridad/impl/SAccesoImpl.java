package com.okiimport.app.service.seguridad.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.okiimport.app.model.Usuario;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.configuracion.SControlUsuario;
import com.okiimport.app.service.seguridad.SAcceso;

public class SAccesoImpl extends AbstractServiceImpl implements SAcceso {
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder; //Encriptador de Claves
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SControlUsuario sControlUsuario;

	public SAccesoImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**OVERRIDE METHODS*/
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = sControlUsuario.consultarUsuario(username, null, null);
		if(usuario!=null){
			if(usuario.getActivo()){
				//Depurar las Autoridades
				List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				/*if(usuario.getGroupMembers()!=null)
					for(GroupMember grupoMiembro : usuario.getGroupMembers()){
						Group grupo = grupoMiembro.getGroup();
						if(grupo.getAuthorities()!=null)
							for(Authority authority : grupo.getAuthorities())
								authorities.add(new GrantedAuthorityImpl(authority.getAuthority()));

					}*/
				User securityUser = new User(username, usuario.getPasword(), true, true, true, true,  authorities);
				return securityUser;
			}
			else
				throw new UsernameNotFoundException("Usuario No Activo!!!");
		}
		else
			throw new UsernameNotFoundException("Usuario No Encontrado!!!");
	}

	/**INTERFACES*/
	//1. SAcceso
	@Override
	public Usuario consultarUsuario(String email){
		return this.sControlUsuario.consultarUsuario(email);
	}
	
	@Override
	public Boolean validarAutenticacion(User user) {
		try{
			UsernamePasswordAuthenticationToken auth = consultarAutenticacion(user);
			authenticationManager.authenticate(auth);
			return auth.isAuthenticated();
		}
		catch(Exception e){
			System.out.println("Error en Autenticar: "+e.getMessage());
		}
		
		return false;
	}

	@Override
	public UsernamePasswordAuthenticationToken consultarAutenticacion(User user) {
		UserDetails userDetails = loadUserByUsername(user.getUsername());
		return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
	}

	@Override
	public Usuario generarTokenParaCambiarPassword(Usuario usuario) {
		usuario.setToken(bcryptEncoder.encode(usuario.getUsername()));
		return sControlUsuario.actualizarUsuario(usuario, false);
	}

	@Override
	public Usuario consultarToken(String token) {
		return this.sControlUsuario.consultarUsuarioByToken(token);
	}

}
