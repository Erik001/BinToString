package com.tree.exclutionrules.model;

public enum DescRelTok {
	TOK_EXP_REL_AND(" And "), 
	TOK_EXP_REL_OR(" Or "),
	TOK_EXP_REL_MINUS(" Minus "); 
	
	private final String tok;

	private DescRelTok(String tok) {
		this.tok = tok;
	}

	@Override
	public String toString() {
		return tok;
	}
}
