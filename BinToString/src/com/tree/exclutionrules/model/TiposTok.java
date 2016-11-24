package com.tree.exclutionrules.model;

public enum TiposTok {
	TOK_EXP_ROOT('R'), TOK_EXP_COMP('C'), TOK_EXP_GRP('G'), TOK_EXP_MOD('M'), TOK_EXP_CAR('V'), TOK_EXP_ITM('I');

	private final char tok;

	private TiposTok(char tok) {
		this.tok = tok;
	}
	
	@Override
	public String toString() {
		return tok + "";
	}

	public char getTok() {
		
		return tok;
	}
	
	
}
