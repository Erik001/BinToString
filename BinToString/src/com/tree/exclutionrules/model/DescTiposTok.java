package com.tree.exclutionrules.model;

public enum DescTiposTok {
	TOK_EXP_ROOT(" "), TOK_EXP_COMP(" ( "), TOK_EXP_GRP(" Group "), TOK_EXP_MOD(" Modulo "), TOK_EXP_CAR(
			" Char. "), TOK_EXP_ITM(" Item ");

	private final String tok;

	private DescTiposTok(String tok) {
		this.tok = tok;
	}

	@Override
	public String toString() {
		return tok;
	}
}
