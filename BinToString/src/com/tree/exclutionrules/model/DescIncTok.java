package com.tree.exclutionrules.model;

public enum DescIncTok {
	TOK_EXP_NOINC(" "), 
	TOK_EXP_IS(" Is Any Of "), 
	TOK_EXP_NOT(" Is None Of ");  
	
	private final String tok;

	private DescIncTok(String tok) {
		this.tok = tok;
	}

	@Override
	public String toString() {
		return tok;
	}
}
