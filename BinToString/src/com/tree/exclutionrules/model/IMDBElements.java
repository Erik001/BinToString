package com.tree.exclutionrules.model;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tree.exclutionrules.dao.Caracteristica;
import com.tree.exclutionrules.dao.Grupo;
import com.tree.exclutionrules.dao.Item;
import com.tree.exclutionrules.dao.Modulo;
import com.tree.exclutionrules.dao.ValorCaracteristica;
import com.tree.exclutionrules.util.DataSource;

public class IMDBElements {
	public Connection connection;
	public IMDBElements(Connection connection){
		this.connection = connection;
	}

	private Logger logger = Logger.getLogger(IMDBElements.class.getName());

	public List<Grupo> getGroups(Connection connection) {

		List<Grupo> groupList = new ArrayList<>();
		String query = "SELECT gpo_id, gpo_Nombre FROM viewGrupos WHERE gpo_tipo = 1 order by 1";
		try (/*Connection connection = DataSource.getInstance().getConnection();*/
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				Grupo gpo = new Grupo();
				logger.log(Level.INFO, "Gpo id: " + resultSet.getString(1)
						+ " ---> " + "Gpo Name: " + resultSet.getString(2));
				gpo.setGroupId(resultSet.getInt(1));
				gpo.setGroupName(resultSet.getString(2));
				logger.log(Level.INFO, gpo.toString());
				groupList.add(gpo);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return groupList;
	}

	public List<Caracteristica> getCharList(Grupo group, Connection connection) {
		List<Caracteristica> characterisitcsList = new ArrayList<>();
		int charId = 0;
		boolean setCatalog = false;
		String carName = null;
		String strMT3 = null;
		String query = String.format(
				"SELECT DISTINCT b.car_id, \n" + "       b.car_tipovalor, \n"
				// + " RTRIM(b.car_desclarga_i1), \n"
						+ "       RTRIM(replace(b.car_desclarga_i1, '%%','%%%%')), \n"
						+ "       RTRIM(ISNULL(x.rpc_carMT3, '')) \n"
						+ "FROM   aci_AmbienteCarItem a, \n"
						+ "       car_caracteristicas b, \n"
						+ "       rpc_RefPCCar x \n"
						+ "WHERE  b.car_id = a.car_id \n"
						+ "       AND x.car_id =* b.car_id \n"
						+ "       AND x.gpo_id = %d \n"
						+ "       AND a.aci_val_id_mod IN (SELECT DISTINCT val_id \n"
						+ "                                FROM   val_valorcaracteristica, \n"
						+ "                                       rpm_RefPCMod \n"
						+ "                                WHERE  car_id = 1 \n"
						+ "                                       AND val_id = rpm_val_id_mod \n"
						+ "                                       AND gpo_id = %d)",
				group.getGroupId(), group.getGroupId());
		try (/*Connection connection = DataSource.getInstance().getConnection();*/
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				List<ValorCaracteristica> listVC = new ArrayList<>();

				charId = resultSet.getInt(1);
				switch (resultSet.getInt(2)) {
					case 0 :
						setCatalog = true;
						break;
					case 1 :
						setCatalog = false;
						break;
				}

				carName = resultSet.getString(3);
				strMT3 = resultSet.getString(4);
				// listVC = getValCar(charId);
				Caracteristica characterisitc = new Caracteristica(charId,
						strMT3, carName, setCatalog, listVC);
				logger.log(Level.INFO, characterisitc.toString());

				characterisitcsList.add(characterisitc);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return characterisitcsList;

	}

	public List<Caracteristica> getCharListByListOfGroups(String groups) {
		List<Caracteristica> characterisitcsList = new ArrayList<>();
		int charId = 0;
		boolean setCatalog = false;
		String carName = null;
		String strMT3 = null;
		String query = String.format(
				"SELECT DISTINCT b.car_id, \n" + "       b.car_tipovalor, \n"
				// + " RTRIM(b.car_desclarga_i1), \n"
						+ "       RTRIM(replace(b.car_desclarga_i1, '%%','%%%%')), \n"
						+ "       RTRIM(ISNULL(x.rpc_carMT3, '')) \n"
						+ "FROM   aci_AmbienteCarItem a, \n"
						+ "       car_caracteristicas b, \n"
						+ "       rpc_RefPCCar x \n"
						+ "WHERE  b.car_id = a.car_id \n"
						+ "       AND x.car_id =* b.car_id \n"
						+ "       AND x.gpo_id in (%s) \n"
						+ "       AND a.aci_val_id_mod IN (SELECT DISTINCT val_id \n"
						+ "                                FROM   val_valorcaracteristica, \n"
						+ "                                       rpm_RefPCMod \n"
						+ "                                WHERE  car_id = 1 \n"
						+ "                                       AND val_id = rpm_val_id_mod \n"
						+ "                                       AND gpo_id in (%s))",
				groups, groups);
		try (/*Connection connection = DataSource.getInstance().getConnection();*/
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				List<ValorCaracteristica> listVC = new ArrayList<>();

				charId = resultSet.getInt(1);
				switch (resultSet.getInt(2)) {
					case 0 :
						setCatalog = true;
						break;
					case 1 :
						setCatalog = false;
						break;
				}

				carName = resultSet.getString(3);
				strMT3 = resultSet.getString(4);
				// listVC = getValCar(charId);
				Caracteristica characterisitc = new Caracteristica(charId,
						strMT3, carName, setCatalog, listVC);
				logger.log(Level.INFO, characterisitc.toString());

				characterisitcsList.add(characterisitc);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return characterisitcsList;

	}

	public List<ValorCaracteristica> getValCar(long charId, Connection connection) {
		List<ValorCaracteristica> listVC = new ArrayList<>();
		String query = String.format("select car_id, \n"
				+ "			 val_id, \n" + "			 val_orden, \n"
				// + " isnull(rtrim(val_DescLarga_I1), ''), \n"
				+ "			 isnull(rtrim(replace(val_DescLarga_I1, '%%','%%%%')), ''), \n"
				+ "			 isnull(rtrim(replace(val_DescLarga_I2, '%%','%%%%')), ''), \n"
				// + " isnull(rtrim(val_DescLarga_I2), '') \n"
				+ "			 from val_valorcaracteristica val \n"
				+ "			 where car_id in (%d)", charId);
		try (/*Connection connection = DataSource.getInstance().getConnection();*/
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {

				ValorCaracteristica vc = new ValorCaracteristica();
				vc.setCarId(resultSet.getInt(1));
				vc.setValId(resultSet.getInt(2));
				vc.setValOrden(resultSet.getInt(3));
				vc.setDescLong1(resultSet.getString(4));
				vc.setDescLong2(resultSet.getString(5));

				logger.log(Level.INFO, vc.toString());

				listVC.add(vc);
			}

		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

		return listVC;
	}

	public List<Caracteristica> getCharListByListOfGroups(Grupo group, Connection connection) {
		List<Caracteristica> characterisitcsList = new ArrayList<>();
		int charId = 0;
		boolean setCatalog = false;
		String carName = null;
		String strMT3 = null;
		String query = String.format(
				"SELECT DISTINCT b.car_id, \n" + "       b.car_tipovalor, \n"
				// + " RTRIM(b.car_desclarga_i1), \n"
						+ "       RTRIM(replace(b.car_desclarga_i1, '%%','%%%%')), \n"
						+ "       RTRIM(ISNULL(x.rpc_carMT3, '')) \n"
						+ "FROM   aci_AmbienteCarItem a, \n"
						+ "       car_caracteristicas b, \n"
						+ "       rpc_RefPCCar x \n"
						+ "WHERE  b.car_id = a.car_id \n"
						+ "       AND x.car_id =* b.car_id \n"
						+ "       AND x.gpo_id = %d \n"
						+ "       AND a.aci_val_id_mod IN (SELECT DISTINCT val_id \n"
						+ "                                FROM   val_valorcaracteristica, \n"
						+ "                                       rpm_RefPCMod \n"
						+ "                                WHERE  car_id = 1 \n"
						+ "                                       AND val_id = rpm_val_id_mod \n"
						+ "                                       AND gpo_id in (%d))",
				group.getGroupId(), group.getGroupId());
		try (//Connection connection = DataSource.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				List<ValorCaracteristica> listVC = new ArrayList<>();

				charId = resultSet.getInt(1);
				switch (resultSet.getInt(2)) {
					case 0 :
						setCatalog = true;
						break;
					case 1 :
						setCatalog = false;
						break;
				}

				carName = resultSet.getString(3);
				strMT3 = resultSet.getString(4);
				// listVC = getValCar(charId);
				Caracteristica characterisitc = new Caracteristica(charId,
						strMT3, carName, setCatalog, listVC);
				logger.log(Level.INFO, characterisitc.toString());

				characterisitcsList.add(characterisitc);
			}

		} catch (SQLException /*| IOException | PropertyVetoException*/ sqle) {
			sqle.printStackTrace();
		}

		return characterisitcsList;

	}

	public ValorCaracteristica getValCarByCarAndValId(long charId, long valId) {
		ValorCaracteristica vc = new ValorCaracteristica();
		String query = String.format("select car_id, \n"
				+ "			 val_id, \n" + "			 val_orden, \n"
				// + " isnull(rtrim(val_DescLarga_I1), ''), \n"
				// + " isnull(rtrim(val_DescLarga_I2), '') \n"
				+ "			 isnull(rtrim(replace(val_DescLarga_I1, '%%','%%%%')), ''), \n"
				+ " 		 isnull(rtrim(replace(val_DescLarga_I2, '%%','%%%%')), '') \n"
				+ "			 from val_valorcaracteristica val \n"
				+ "			 where car_id = %d and val_id = %d", charId, valId);
		try (//Connection connection = DataSource.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				vc.setCarId(resultSet.getInt(1));
				vc.setValId(resultSet.getInt(2));
				vc.setValOrden(resultSet.getInt(3));
				vc.setDescLong1(resultSet.getString(4));
				vc.setDescLong2(resultSet.getString(5));

				logger.log(Level.INFO, vc.toString());

			}

		} catch (SQLException /*| IOException | PropertyVetoException*/ sqle) {
			sqle.printStackTrace();
		}

		return vc;
	}

	public Item getItemByItemId(long itemId) {
		Item itm = new Item();
		String query = String.format(
				"SELECT itm_Id, LTRIM(RTRIM(replace(itm_Desc2, '%%','%%%%'))) FROM itm_Items WHERE itm_Id = %d",
				itemId);

		try (//Connection connection = DataSource.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {

				itm.setItemId(resultSet.getInt(1));
				itm.setItm_Desc2(resultSet.getString(2));
				logger.log(Level.INFO, itm.toString());
			}

		} catch (SQLException /*| IOException | PropertyVetoException*/ sqle) {
			sqle.printStackTrace();
		}

		return itm;
	}

	public List<Modulo> getModuleListByListOfGroups(String gruposCSV) {
		List<Modulo> moduleList = new ArrayList<>();
		int moduleId = 0;
		String moduleName = null;

		String query = String
				.format("select distinct val_id, LTRIM(RTRIM(replace(val_desclarga_i1, '%%','%%%%'))) \n"
						+ "			 from val_valorcaracteristica, \n"
						+ "			 rpm_RefPCMod \n"
						+ "			 where	car_id = 1 \n"
						+ "			 and		val_id = rpm_val_id_mod \n"
						+ "			 and		gpo_id  IN (%s)", gruposCSV);
		try (//Connection connection = DataSource.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {

				moduleId = resultSet.getInt(1);

				moduleName = resultSet.getString(2);
				// listVC = getValCar(charId);
				Modulo module = new Modulo(moduleId, moduleName);

				moduleList.add(module);
			}

		} catch (SQLException /*| IOException | PropertyVetoException*/ sqle) {
			sqle.printStackTrace();
		}

		return moduleList;
	}

	public Grupo getGroupById(Long groupId) {
		Grupo gpo = new Grupo();
		String query = String.format(
				"SELECT gpo_id, LTRIM(RTRIM(gpo_Nombre)) FROM viewGrupos WHERE gpo_tipo = 1 AND gpo_Id =%d order by 1",
				groupId);
		try (//Connection connection = DataSource.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {

				gpo.setGroupId(resultSet.getInt(1));
				gpo.setGroupName(String.format("%04d %s", gpo.getGroupId(),
						resultSet.getString(2)));
				logger.log(Level.INFO, gpo.toString());

			}

		} catch (SQLException/* | IOException | PropertyVetoException*/ sqle) {
			sqle.printStackTrace();
		}

		return gpo;

	}

}
