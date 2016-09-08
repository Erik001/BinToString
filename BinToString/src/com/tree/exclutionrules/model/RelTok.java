package com.tree.exclutionrules.model;

public enum RelTok {
	TOK_EXP_REL_AND('&'), TOK_EXP_REL_OR('+'), TOK_EXP_REL_MINUS('-');

	private final char tok;

	private RelTok(char tok) {
		this.tok = tok;
	}

	@Override
	public String toString() {
		return tok + "";
	}
}
