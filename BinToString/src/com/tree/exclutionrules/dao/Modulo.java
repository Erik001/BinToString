package com.tree.exclutionrules.dao;

public class Modulo {
	private int moduleId;
	private String moduleDesc;
	
	
	
	public Modulo(int moduleId, String moduleDesc) {
		super();
		this.moduleId = moduleId;
		this.moduleDesc = moduleDesc;
	}
	
	
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleDesc() {
		return moduleDesc;
	}
	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}


	@Override
	public String toString() {
		return "Modulo [moduleId=" + moduleId + ", moduleDesc=" + moduleDesc
				+ "]";
	}
	
	
}
