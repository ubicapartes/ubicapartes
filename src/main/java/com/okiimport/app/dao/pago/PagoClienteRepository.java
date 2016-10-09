package com.okiimport.app.dao.pago;


import java.util.Collection;

import com.okiimport.app.model.Compra;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.model.enumerados.EEstatusCompra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;



@Repository
public interface PagoClienteRepository extends PagoRepository<PagoCliente>{
	public PagoCliente findByCompra(Compra compra);
}
