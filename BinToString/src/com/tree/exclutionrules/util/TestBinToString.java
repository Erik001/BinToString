package com.tree.exclutionrules.util;

import java.beans.PropertyVetoException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tree.exclutionrules.dao.Caracteristica;
import com.tree.exclutionrules.dao.Formato;
import com.tree.exclutionrules.dao.ReglaExclusion;
import com.tree.exclutionrules.model.FormatsElements;
import com.tree.exclutionrules.model.IMDBElements;
import com.tree.exclutionrules.stringconvertion.TokenExpTotal;

public class TestBinToString {
	public static void main(String[] args) {
		List<Caracteristica> gl = new ArrayList<>();
		// TokenExpTotal st = new TokenExpTotal();
		// List<Grupo> lg = new ArrayList<Grupo>();
		// lg.add(new Grupo(10,"TEST"));
		// st.loadCharList(lg);
		// System.out.println(st.binExpToStrExp("1R?&0#V|&350#1#7#V|&8#1#0#)"));
		// System.out.println(st.binExpToStrExp("1R?&0#C|+0#V|+8#1#0#V!+D5#1#2#V|+2734#1#1#)V|&2721#2#B24#B25#)"));
		// System.out.println(st.binExpToStrExp("1R?&0#V!+2712#A#4F94#4664#D4C4#7F5C#3A3C#BC35#29E3#29F5#D4EA#29F7#V|+274C#1#1#V!+2741#1#2#)"));
		List<Formato> formatList = new FormatsElements().getListaFormatos();
		TokenExpTotal st = null;
		try (FileWriter fos = new FileWriter(
				"C:/Rules_Totales_CAM.txt", true);
				BufferedWriter buffWriter = new BufferedWriter(fos);
				Connection connection = DataSource.getInstance().getConnection();
					
					
		// String ca = "hola"; BAD is not a closable

		) {
			for (Formato fm : formatList) {
				if (fm.getRuleList().size() > 0
						&& fm.getGroupList().size() > 0) {
					System.out.println("Foramtid " + fm.getFormatid());
					st = new TokenExpTotal(connection);
					st.loadCharList(fm.getGroupList());
					st.loadModuleList(fm.getGroupList());
					for (ReglaExclusion re : fm.getRuleList()) {

						st.m_iIndice = 0;
						st.finalString = new StringBuilder();

						System.out.println(re);

						String resultingExpression = st
								.binExpToStrExp(re.getRuleBin());
						// System.out.println(resultingExpression);
						String line = String.format(fm.getFormatid() + "\t "
								+ fm.getFormatName() + "\t " + re.getRuleId()
								+ "\t " + re.getRuleName() + "\t "
								+ resultingExpression);

						// System.out.println(line + resultingExpression);
						buffWriter.write(line);
						buffWriter.newLine();

					}
				}
			}
			/*
			 * IMDBElements imdb = new IMDBElements(); gl = imdb.getChatList(new
			 * Grupo(10,"TEST")); for(Caracteristica g: gl)
			 * System.out.println(g);
			 */
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}catch(SQLException | PropertyVetoException sqle) {
			sqle.printStackTrace();
			
		}
	}
}