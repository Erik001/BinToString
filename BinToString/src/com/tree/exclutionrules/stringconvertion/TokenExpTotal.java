package com.tree.exclutionrules.stringconvertion;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.tree.exclutionrules.dao.Caracteristica;
import com.tree.exclutionrules.dao.Grupo;
import com.tree.exclutionrules.dao.Item;
import com.tree.exclutionrules.dao.Modulo;
import com.tree.exclutionrules.dao.ValorCaracteristica;
import com.tree.exclutionrules.model.DescIncTok;
import com.tree.exclutionrules.model.DescRelTok;
import com.tree.exclutionrules.model.DescTiposTok;
import com.tree.exclutionrules.model.IMDBElements;
import com.tree.exclutionrules.model.IncTok;
import com.tree.exclutionrules.model.RelTok;
import com.tree.exclutionrules.model.TiposTok;
import com.tree.exclutionrules.util.DataSource;

public class TokenExpTotal {
	private Connection connection;
	
	public TokenExpTotal(Connection connection) {
		this.connection = connection;
		//try(Connection connection = DataSource.getInstance().getConnection();){
		imdb = new IMDBElements(connection);
		/*}catch(SQLException | IOException | PropertyVetoException sqle) {
			sqle.printStackTrace();
			
		//}*/

	}


	// Datos del token
	public static StringBuilder finalString = new StringBuilder();
	public TiposTok m_iTipoToken;
	public IncTok m_iInclude; // Instrucción: incluir o no
	public RelTok m_iRelation; // Relación con la siguiente expresión
	public Deque<Long> m_aListaElem = new ArrayDeque<Long>(); // Elementos del
																// token (ids de
																// grupos, etc)
	public long m_lId; // Para el caso de valores de características

	private IMDBElements imdb; 
	public TokenExpTotal m_pSiguiente;
	public TokenExpTotal m_pPrimerHijo;

	private static final char TOK_ID_TOTAL = '1';
	private static final char CHAR_FIN_HIJOS = ')';
	public static int m_iIndice;
	private long inout_lId;
	private int iIndiceToken = -1;
	private static List<Caracteristica> cl = new ArrayList<>();
	private static List<Modulo> ml = new ArrayList<>();

	public void loadCharList(List<Grupo> groups) {
		int listSize = 0;
		String gruposCSV = "";
		if (groups.size() > 1) {
			for (Grupo g : groups) {
				if (listSize < groups.size() - 1) {
					gruposCSV += g.getGroupId() + ", ";
					listSize++;
				}else gruposCSV += g.getGroupId() + "";
			}
		} else {
			gruposCSV += groups.get(0).getGroupId() + "";
		}

		cl = imdb.getCharListByListOfGroups(gruposCSV);
		
	}
	
	public void loadModuleList(List<Grupo> groups) {
		int listSize = 0;
		String gruposCSV = "";
		if (groups.size() > 1) {
			for (Grupo g : groups) {
				if (listSize < groups.size() - 1) {
					gruposCSV += g.getGroupId() + ", ";
					listSize++;
				}else gruposCSV += g.getGroupId() + "";
			}
		} else {
			gruposCSV += groups.get(0).getGroupId() + "";
		}

		ml = imdb.getModuleListByListOfGroups(gruposCSV);
		
	}

	public String binExpToStrExp(String strBinExp) {
		// StringBuilder strExp = new StringBuilder();
		TokenExpTotal pToken = new TokenExpTotal(connection);
		// Si está vacío no se hace nada
		if (strBinExp.length() < 2)
			return "";

		try {
			// Se arma el árbol de expresiones en base a la cadena binaria dada
			pToken.fromExpStr(strBinExp);

			// Se genera la descripción que se puede leer
			// aBuffer.Inicializa();
			pToken.getDescription(strBinExp);
			// delete pToken;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pToken.finalString.toString();
	}

	public String fromExpStr(String inout_strExp) throws BinToStringException {
		// Validamos que el primer caracter sea TOK_ID_TOTAL
		char charPrimerCar = readToken(inout_strExp);
		if (charPrimerCar != TOK_ID_TOTAL)
			throw new BinToStringException(
					"Se esperaba TOK_ID_TOTAL al inicio de la expresion");

		return secondFromExpStr(inout_strExp);
	}

	public char readToken(String inout_strExp) throws BinToStringException {
		/**
		 * Lee un char del buffer. Parámetros inout_pToken Apuntador a la
		 * variable que recibe el valor Regresa true El dato se ha leído false
		 * El dato no existe o estaba mal
		 */
		if (inout_strExp.length() == m_iIndice)
			throw new BinToStringException();

		char selectedChar = inout_strExp.charAt(m_iIndice);
		m_iIndice++;
		return selectedChar;
	}

	public String secondFromExpStr(String inout_aBuffer)
			throws BinToStringException {
		char iToken = 0;
		// int iIndiceToken = -1;
		long lTamVector;
		long lId;
		// Deque<Long> m_aListaElem = new ArrayDeque<Long>();

		// Se lee el tipo de token
		iToken = readToken(inout_aBuffer);
		for (TiposTok tt : TiposTok.values()) {
			if (tt.getTok() == iToken) {
				m_iTipoToken = tt;
				break;
			}
		}
		if (m_iTipoToken != null)
			iIndiceToken++;
		else
			throw new BinToStringException();

		// Se lee el tipo de inclusión
		iToken = readToken(inout_aBuffer);
		for (IncTok tt : IncTok.values()) {
			if (tt.getTok() == iToken) {
				m_iInclude = tt;
				break;
			}
		}
		if (m_iInclude != null)
			iIndiceToken++;
		else
			throw new BinToStringException();

		// Se lee el tipo de relación
		iToken = readToken(inout_aBuffer);
		for (RelTok tt : RelTok.values()) {
			if (tt.getTok() == iToken) {
				m_iRelation = tt;
				break;
			}
		}
		if (m_iRelation != null)
			iIndiceToken++;
		else
			throw new BinToStringException();

		// Si es característica se lee el valor de la misma
		if (m_iTipoToken == TiposTok.TOK_EXP_CAR) {
			if (!readLong(inout_aBuffer))
				throw new BinToStringException();
			m_lId = inout_lId;
		}

		// Se lee el número de elementos del vector y se reserva el tamaño
		if (!readLong(inout_aBuffer))
			throw new BinToStringException();

		lTamVector = inout_lId;

		// SetTamLista ( lTamVector );

		// Se leen los valores de los elementos de la lista
		while (lTamVector-- > 0) {
			if (!readLong(inout_aBuffer))
				throw new BinToStringException();

			m_aListaElem.add(inout_lId);
		} ;

		// Ahora se leen los hijos si existen
		if (m_iTipoToken == TiposTok.TOK_EXP_COMP) {
			// ASSERT ( inout_aBuffer.PeekToken() );
			while (peekToken(inout_aBuffer) != CHAR_FIN_HIJOS) {
				TokenExpTotal pExp = new TokenExpTotal(connection);
				pExp.secondFromExpStr(inout_aBuffer);
				// secondFromExpStr(inout_aBuffer);
				agregaHijo(pExp);
			}
			iToken = readToken(inout_aBuffer); // Se extrae el token que indica
												// el fin de los hijos de la
												// exp. compuesta

		} else if (m_iTipoToken == TiposTok.TOK_EXP_ROOT) {

			while (peekToken(inout_aBuffer) != '0'
					&& peekToken(inout_aBuffer) != CHAR_FIN_HIJOS) {
				TokenExpTotal pExp = new TokenExpTotal(connection);
				pExp.secondFromExpStr(inout_aBuffer);
				// secondFromExpStr(inout_aBuffer);
				agregaHijo(pExp);
			}

		}
		return inout_aBuffer;
	}

	public boolean readLong(String m_pExpresionConst) {
		/**
		 * Lee un long del buffer. Parámetros inout_lId Apuntador a la variable
		 * que recibe el valor Regresa true El dato se ha leído false El dato no
		 * existe o estaba mal
		 */
		// char strEntero[12];
		// char *pFin;
		int iIndiceTerminador, iIndiceBuffer;
		long lSigno = 1;

		String strEntero = "";

		// Por desempeño
		if (m_pExpresionConst.substring(m_iIndice, m_iIndice + 2)
				.equals("0#")) {
			m_iIndice += 2;
			return true;
		}

		// Revisamos que se tenga un entero en la cadena
		iIndiceTerminador = m_pExpresionConst.indexOf('#', m_iIndice);

		if ((iIndiceTerminador < 0) || // Que exista el terminador
		// ( iIndiceTerminador - m_iIndice > ( sizeof ( strEntero ) -2 ) ) || //
		// Que la longitud sea de un entero
				(iIndiceTerminador == m_iIndice) // Que tenga al menos un
													// caracter
		)
			return false;

		// Leemos el signo si existe
		if (m_pExpresionConst.charAt(m_iIndice) == '-') {
			lSigno = -1;
			m_iIndice++;
		}

		strEntero = m_pExpresionConst.substring(m_iIndice, iIndiceTerminador);
		m_iIndice += strEntero.length();

		/*
		 * for ( iIndiceBuffer = 0; m_iIndice < iIndiceTerminador; m_iIndice++,
		 * iIndiceBuffer++ ) strEntero [ iIndiceBuffer ] = ( *m_pExpresionConst
		 * ) [ m_iIndice ];
		 */

		// strEntero [ iIndiceBuffer ] = 0;
		m_iIndice++; // Por el terminador

		// *inout_lId = strtol ( strEntero, &pFin, 16);
		inout_lId = Long.parseLong(strEntero, 16);
		(inout_lId) *= lSigno;
		return true;
	}

	public char peekToken(String m_pExpresionConst) {
		/**
		 * Lee un char del buffer, pero no avanza la posición del índice
		 * Parámetros inout_pToken Apuntador a la variable que recibe el valor
		 * Regresa true El dato se ha leído false El dato no existe o estaba mal
		 */
		if (m_iIndice >= m_pExpresionConst.length())
			return '0';
		else
			return m_pExpresionConst.charAt(m_iIndice);
	}

	void agregaHijo(TokenExpTotal in_pToken) {
		if (m_pPrimerHijo == null)
			m_pPrimerHijo = in_pToken;
		else {
			// Se busca el último hijo y se agrega
			TokenExpTotal pSiguiente = m_pPrimerHijo;
			while (pSiguiente.m_pSiguiente != null)
				pSiguiente = pSiguiente.m_pSiguiente;
			pSiguiente.m_pSiguiente = in_pToken;
		}
	}

	public boolean getDescription(String inout_aBuffer)
			throws BinToStringException {
		/*
		 * const CGrupoIMDB *pGrupo; const CModuloIMDB *pModulo; const CCarIMDB
		 * *pCar; const CItemIMDB *pItem; CString strTexto; CString sAux;
		 */
		Caracteristica car = null;

		// Se pone el adjetivo
		for (DescTiposTok tt : DescTiposTok.values()) {
			if (tt.ordinal() == m_iTipoToken.ordinal()) {
				finalString.append(tt.toString());
				break;
			}
		}
		if (m_iTipoToken != null)
			iIndiceToken++;
		else
			throw new BinToStringException();

		// si no es un agrupamiento de expresiones
		if (m_iTipoToken != TiposTok.TOK_EXP_COMP
				&& m_iTipoToken != TiposTok.TOK_EXP_ROOT) {
			if (m_iTipoToken == TiposTok.TOK_EXP_CAR) { // Si es característica
														// ponemos el nombre

				for (Caracteristica caracteristica : cl) {
					if (caracteristica.getCarId() == m_lId) {
						finalString.append(String.format("%s (%d)",
								caracteristica.getStrName(),
								caracteristica.getCarId()));
						break;
					} // TODO Find Char method would be better
				}

				/*
				 * pCar = in_pCarIMDB->GetCaracteristica ( m_lId ); if ( pCar ==
				 * NULL ) sAux.Format("!No existe! (%d)", m_lId); else
				 * sAux.Format("%s (%d)", pCar->GetNombre(), pCar->GetId()); if
				 * (! inout_aBuffer.WriteString (sAux)) throw new CErrorSistema
				 * (IDERR_BUFFER_SHORT, __FUNCTION__);
				 */
			}

			// El tipo de expresión
			// if (!inout_aBuffer.WriteString ( g_aListaDescIncTok [ m_iInclude
			// ])) throw new CErrorSistema ( IDERR_BUFFER_SHORT, __FUNCTION__ );
			for (DescIncTok tt : DescIncTok.values()) {
				if (tt.ordinal() == m_iInclude.ordinal()) {
					finalString.append(tt.toString());
					break;
				}
			}
			if (m_iTipoToken != null)
				iIndiceToken++;
			else
				throw new BinToStringException();

			// La lista de elementos
			int listSizeCounter = 0;
			for (Long l : m_aListaElem) {
				switch (m_iTipoToken) {
					case TOK_EXP_GRP :
						Grupo gr = imdb.getGroupById(l);
						finalString.append(String.format("%s (%d)",
								gr.getGroupName(),
								gr.getGroupId()));
						
						// pGrupo = in_pCarIMDB->GetGrupo (m_aListaElem[i]);
						// if ( pGrupo == NULL ) sAux.Format("!No existe! (%d)",
						// m_aListaElem [i]);
						// else sAux.Format("%s (%d)", pGrupo->GetNombre(),
						// pGrupo->GetId());
						// if (!inout_aBuffer.WriteString (sAux)) throw new
						// CErrorSistema ( IDERR_BUFFER_SHORT, __FUNCTION__ );
						break;
					case TOK_EXP_MOD :
						for (Modulo module : ml) {
							if (module.getModuleId() == l) {
								finalString.append(String.format("%s (%d)",
										module.getModuleDesc(),
										module.getModuleId()));
								break;
							} // TODO Find Char method would be better
						}
						// pModulo = in_pCarIMDB->GetModulo (m_aListaElem[i]);
						// if ( pModulo == NULL ) sAux.Format("!No existe!
						// (%d)", m_aListaElem[i]);
						// else sAux.Format("%s (%d)", pModulo->GetNombre(),
						// pModulo->GetId());
						// if (!inout_aBuffer.WriteString (sAux)) throw new
						// CErrorSistema ( IDERR_BUFFER_SHORT, __FUNCTION__ );
						break;
					case TOK_EXP_CAR :
						ValorCaracteristica vc = imdb
								.getValCarByCarAndValId(m_lId, l);
						if (vc.getDescLong1() == null
								&& vc.getDescLong2() == null)
							finalString.append(String.format("%s (%d)",
									"00000000", vc.getValId()));
						else
							finalString.append(String.format("%s (%d)",
									vc.getDescLong1(), vc.getValId()));
						// if (in_pCarIMDB->GetValCar(m_lId, m_aListaElem[i],
						// &strTexto, NULL) == false)
						// sAux.Format("!No existe! (%d)", m_aListaElem[i]);
						// else sAux.Format("%s (%d)", strTexto,
						// m_aListaElem[i]);
						// if (!inout_aBuffer.WriteString(sAux)) throw new
						// CErrorSistema ( IDERR_BUFFER_SHORT, __FUNCTION__ );

						break;
					case TOK_EXP_ITM :
						Item i = imdb.getItemByItemId(l);
						finalString.append(String.format("%s (%d)",
								i.getItm_Desc2(), i.getItemId()));
						// pItem = in_pCarIMDB->GetItem(m_aListaElem [i]);
						// if ( pItem == NULL ) sAux.Format("!No existe! (%d)",
						// m_aListaElem [i]);
						// else sAux.Format("%s (%d)", pItem->GetNombre(),
						// pItem->GetId());
						// if (!inout_aBuffer.WriteString (sAux)) throw new
						// CErrorSistema ( IDERR_BUFFER_SHORT, __FUNCTION__ );
						break;
				}

				listSizeCounter++;
				// Si no es el último elemento ponemos una coma
				if (listSizeCounter < m_aListaElem.size())
					finalString.append(", ");

			}
		} else {
			// De otro modo es una expresión compuesta. Iteramos sobre los hijos
			// y ponemos un cierre de paréntesis
			TokenExpTotal pHijo = m_pPrimerHijo;
			while (pHijo != null) {
				pHijo.getDescription(inout_aBuffer);
				pHijo = pHijo.m_pSiguiente;
			}

			// Si no somos root, ponemos un paréntesis derecho
			if (m_iTipoToken != TiposTok.TOK_EXP_ROOT) {
				// if (!inout_aBuffer.WriteString (")")) throw new CErrorSistema
				// ( IDERR_BUFFER_SHORT, __FUNCTION__ );
				finalString.append(")");
			}
		}

		// Si tenemos siguiente, ponemos el operador de relación
		if (m_pSiguiente != null) {
			for (DescRelTok tt : DescRelTok.values()) {
				if (tt.ordinal() == m_iRelation.ordinal()) {
					finalString.append(tt.toString());
					break;
				}
			}
			if (m_iRelation != null)
				iIndiceToken++;
			else
				throw new BinToStringException();
		}

		return true;
	}

}
