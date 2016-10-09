package com.okiimport.app.service.web.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.okiimport.app.dao.pago.PagoClienteRepository;
import com.okiimport.app.dao.transaccion.CompraRepository;
import com.okiimport.app.dao.transaccion.detalle.oferta.DetalleOfertaRepository;
import com.okiimport.app.model.Compra;
import com.okiimport.app.model.DetalleOferta;
import com.okiimport.app.model.FormaPago;
import com.okiimport.app.model.HistoricoMoneda;
import com.okiimport.app.model.Pago;
import com.okiimport.app.model.PagoCliente;
import com.okiimport.app.model.enumerados.EEstatusCompra;
import com.okiimport.app.model.enumerados.EEstatusFormaPago;
import com.okiimport.app.resource.service.AbstractServiceImpl;
import com.okiimport.app.service.configuracion.SControlConfiguracion;
import com.okiimport.app.service.web.SPago;

public class SPagoImpl extends AbstractServiceImpl implements SPago {

	private static DecimalFormat decimalFormat;

	@Autowired
	private PagoClienteRepository pagoRepository;

	@Autowired
	private CompraRepository compraRepository;

	@Autowired
	private DetalleOfertaRepository detalleOfertaRepository;

	public Boolean guardarPagoCliente(SControlConfiguracion sControlConfiguracion, BraintreeGateway gateway, PagoCliente pagoCliente) {
		Boolean valor = false;
		try {
			Compra compra = pagoCliente.getCompra();
			for (DetalleOferta detalle : pagoCliente.getCompra()
					.getDetalleOfertas()) {
				detalle.setCompra(compra);
				detalleOfertaRepository.save(detalle);
			}

			pagoRepository.save(pagoCliente);
			valor = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return valor;
	}
	
	// Formas de pago
		public Map<String, Object> consultarPagoByCompra(Compra c) {
				Map<String, Object> parametros = new HashMap<String, Object>();
				PagoCliente pago = null;
				pago = this.pagoRepository.findByCompra(c);
				parametros.put("formasPago", pago);
				return parametros;
		}

	public Boolean registrarPagoEfectivo(PagoCliente pago) {
		Boolean valor = false;
		try {

			this.pagoRepository.save(pago);
			valor = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valor;
	}

	public Boolean registrarPago(PagoCliente pago) {
		Boolean valor = false;
		try {

			this.pagoRepository.save(pago);
			valor = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valor;
	}

	/** METODOS ESTATICOS DE LA CLASE */
	private static DecimalFormat getDecimalFormat() {
		if (decimalFormat == null) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setDecimalSeparator('.');
			symbols.setGroupingSeparator(',');
			decimalFormat = new DecimalFormat("#.##", symbols);
		}
		return decimalFormat;
	}

	/** METODOS PROPIOS DE LA CLASE */
	// Metodos Privados
	private Result<Transaction> crearTransaccion(
			SControlConfiguracion sControlConfiguracion,
			BraintreeGateway gateway, Pago pago) {
		DecimalFormat decimalFormat = getDecimalFormat();

		HistoricoMoneda historico = sControlConfiguracion
				.consultarActualConversionMonedaBase();
		System.out.println(new BigDecimal((float) historico.convert(pago
				.getMonto())).toPlainString());
		System.out.println(pago.getPaymentMethodNonce());
		TransactionRequest request = new TransactionRequest()
				.amount(new BigDecimal(decimalFormat.format((float) historico
						.convert(pago.getMonto()))))
				.paymentMethodNonce(pago.getPaymentMethodNonce())
				.merchantAccountId("okiimport").options()
				.submitForSettlement(true).done();

		return gateway.transaction().sale(request);
	}

}
