package com.okiimport.app.service.transaccion;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.okiimport.app.model.Analista;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.Cotizacion;
import com.okiimport.app.model.DetalleCotizacion;
import com.okiimport.app.model.DetalleCotizacionInternacional;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.DetalleRequerimiento;
import com.okiimport.app.model.Oferta;
import com.okiimport.app.model.OrdenCompra;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.model.Proveedor;
import com.okiimport.app.model.Requerimiento;
import com.okiimport.app.model.Vehiculo;
import com.okiimport.app.service.configuracion.SControlConfiguracion;
import com.okiimport.app.service.configuracion.SControlUsuario;
import com.okiimport.app.service.maestros.SMaestros;

@Service
@Transactional
public interface STransaccion {
	
	//Requerimiento
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Requerimiento registrarRequerimiento(Requerimiento requerimiento, boolean asignarAnalista, SMaestros sMaestros);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Requerimiento actualizarRequerimiento(Requerimiento requerimiento);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	void actualizarDetallesRequerimiento(Requerimiento requerimiento);
	
	@Transactional(readOnly=true)
	void asignarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarRequerimientosGeneral(Requerimiento regFiltro, String fieldSort, Boolean sortDirection,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarMisRequerimientosEmitidos(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarMisRequerimientosProcesados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarMisRequerimientosOfertados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);

	@Transactional(readOnly=true)
	Map<String, Object> consultarRequerimientosCliente(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, String cedula,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> RequerimientosCotizados(Requerimiento regFiltro, String fieldSort, Boolean sortDirection, Integer idusuario,
			int pagina, int limit);

	@Transactional(readOnly=true)
	Map <String, Object> consultarRequerimientosConSolicitudesCotizacion(Requerimiento regFiltro, String fieldSort, 
			Boolean sortDirection, Integer idProveedor, int pagina, int limit);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Requerimiento reactivarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Requerimiento cerrarRequerimiento(Requerimiento requerimiento, SMaestros sMaestros, SControlUsuario sControlUsuario, boolean aprobarProveedores);
	
	//DetalleRequerimiento
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	DetalleRequerimiento registrarDetalleRequerimiento(int idRequerimiento, DetalleRequerimiento detalleRequerimiento);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarDetallesRequerimiento(int idRequerimiento, int pagina, int limit);
	
	//Vehiculo
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Vehiculo actualizarVehiculo(Vehiculo vehiculo);
	
	//Cotizaciones
	@Transactional(readOnly=true)
	Map<String, Object> ConsultarCotizacionesRequerimiento(Cotizacion cotFiltro, String fieldSort, Boolean sortDirection, Integer idrequerimiento,
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarSolicitudCotizaciones(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int idProveedor, int pagina, int limit);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Cotizacion ActualizarCotizacion(Cotizacion cotizacion);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Cotizacion registrarSolicitudCotizacion(Cotizacion cotizacion, List<DetalleCotizacion> detalleCotizacions);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Cotizacion registrarCotizacion(Cotizacion cotizacion, Requerimiento requerimiento);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarCotizacionesParaEditar(Cotizacion cotizacionF, String fieldSort, Boolean sortDirection,
			Integer idRequerimiento, int pagina, int limit);
	
	@Transactional(readOnly=true)
	Boolean validarProveedorEnCotizaciones(Proveedor proveedor);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Cotizacion registrarRecotizacion(Requerimiento requerimiento, Proveedor proveedor, List<DetalleCotizacion> detalles);
	
	//Detalle de Cotizaciones	
	@Transactional(readOnly=true)
	List<DetalleCotizacion> consultarDetallesCotizacion(int idCotizacion);
	
	@Transactional(readOnly=true)
	Map<DetalleRequerimiento, List<DetalleCotizacion>> consultarDetallesCotizacionEmitidos(Integer idRequerimiento);
	
		//Internacional
	@Transactional(readOnly=true)
	List<DetalleCotizacionInternacional> consultarDetallesCotizacion(Integer idCotizacion);
	
	//Ofertas
	@Transactional(readOnly=true)
	Map<String, Object> consultarOfertasPorRequerimiento(int idRequerimiento, String fieldSort, Boolean sortDirection, 
			int pagina, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarOfertasRecibidasPorRequerimiento(int idRequerimiento, int pagina, int limit);
	
	@Transactional(readOnly=true)
	List<Oferta> consultarOfertasEnviadaPorRequerimiento(int idRequerimiento);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Oferta actualizarOferta(Oferta oferta);
	
	@Transactional(readOnly=true)
	List<Oferta> consultarOfertasNoEnviada(Requerimiento requerimiento);
	
	//Detalle Oferta
	@Transactional(readOnly=true)
	List<DetalleOferta> consultarDetallesOferta(Oferta oferta);
	
	//Carrito de compra de un cliente
	@Transactional(readOnly=true)
	List<DetalleOferta> consultarDetallesOfertaInShoppingCar(Integer idCliente);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarSolicitudesCompraProveedor(Requerimiento requerimiento, Proveedor proveedor, int page, int limit);
	
	//Compras
	@Transactional(readOnly=true)
	Map<String, Object> consultarComprasPorRequerimiento(Compra compraF, int idRequerimiento, String fieldSort, Boolean sortDirection,
			int pagina, int limite);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarComprasDelCliente(String cedula, String fieldSort, Boolean sortDirection,
			int page, int limit);
	
	@Transactional(readOnly=true)
	Map<String, Object> consultarComprasGeneral(String fieldSort, Boolean sortDirection,
			int page, int limit);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Compra registrarOActualizarCompra(Compra compra);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Compra registrarSolicitudCompra(Compra compra);
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	Compra registrarCompra(Compra compra, Requerimiento requerimiento, boolean cambiarEstatus);
	
	//DetalleCompra
	@Transactional(readOnly=true)
	Map<String, Object> consultarDetallesCompra(int idCompra, String fieldSort, Boolean sortDirection, 
			int pagina, int limite);
	
	//OrdenCompra
	@Transactional(readOnly=true)
	Map<String, Object> consultarOrdenesCompraProveedor(OrdenCompra ordenCompra, Requerimiento requerimiento, 
			String fieldSort, Boolean sortDirection, int page, int limit);
	
	@Transactional(readOnly=true)
	void guardarOrdenCompra(Compra compra, SControlConfiguracion sControlConfiguracion);
	
	//Pagos
		@Transactional(readOnly=true)
		Map<String, Object> consultarPagosClientes(PagoCliente pagoFiltro,  String fieldSort, Boolean sortDirection, 
				 int page, int limit);
	
		@Transactional(readOnly=true)
	List<DetalleCotizacion> consultarDetalleContizacionEmitidosPorRequerimiento(Integer idRequerimiento);
		
		@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
		void actualizarDetalleCotizacion(DetalleCotizacion detalle);
		
		@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
		void actualizarDetallesOferta(DetalleOferta detalle);
		
		@Transactional(readOnly=true)
		Boolean validarAnalistaEnRequerimientos(Analista analista);
}
