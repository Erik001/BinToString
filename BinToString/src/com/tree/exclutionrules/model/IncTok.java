package com.tree.exclutionrules.model;

public enum IncTok {
	TOK_EXP_NOINC('?'), TOK_EXP_IS('|'), TOK_EXP_NOT('!');

	private final char tok;

	private IncTok(char tok) {
		this.tok = tok;
	}

	@Override
	public String toString() {
		return tok + "";
	}
}
