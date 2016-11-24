package com.tree.exclutionrules.dao;

import java.util.List;
import java.util.ArrayList;

public class Caracteristica {

	private int carId;
	private String strMT3;
	private String strName;
	private boolean isCatalog;
	private List<ValorCaracteristica> listVC = new ArrayList<>();
	
	public Caracteristica(int carId, String strMT3, String strName,
			boolean isCatalog, List<ValorCaracteristica> listVC) {
		super();
		this.carId = carId;
		this.strMT3 = strMT3;
		this.strName = strName;
		this.isCatalog = isCatalog;
		this.listVC = listVC;
	}

	public Caracteristica() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public String getStrMT3() {
		return strMT3;
	}
	public void setStrMT3(String strMT3) {
		this.strMT3 = strMT3;
	}
	public String getStrName() {
		return strName;
	}
	public void setStrName(String strName) {
		this.strName = strName;
	}
	public boolean isCatalog() {
		return isCatalog;
	}
	public void setCatalog(boolean isCatalog) {
		this.isCatalog = isCatalog;
	}
	public List<ValorCaracteristica> getListVC() {
		return listVC;
	}
	public void setListVC(List<ValorCaracteristica> listVC) {
		this.listVC = listVC;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + carId;
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
		Caracteristica other = (Caracteristica) obj;
		if (carId != other.carId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Caracteristica [carId=" + carId + ", strMT3=" + strMT3
				+ ", strName=" + strName + ", isCatalog=" + isCatalog + "]";
	}
}
