package com.okiimport.app.dao.transaccion.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.OrdenCompra;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.enumerados.EEstatusCotizacion;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class RequerimientoDAO extends AbstractJpaDao<Requerimiento> {
	
	public Specification<Requerimiento> consultarRequerimientosGeneral(final Requerimiento regFiltro){
		return new Specification<Requerimiento>(){

			public Predicate toPredicate(Root<Requerimiento> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("analista", JoinType.INNER);
				entidades.put("cliente", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				//3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				
				agregarRestriccionesFiltros(restricciones, regFiltro, joins);
				
				//4. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	};

	public Specification<Requerimiento> consultarRequerimientoUsuario(final Requerimiento regFiltro, final Integer idusuario, 
			final List<EEstatusRequerimiento> estatus) {
		return new Specification<Requerimiento>(){
			public Predicate toPredicate(Root<Requerimiento> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("analista", JoinType.INNER);
				entidades.put("cliente", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				agregarRestriccionesFiltros(restricciones, regFiltro, joins);
				
				restricciones.add(criteriaBuilder.equal(
						joins.get("analista").get("id"), idusuario));
				
				if(estatus!=null && !estatus.isEmpty())
					restricciones.add(entity.get("estatus").in(estatus));
								
				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
		};
	}
	
	public Specification<Requerimiento> consultarRequerimientosCliente(final Requerimiento regFiltro, final String cedula){
		return new Specification<Requerimiento>(){
			public Predicate toPredicate(Root<Requerimiento> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("cliente", JoinType.INNER);
				entidades.put("analista", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				//3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				agregarRestriccionesFiltros(restricciones, regFiltro, joins);
				
				if(cedula!=null)
					restricciones.add(criteriaBuilder.equal(
							joins.get("cliente").get("cedula"), cedula));
								
				//4. Ejecutamos
				return crearPredicate(restricciones);
			}
		};
	}
	
	public Specification<Requerimiento> consultarRequerimientosCotizados(final Requerimiento regFiltro, final Integer idusuario){
		return new Specification<Requerimiento>(){
			public Predicate toPredicate(Root<Requerimiento> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("analista", JoinType.INNER);
				entidades.put("cliente", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();

				agregarRestriccionesFiltros(restricciones, regFiltro, joins);
				
				restricciones.add(criteriaBuilder.equal(
						joins.get("analista").get("id"), idusuario));
				restricciones.add(criteriaBuilder.equal(entity.get("estatus"), EEstatusRequerimiento.CON_COTIZACIONES_A));

				// 4. Ejecutamos				
				return crearPredicate(restricciones);
			}
		};
	}
	
	public Specification<Requerimiento> consultarRequerimientosConSolicitudesCotizacion(final Requerimiento regFiltro, 
			final Integer idProveedor, final List<EEstatusRequerimiento> estatus){
		return new Specification<Requerimiento>(){
			public Predicate toPredicate(Root<Requerimiento> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("analista", JoinType.INNER);
				entidades.put("cliente", JoinType.INNER);
				entidades.put("detalleRequerimientos", JoinType.INNER);
				entidades.put("marcaVehiculo", JoinType.LEFT);
				entidades.put("motor", JoinType.LEFT);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				//3. Creamos los campos a seleccionar
				criteriaQuery.multiselect(new Selection[]{
						entity.get("idRequerimiento"),
						entity.get("estatus"),
						entity.get("fechaCreacion"),
						entity.get("fechaVencimiento"),
						entity.get("modeloV"),
						entity.get("tipoRepuesto"),
						joins.get("analista"),
						joins.get("cliente"),
						joins.get("marcaVehiculo"),
						joins.get("motor")
				});
				criteriaQuery.distinct(true);
				
				//4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				Join<?,?> joinCotizacion = joins.get("detalleRequerimientos").join("detalleCotizacions").join("cotizacion");
				
				agregarRestriccionesFiltros(restricciones, regFiltro, joins);
				
				if(idProveedor!=null)
					restricciones.add(criteriaBuilder.equal(
							joinCotizacion.join("proveedor").get("id"), 
							idProveedor));
				
				restricciones.add(criteriaBuilder.or( 
						criteriaBuilder.equal(joinCotizacion.get("estatus"), EEstatusCotizacion.SOLICITADA),
						criteriaBuilder.equal(joinCotizacion.get("estatus"), EEstatusCotizacion.EMITIDA)));
				restricciones.add(criteriaBuilder.not(entity.get("estatus").in(estatus)));
				
				//5. Ejecutamos
				return crearPredicate(restricciones);
			}
		};
	}
	
	public Specification<Requerimiento> consultarPorOrdenCompra(final OrdenCompra ordenCompra){
		return new Specification<Requerimiento>(){

			@Override
			public Predicate toPredicate(Root<Requerimiento> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				//1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				//2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("compras", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				//3. Creamos los campos a seleccionar
				criteriaQuery.multiselect(new Selection[]{
						entity.get("idRequerimiento"),
						entity.get("estatus"),
						entity.get("fechaCreacion"),
						entity.get("fechaVencimiento"),
						entity.get("modeloV"),
						entity.get("tipoRepuesto")
				});
				criteriaQuery.distinct(true);
				
				//4. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				if(ordenCompra!=null)
					restricciones.add(criteriaBuilder.equal(
							joins.get("compras").join("detalleOfertas").join("ordenCompra").get("idOrdenCompra"), 
							ordenCompra.getIdOrdenCompra()));
				
				//5. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}
	
	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarRestriccionesFiltros(List<Predicate> restricciones,
			Requerimiento regFiltro, Map<String, Join<?,?>> joins) {

		if (regFiltro != null) {
			if (regFiltro.getIdRequerimiento() != null) {
				restricciones.add(this.criteriaBuilder.like(
						this.entity.get("idRequerimiento").as(String.class),
						"%" + String.valueOf(regFiltro.getIdRequerimiento()) + "%"));
			}

			if (regFiltro.getFechaCreacion() != null) {
				restricciones.add(this.criteriaBuilder.like(
						this.entity.get("fechaCreacion").as(String.class),
						"%" + dateFormat.format(regFiltro.getFechaCreacion()) + "%"));
			}

			if (regFiltro.getModeloV() != null) {
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("modeloV").as(String.class)),
						"%" + String.valueOf(regFiltro.getModeloV()).toLowerCase() + "%"));
			}
			
			if(regFiltro.getFechaVencimiento() != null){
				restricciones.add(this.criteriaBuilder.like(
						this.entity.get("fechaVencimiento").as(String.class), 
						"%"+ dateFormat.format(regFiltro.getFechaVencimiento()) +"%"));
			}
			
			if(regFiltro.getSerialCarroceriaV() != null){
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("serialCarroceriaV").as(String.class)),
						"%" + String.valueOf(regFiltro.getSerialCarroceriaV()).toLowerCase() + "%"));
			}
			
			if (regFiltro.getEstatus() != null)
			{
				restricciones.add(this.criteriaBuilder.like(this.entity.get("estatus").as(String.class), 
						"%" + regFiltro.getEstatus() + "%"));
			}
			
			if(joins!=null){
				//Cliente
				Cliente cliente = regFiltro.getCliente();
				if (cliente != null && joins.get("cliente") != null) {
					if (cliente.getNombre() != null){
						restricciones.add(this.criteriaBuilder.like(
								this.criteriaBuilder.lower(joins.get("cliente").get("nombre").as(String.class)),
								"%"+ String.valueOf(cliente.getNombre()).toLowerCase() + "%"));
					}
				}
				
				
					
				//Analista
				Analista analista = regFiltro.getAnalista();
				if(analista!=null && joins.get("analista") != null){
					if(analista.getNombre() != null)
						restricciones.add(this.criteriaBuilder.or(this.criteriaBuilder.like(
								this.criteriaBuilder.lower(joins.get("analista").get("nombre").as(String.class)),
								"%"+ String.valueOf(analista.getNombre()).toLowerCase() + "%"),this.criteriaBuilder.like(
								this.criteriaBuilder.lower(joins.get("analista").get("apellido").as(String.class)),
								"%"+ String.valueOf(analista.getNombre()).toLowerCase() + "%")));

				}
				
				//Marca Vehiculo
				MarcaVehiculo marcaVehiculo = regFiltro.getMarcaVehiculo();
				if(marcaVehiculo!=null && joins.get("marcaVehiculo") != null){
					if(marcaVehiculo.getNombre()!=null)
						restricciones.add(this.criteriaBuilder.like(
								this.criteriaBuilder.lower(joins.get("marcaVehiculo").get("nombre").as(String.class)), 
								"%"+String.valueOf(marcaVehiculo.getNombre()).toLowerCase()+"%"));
				}
			}
		}
	}
}
