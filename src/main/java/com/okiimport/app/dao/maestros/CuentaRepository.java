package com.okiimport.app.dao.maestros;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Cuenta;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface CuentaRepository extends IGenericJPARepository<Cuenta, Integer> {
	List<Cuenta> findByEstatus(EEstatusGeneral estatus);
	Page<Cuenta> findByEstatus(EEstatusGeneral estatus, Pageable pageable);
	List<Cuenta> findByProveedorId(Integer id);
	Page<Cuenta> findByProveedorId(Integer id, Pageable pageable);
	List<Cuenta> findByProveedorIdIsNull(Integer id);
	Page<Cuenta> findByProveedorIdIsNull(Integer id, Pageable pageable);
}
