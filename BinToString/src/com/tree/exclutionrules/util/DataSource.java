package com.tree.exclutionrules.util;

import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.dbcp2.BasicDataSource;


public class DataSource {

	private static DataSource datasource;
	private BasicDataSource ds;
	private Properties dbProp = new Properties();
	private String url;
	private String usernName;
	private String password;
	private String imdbServer;
	private String imdbDB;
	private String driver;
	private Logger logger = Logger.getLogger(DataSource.class.getName());

	private DataSource()
			throws IOException, SQLException, PropertyVetoException {
		try {
			FileInputStream fis = new FileInputStream("dbInfo.properties");
			dbProp.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}

		imdbServer = dbProp.getProperty("imdbServer");
		imdbDB = dbProp.getProperty("imdbDB");

		url = String.format(
				"jdbc:sqlserver://%s\\SQLSERVER:1433;databaseName=%s",
				imdbServer, imdbDB);

		usernName = dbProp.getProperty("userName");
		password = dbProp.getProperty("password");
		driver = dbProp.getProperty("driver");
		ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(usernName);
		ds.setPassword(password);
		ds.setUrl(url);

		// the settings below are optional -- dbcp can work with defaults
		ds.setMinIdle(5);
		ds.setMaxIdle(20);
		ds.setMaxOpenPreparedStatements(180);

	}

	public static DataSource getInstance()
			throws IOException, SQLException, PropertyVetoException {
		if (datasource == null) {
			datasource = new DataSource();
			return datasource;
		} else {
			return datasource;
		}
	}

	public Connection getConnection() throws SQLException {
		return this.ds.getConnection();
	}

}
