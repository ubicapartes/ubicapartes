package com.okiimport.app.dao.pago;


import com.okiimport.app.model.Compra;
import com.okiimport.app.model.PagoCliente;
import org.springframework.stereotype.Repository;



@Repository
public interface PagoClienteRepository extends PagoRepository<PagoCliente>{
	public PagoCliente findByCompra(Compra c);
}
