package com.tree.exclutionrules.dao;

import java.util.List;

public class Formato {
	
	private int formatid;
	private String formatName;
	private List<Grupo> groupList;
	private List<ReglaExclusion> ruleList;
	
	public Formato() {
	}
	public Formato(int formatid, String formatName) {
		super();
		this.setFormatid(formatid);
		this.setFormatName(formatName);
	}
	public int getFormatid() {
		return formatid;
	}
	public void setFormatid(int formatid) {
		this.formatid = formatid;
	}
	public String getFormatName() {
		return formatName;
	}
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}
	@Override
	public String toString() {
		return "Formato [formatid=" + formatid + ", formatName=" + formatName
				+ "]";
	}
	public List<ReglaExclusion> getRuleList() {
		return ruleList;
	}
	public void setRuleList(List<ReglaExclusion> ruleList) {
		this.ruleList = ruleList;
	}
	public List<Grupo> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<Grupo> groupList) {
		this.groupList = groupList;
	}
	
	

}
