package com.tree.exclutionrules.dao;

public class Grupo {
	public Grupo(){
		
	}
	public Grupo(int groupId, String groupName) {
		this.groupId = groupId;
		this.groupName = groupName;
	}
	@Override
	public String toString() {
		return "Grupo [groupId=" + groupId + ", groupName=" + groupName + "]";
	}
	private int groupId;
	private String groupName;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	
}
