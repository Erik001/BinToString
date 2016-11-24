package com.tree.exclutionrules.dao;

public class ReglaExclusion {
	private int ruleId;
	private String ruleName;
	private String ruleBin;
	public int getRuleId() {
		return ruleId;
	}
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getRuleBin() {
		return ruleBin;
	}
	public void setRuleBin(String ruleBin) {
		this.ruleBin = ruleBin;
	}
	public ReglaExclusion(int ruleId, String ruleName, String ruleBin) {
		super();
		this.ruleId = ruleId;
		this.ruleName = ruleName;
		this.ruleBin = ruleBin;
	}
	public ReglaExclusion() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ReglaExclusion [ruleId=" + ruleId + ", ruleName=" + ruleName
				+ ", ruleBin=" + ruleBin + "]";
	}
	
	
}
