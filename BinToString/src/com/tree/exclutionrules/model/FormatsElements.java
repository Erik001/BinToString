package com.tree.exclutionrules.model;

import java.awt.geom.RectangularShape;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tree.exclutionrules.dao.Formato;
import com.tree.exclutionrules.dao.Grupo;
import com.tree.exclutionrules.dao.ReglaExclusion;

public class FormatsElements {

	private Properties dbProp = new Properties();
	private String url;
	private String usernName;
	private String password;
	private String formatsServer;
	private String formatsDB;
	private Logger logger = Logger.getLogger(IMDBElements.class.getName());

	public FormatsElements() {
		try {
			FileInputStream fis = new FileInputStream("dbInfo.properties");
			dbProp.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		formatsServer = dbProp.getProperty("formatsServer");
		formatsDB = dbProp.getProperty("formatsDB");

		url = String.format(
				"jdbc:sqlserver://%s\\SQLSERVER:1433;databaseName=%s",
				formatsServer, formatsDB);

		usernName = dbProp.getProperty("userName");
		password = dbProp.getProperty("password");

	}

	public List<Formato> getListaFormatos() {
		List<Formato> formatList = new ArrayList<>();

		String query = "SELECT f.FormatId, f.FormatNom FROM [Format] f  WHERE f.StatusId > 1 order by 1";
		try (Connection connection = DriverManager.getConnection(url, usernName,
				password);
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {
			while (rs.next()) {
				Formato formato = new Formato();
				// logger.log(Level.INFO, "Formato id: " + rs.getString(1) + "
				// ---> "
				// + "Formato Name: " + rs.getString(2));
				formato.setFormatid(rs.getInt(1));
				formato.setFormatName(rs.getString(2));
				formato.setGroupList(getListOfGroupsByFormat(formato.getFormatid(), connection));
				formato.setRuleList(getListOfReglasEclusionByFormatId(formato.getFormatid(), connection));
				logger.log(Level.INFO, formato.toString());
				formatList.add(formato);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return formatList;
	}
	
	public List<Grupo> getListOfGroupsByFormat(int formatId, Connection connection){
		List<Grupo> groupList = new ArrayList<>();

		String query = String.format("SELECT DISTINCT fm.GroupId FROM FormatModule fm WHERE fm.FormatId = %d ORDER BY fm.GroupId",formatId);
		try (//Connection connection = DriverManager.getConnection(url, usernName,
				//password);
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {
			while (rs.next()) {
				Grupo grupo = new Grupo(rs.getInt(1), "Grupo " + rs.getInt(1));			
				groupList.add(grupo);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return groupList;
		
	}
	
	public List<ReglaExclusion> getListOfReglasEclusionByFormatId(int formatId, Connection connection){
		List<ReglaExclusion> exclList = new ArrayList<>();

		//String query = String.format("SELECT fi.RuleId, fi.RuleNom, fi.RuleBin FROM FormatIERule fi WHERE fi.FormatId = %d order by 1",formatId);
		String query = String.format("SELECT fe.FormatLineId, replace(fe.LineDsc, '%%','%%%%'), fe.LineBin  FROM FormatExpansion fe WHERE fe.LineBin LIKE '1R%%' and fe.FormatId = %d", formatId);

		try (//Connection connection = DriverManager.getConnection(url, usernName,
				//password);
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(query)) {
			while (rs.next()) {
				ReglaExclusion exclRule = new ReglaExclusion(rs.getInt(1), rs.getString(2), rs.getString(3));			
				exclList.add(exclRule);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return exclList;
		
	}

}
