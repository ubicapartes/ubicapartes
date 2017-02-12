package com.okiimport.app.dao.maestros.impl;

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

import org.springframework.data.jpa.domain.Specification;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Banco;
import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Cuenta;
import com.okiimport.app.model.MarcaVehiculo;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.resource.dao.AbstractJpaDao;

public class CuentaDAO extends AbstractJpaDao<Cuenta>{
	public Specification<Cuenta> consultarCuentas(final Cuenta CuentaF){
		return new Specification<Cuenta>(){
			public Predicate toPredicate(Root<Cuenta> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("banco", JoinType.INNER);
				if(CuentaF.getProveedor()!=null && CuentaF.getProveedor().getId()==-1){
					entidades.put("proveedor", JoinType.INNER);
				}
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				agregarRestricciones(CuentaF, restricciones, joins);
				
				if(CuentaF.getProveedor()!=null && CuentaF.getProveedor().getId()!=null && CuentaF.getProveedor().getId()!=-1 && !CuentaF.getProveedor().getId().toString().isEmpty()){
					restricciones.add(criteriaBuilder.equal(entity.get("proveedor").get("id"), CuentaF.getProveedor().getId()));
				}
				else if(CuentaF.getProveedor()!=null && CuentaF.getProveedor().getId()==null){
					restricciones.add(criteriaBuilder.isNull(entity.get("proveedor").get("id")));
				}

				// 4. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}

	public Specification<Cuenta> consultarCuentasUbicapartes(final Cuenta CuentaF){
		return new Specification<Cuenta>(){
			public Predicate toPredicate(Root<Cuenta> entity, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				// 1. Inicializar Variables
				inicializar(entity, criteriaQuery, criteriaBuilder);
				
				// 2. Generamos los Joins
				Map<String, JoinType> entidades = new HashMap<String, JoinType>();
				entidades.put("banco", JoinType.INNER);
				Map<String, Join<?,?>> joins = crearJoins(entidades);
				
				
				// 3. Creamos las Restricciones de la busqueda
				List<Predicate> restricciones = new ArrayList<Predicate>();
				agregarRestricciones(CuentaF, restricciones, joins);
				

				if(CuentaF.getProveedor()!=null && CuentaF.getProveedor().getId()==null){
					restricciones.add(criteriaBuilder.isNull(entity.get("proveedor").get("id")));
				}

				// 4. Ejecutamos
				return crearPredicate(restricciones);
			}
			
		};
	}

	/**METODOS PRIVADOS DE LA CLASE*/
	private void agregarRestricciones(final Cuenta CuentaF, final List<Predicate> restricciones, final Map<String, Join<?,?>> joins){

		if(CuentaF!=null){
				
			if (CuentaF.getNumero() != null) {
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("numero").as(String.class)),
						"%"+String.valueOf(CuentaF.getNumero()).toLowerCase()+"%"));
			}
			
			if (CuentaF.getTipo() != null) {
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("tipo").as(String.class)),
						"%"+String.valueOf(CuentaF.getTipo()).toLowerCase()+"%"));
			}
			
			if (CuentaF.getTitular() != null) {
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("titular").as(String.class)),
						"%"+String.valueOf(CuentaF.getTitular()).toLowerCase()+"%"));
			}
			
			if (CuentaF.getIdentificacion() != null) {
				restricciones.add(this.criteriaBuilder.like(
						this.criteriaBuilder.lower(this.entity.get("identificacion").as(String.class)),
						"%"+String.valueOf(CuentaF.getIdentificacion()).toLowerCase()+"%"));
			}
			

			
			if(joins!=null){
				//Banco
				Banco banco = CuentaF.getBanco();
				if (banco != null && joins.get("banco") != null) {
					if (banco.getNombre() != null){
						restricciones.add(this.criteriaBuilder.like(
								this.criteriaBuilder.lower(joins.get("banco").get("nombre").as(String.class)),
								"%"+ String.valueOf(banco.getNombre()).toLowerCase() + "%"));
					}
				}
				
				//Banco
				Proveedor proveedor = CuentaF.getProveedor();
				if (proveedor != null && joins.get("proveedor") != null) {
					if (proveedor.getNombre() != null){
						restricciones.add(this.criteriaBuilder.like(
								this.criteriaBuilder.lower(joins.get("proveedor").get("nombre").as(String.class)),
								"%"+ String.valueOf(proveedor.getNombre()).toLowerCase() + "%"));
					}
				}
				
			}
			
			
		}
	}

}
