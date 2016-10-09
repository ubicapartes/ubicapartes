package com.okiimport.app.dao.transaccion;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.okiimport.app.model.Cliente;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.enumerados.EEstatusCompra;
import com.okiimport.app.resource.dao.IGenericJPARepository;

@Repository
public interface CompraRepository extends IGenericJPARepository<Compra, Integer> {
	public Page<Compra> findByRequerimientoClienteAndEstatus(Cliente c, EEstatusCompra estatus, Pageable pageable);
	public Page<Compra> findByRequerimientoCliente(Cliente c, Pageable pageable);
	public Page<Compra> findByEstatusIn(Collection<EEstatusCompra> estatus, Pageable pageable);

}
