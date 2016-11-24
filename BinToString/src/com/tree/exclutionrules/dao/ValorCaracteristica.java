package com.tree.exclutionrules.dao;

public class ValorCaracteristica {
	
	private int carId;
	private int valId;
	private int valOrden;
	private String descLong1;
	private String descLong2;
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public int getValId() {
		return valId;
	}
	public void setValId(int valId) {
		this.valId = valId;
	}
	public int getValOrden() {
		return valOrden;
	}
	public void setValOrden(int valOrden) {
		this.valOrden = valOrden;
	}
	public String getDescLong1() {
		return descLong1;
	}
	public void setDescLong1(String descLong1) {
		this.descLong1 = descLong1;
	}
	public String getDescLong2() {
		return descLong2;
	}
	public void setDescLong2(String descLong2) {
		this.descLong2 = descLong2;
	}
	public ValorCaracteristica() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ValorCaracteristica(int carId, int valId, int valOrden,
			String descLong1, String descLong2) {
		super();
		this.carId = carId;
		this.valId = valId;
		this.valOrden = valOrden;
		this.descLong1 = descLong1;
		this.descLong2 = descLong2;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + carId;
		result = prime * result + valId;
		result = prime * result + valOrden;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValorCaracteristica other = (ValorCaracteristica) obj;
		if (carId != other.carId)
			return false;
		if (valId != other.valId)
			return false;
		if (valOrden != other.valOrden)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ValorCaracteristica [carId=" + carId + ", valId=" + valId
				+ ", valOrden=" + valOrden + ", descLong1=" + descLong1
				+ ", descLong2=" + descLong2 + "]";
	}

}
