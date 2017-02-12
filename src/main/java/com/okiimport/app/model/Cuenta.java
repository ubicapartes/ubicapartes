package com.okiimport.app.model;

import java.io.Serializable;

import javax.persistence.*;

import com.google.gson.annotations.SerializedName;
import com.okiimport.app.model.enumerados.EEstatusGeneral;
import com.okiimport.app.resource.model.AbstractEntity;


@Entity
@Table(name="cuenta")
@NamedQuery(name="Cuenta.findAll", query="SELECT c FROM Cuenta c")
public class Cuenta extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_cuenta")
	private Integer idCuenta;

	
	@Column(name="numero")
	@SerializedName("numero_cuenta")
	private String numero;
	
	@Column(name="tipo")
	@SerializedName("tipo_cuenta")
	private String tipo;
	
	@Column(name="titular")
	@SerializedName("titular")
	private String titular;
	
	@Column(name="identificacion")
	@SerializedName("identificacion")
	private String identificacion;
		
	@ManyToOne()
	@JoinColumn(name="id_banco")
	@SerializedName("nombre_banco")
	private Banco banco;
	
	//bi-directional many-to-one association to Estado
	@ManyToOne()
	@JoinColumn(name="id_persona")
	@SerializedName("nombre_proveedor")
	private Proveedor proveedor;
	
	@Enumerated(EnumType.STRING)
	private EEstatusGeneral estatus;
	

	public Cuenta() {
		this.banco=new Banco();
		this.numero="";
	}
	
	
	/**METODOS OVERRIDE*/
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Cuenta){
			if(this.getIdCuenta()!=null)
				return this.getIdCuenta().equals(((Cuenta) obj).getIdCuenta());
			else
				return (this.getNumero().equalsIgnoreCase(((Cuenta) obj).getNumero()));
		}
		else
			return super.equals(obj);
	}
	
	/**METODOS PROPIOS DE LA CLASE*/
	public Integer getIdCuenta() {
		return idCuenta;
	}


	public void setIdCuenta(Integer idCuenta) {
		this.idCuenta = idCuenta;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getTitular() {
		return titular;
	}


	public void setTitular(String titular) {
		this.titular = titular;
	}


	public String getIdentificacion() {
		return identificacion;
	}


	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}


	public Banco getBanco() {
		return banco;
	}


	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
	public EEstatusGeneral getEstatus() {
		return estatus;
	}

	public void setEstatus(EEstatusGeneral estatus) {
		this.estatus = estatus;
	}

	public Proveedor getProveedor() {
		return proveedor;
	}


	public void setProveedor(Proveedor proveedor) {
		this.proveedor = proveedor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Transient
	public boolean isEliminar() {
		return this.estatus.equals(EEstatusGeneral.INACTIVO);
	}
}