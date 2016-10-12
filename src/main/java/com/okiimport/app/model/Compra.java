package com.okiimport.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.okiimport.app.model.enumerados.EEstatusCompra;
import com.okiimport.app.model.enumerados.EEstatusRequerimiento;
import com.okiimport.app.resource.model.AbstractEntity;
import com.okiimport.app.resource.service.model.FleteZoom;

/**
 * The persistent class for the compra database table.
 * 
 */
@Entity
@Table(name="compra")
@NamedQuery(name="Compra.findAll", query="SELECT c FROM Compra c")
@JsonIgnoreProperties({"detalleOfertas"})
public class Compra extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="compra_id_seq")
	@SequenceGenerator(name="compra_id_seq", sequenceName="compra_id_seq", initialValue=1, allocationSize=1)
	@Column(name="id_compra")
	private Integer idCompra;
	
	@Column(name="precio_venta")
	private Float precioVenta;
	
	@Column(name="precio_flete")
	private Float precioFlete;
	
	@Column(name="tipo_flete")
	private Boolean tipoFlete;
	
	private String observacion;
	
	@Enumerated(EnumType.STRING)
	private EEstatusCompra estatus;
	
	//bi-directional many-to-one association to Requerimiento
	@ManyToOne
	@JoinColumn(name="id_requerimiento")
	private Requerimiento requerimiento;
	
	//bi-directional many-to-one association to HistoricoMoneda
	@ManyToOne
	@JoinColumn(name="id_historico_moneda")
	private HistoricoMoneda historicoMoneda;
	
	//bi-directional one-to-many association to DetalleOferta
	@OneToMany(mappedBy="compra", fetch=FetchType.EAGER)
	private List<DetalleOferta> detalleOfertas;

	@Transient
	private FleteZoom fleteZoom;

	public Compra() {
		this.detalleOfertas = new ArrayList<DetalleOferta>();
	}
	
	public Compra(Requerimiento requerimiento, Date fechaCreacion){
		this();
		this.requerimiento = requerimiento;
	}

	public Integer getIdCompra() {
		return idCompra;
	}

	public void setIdCompra(Integer idCompra) {
		this.idCompra = idCompra;
	}

	public Float getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Float precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Float getPrecioFlete() {
		return precioFlete;
	}

	public void setPrecioFlete(Float precioFlete) {
		this.precioFlete = precioFlete;
	}
	
	public Boolean getTipoFlete() {
		return tipoFlete;
	}

	public void setTipoFlete(Boolean tipoFlete) {
		this.tipoFlete = tipoFlete;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public EEstatusCompra getEstatus() {
		return estatus;
	}

	public void setEstatus(EEstatusCompra estatus) {
		this.estatus = estatus;
	}

	public Requerimiento getRequerimiento() {
		return requerimiento;
	}

	public void setRequerimiento(Requerimiento requerimiento) {
		this.requerimiento = requerimiento;
	}

	public HistoricoMoneda getHistoricoMoneda() {
		return historicoMoneda;
	}

	public void setHistoricoMoneda(HistoricoMoneda historicoMoneda) {
		this.historicoMoneda = historicoMoneda;
	}

	public List<DetalleOferta> getDetalleOfertas() {
		return detalleOfertas;
	}

	public void setDetalleOfertas(List<DetalleOferta> detalleOfertas) {
		try {
			if(detalleOfertas != null && !detalleOfertas.isEmpty())
				for(DetalleOferta detalle : detalleOfertas)
					this.addDetalleOferta(detalle);
		} catch(Exception e){
			this.detalleOfertas = detalleOfertas;
		}
	}
	
	public DetalleOferta addDetalleOferta(DetalleOferta detalleOferta){
		getDetalleOfertas().add(detalleOferta);
		detalleOferta.setCompra(this);
		
		return detalleOferta;
	}
	
	public DetalleOferta removeDetalleOferta(DetalleOferta detalleOferta){
		getDetalleOfertas().remove(detalleOferta);
		detalleOferta.setCompra(null);
		
		return detalleOferta;
	}
	
	public FleteZoom getFleteZoom() {
		return fleteZoom;
	}

	public void setFleteZoom(FleteZoom fleteZoom) {
		this.fleteZoom = fleteZoom;
	}

	/**EVENTOS*/
	@PostLoad
	public void postLoad(){
		if(this.precioFlete==null)
			this.precioFlete = new Float(0);
	}

	/**METODOS PROPIOS DE LA CLASE*/
	public String determinarEstatus(){
		if(this.estatus.equals(EEstatusCompra.SOLICITADA))
			return "Solicitud de Pedido";
		else if(this.estatus.equals(EEstatusCompra.ENVIADA))
			return "Compra Realizada y Enviada a Proveedores";
		else if(this.estatus.equals(EEstatusCompra.RECHAZADA))
			return "Rechazada";
		else if(this.estatus.equals(EEstatusCompra.PAGADA))
			return "Pagada";
		else if(this.estatus.equals(EEstatusCompra.EN_ESPERA_DEPOSITO))
			return "En Espera Depósito";
		else if(this.estatus.equals(EEstatusCompra.EN_ESPERA_TRANSFERENCIA))
			return "En Espera Transferencia";
		else if(this.estatus.equals(EEstatusCompra.EN_ESPERA)){
			System.out.println("ESTATUS DE LA COMPRA -> "+this.estatus.equals(EEstatusCompra.EN_ESPERA));
			return "En espera";
		}
			
		return null;
	}
	
	public boolean registrar(){
		return (this.estatus.equals(EEstatusCompra.ENVIADA) && this.requerimiento.getEstatus().equals(EEstatusRequerimiento.CONCRETADO));
	}
	
	public Float calcularTotal(){
		float total = 0;
		if ( detalleOfertas != null && !detalleOfertas.isEmpty()){
			for(DetalleOferta detalleOferta : detalleOfertas){
				total = total + detalleOferta.calcularPrecioVentaConverter();
			}
		}
		
		if(getFleteZoom()!=null)
			return calcularSubTotal() + getFleteZoom().totalReal();
		else
			return calcularSubTotal();
	}
	
	public Float calcularSubTotal(){
		float total = 0;
		if ( detalleOfertas != null && !detalleOfertas.isEmpty()){
			for(DetalleOferta detalleOferta : detalleOfertas){
				total = total + detalleOferta.calcularPrecioVentaConverter();
			}
		}
		return total;
	}
	
	@Transient
	public Map<Proveedor, List<DetalleOferta>> getMap(){
		Map<Proveedor, List<DetalleOferta>> map = new HashMap<Proveedor, List<DetalleOferta>>();
		List<DetalleOferta> detalles = this.getDetalleOfertas();
		//Para agrupar los detalles por proveedor
		for(int i=0; i < detalles.size(); i++){
			Proveedor prov1 = detalles.get(i).getDetalleCotizacion().getCotizacion().getProveedor();
				if(map.get(prov1) == (null))
					map.put(prov1, new ArrayList<DetalleOferta>());
				
				map.get(prov1).add((DetalleOferta) detalles.get(i));
		}
		return map;
	}
	
	public Map<String, Number> cantidadPiezasYPeso(){
		float pesoTotal = 0;
		int cantidadPiezas = 0;
		
		for(DetalleOferta detalle : this.getDetalleOfertas()){
			cantidadPiezas += detalle.getDetalleCotizacion().getCantidad();
			pesoTotal += detalle.getDetalleCotizacion().calcularTotal();
		}
		
		Map<String, Number> parametros = new HashMap<String, Number>();
		parametros.put("pesoTotal", pesoTotal);
		parametros.put("cantidadPiezas", cantidadPiezas);
		
		return parametros;
	}


}
