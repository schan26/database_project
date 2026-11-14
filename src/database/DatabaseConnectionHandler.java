package ca.ubc.cs304.src.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

import ca.ubc.cs304.src.model.PolicyModel;


/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
	// Use this version of the ORACLE_URL if you are running the code off of the server
	//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
	// Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String EXCEPTION_TAG = "[EXCEPTION]";
	private static final String WARNING_TAG = "[WARNING]";

	private Connection connection = null;

	public DatabaseConnectionHandler() {
		try {
			// Load the Oracle JDBC driver
			// Note that the path could change for new drivers
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


	public void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


	/**
	 * Inserts a new policy
	 */
	public void insertPolicy(PolicyModel model) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement("INSERT INTO Policy VALUES (?,?,?,?,?,?,?)");
			ps.setInt(1, model.getPolicyID());
			ps.setString(2, model.getPolicyName());
			ps.setString(3, model.getLicenseNumber());
			ps.setInt(4, model.getCoverageLimit());
			ps.setDate(5, java.sql.Date.valueOf(model.getStartDate()));
			ps.setDate(6, java.sql.Date.valueOf(model.getEndDate()));
			ps.setString(7, model.getPolicyStatus());

			PreparedStatement ps2 = connection.prepareStatement("INSERT INTO Healthcare VALUES (?, ?, ?)");
			ps2.setInt(1, model.getPolicyID()); // Must match the one just inserted
			ps2.setInt(2, model.getPremium());
			ps2.setInt(3, model.getDeductible());

			ps.executeUpdate();
			ps2.executeUpdate();
			connection.commit();

			ps.close();
			ps2.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			throw e;
		}
	}


	/**
	 * Updates a policy status
	 */
	public void updatePolicy(int policyID, String attribute, LocalDate date, String status, Integer license) throws SQLException {

		try {
			StringBuffer sql = new StringBuffer("UPDATE Policy SET ");
			PreparedStatement ps = null;


			switch (attribute) {
				case "POLICYNAME":
					sql.append("policyName = ? WHERE policyID = ?");
					ps = connection.prepareStatement(sql.toString());
					ps.setString(1, status);
					break;
				case "POLICYSTATUS":
					sql.append("policyStatus = ? WHERE policyID = ?");
					ps = connection.prepareStatement(sql.toString());
					ps.setString(1, status);
					break;
				case "PLICENSE#":
					sql.append("plicense# = ? WHERE policyID = ?");
					ps = connection.prepareStatement(sql.toString());
					ps.setString(1, status);
					break;

				case "COVERAGELIMIT":
					sql.append("coverageLimit = ? WHERE policyID = ?");
					ps = connection.prepareStatement(sql.toString());
					ps.setInt(1, license);
					break;

				case "ENDDATE":
					sql.append("endDate = ? WHERE policyID = ?");
					ps = connection.prepareStatement(sql.toString());
					ps.setDate(1, java.sql.Date.valueOf(date));
					break;
				case "STARTDATE":
					sql.append("startDate = ? WHERE policyID = ?");
					ps = connection.prepareStatement(sql.toString());
					ps.setDate(1, java.sql.Date.valueOf(date));
					break;

				default:
					break;

			}
			ps.setInt(2, policyID);

			int rowsUpdated = ps.executeUpdate();
			if (rowsUpdated == 0) {
				throw new SQLException(" Policy " + policyID + " does not exist!");
			}


			connection.commit();
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			throw e;
		}
	}


	/**
	 * filters policy with conditions set by user
	 */
	public List<Object> selectPoliciesByClause(String whereClause, List<Object> paramList) throws SQLException {
		List<Object> result = new ArrayList<>();

		String query = "SELECT * FROM Policy NATURAL JOIN PolicyHeld WHERE " + whereClause;
		PreparedStatement ps = connection.prepareStatement(query);

		for (int i = 0; i < paramList.size(); i++) {
			try {
				if (paramList.get(i) instanceof Integer) {ps.setInt(i + 1, (Integer) paramList.get(i));}
				else if (paramList.get(i) instanceof String) {ps.setString(i + 1, (String) paramList.get(i));}
			} catch  (SQLException e) {
				System.out.println(EXCEPTION_TAG + " set object invalid " + e.getMessage());
				throw e;
			}
		}

		result = getQueryResult(ps.executeQuery());
		ps.close();

		return result;
	}


	/**
	 * deleting a policy and deletes policy held
	 */
	public void deletePolicy(int policyID) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement("DELETE FROM Policy WHERE policyID = ?");
			ps.setInt(1, policyID);

			int rowCount = ps.executeUpdate();
			if (rowCount == 0) {
				throw new SQLException(" Policy " + policyID + " does not exist!");
			}

			connection.commit();

			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			throw e;
		}
	}


	/**
	 *	displays table with columns and table selected by user
	 */
	public List<Object> project(String select, String table) {
		List<Object> result = new ArrayList<>();
		try {
			String sql = "SELECT " + select + " FROM " + table;
			PreparedStatement ps = connection.prepareStatement(sql);
//			ps.setString(1, select);
//			ps.setString(2, table);

			result = getQueryResult(ps.executeQuery());
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
		return result;
	}


	/**
	 *	display entire selected table
	 */
	public List<Object> selectTable(ArrayList<String> tableNames) throws SQLException {
		List<Object> result = new ArrayList<>();

		try {
			String sql = "";
			if (tableNames.size() == 1) {
				sql = "SELECT " + "*" + " FROM " + tableNames.get(0);
			} else {
				sql = "SELECT " + "*" + " FROM " + tableNames.get(0);
				for (int i = 1; i < tableNames.size(); i++) {
					sql += " NATURAL JOIN " + tableNames.get(i);
				}
			}

			PreparedStatement ps = connection.prepareStatement(sql);
			result = getQueryResult(ps.executeQuery());
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			throw e;
		}
		return result;
	}


	/**
	 *	Query for "Find the policies that have been held by more than one client <group by + having>"
	 */
	public List<Object> findCount() {
		List<Object> result = new ArrayList<>();

		try {
			PreparedStatement ps = connection.prepareStatement("SELECT PLICENSE# AS PROVIDER_LICENSE, POLICYNAME, COUNT(*) AS COUNT FROM POLICY NATURAL JOIN POLICYHELD GROUP BY POLICYNAME, PLICENSE# HAVING COUNT(*)>1");
			result = getQueryResult(ps.executeQuery());
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}

		return result;
	}


	/**
	 *	Query for "Find agents who manage policies with all providers <division>"
	 */
	public List<Object> findAgent() {
		List<Object> result = new ArrayList<>();
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT DISTINCT A_NAME AS AGENT_NAME, AGENTLICENSE# FROM AGENT1 A1 NATURAL JOIN AGENT2 A2 WHERE NOT EXISTS( SELECT PLICENSE# FROM PROVIDER2 P WHERE NOT EXISTS( SELECT PLICENSE# FROM MANAGEDBY M NATURAL JOIN POLICY J WHERE A2.AGENTLICENSE#=M.AGENTLICENSE# AND P.PLICENSE#=J.PLICENSE#))");

			result = getQueryResult(ps.executeQuery());
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());

		}
		return result;
	}


	/**
	 *	Query for "Find the accident date and location, claim amount and status for a specified client <join>"
	 */
	public List<Object> findClaims(int clientID) {
		List<Object> result = new ArrayList<>();
		try {
			String sql = "SELECT LOCATION, ACCIDENTDATE, CLAIMAMOUNT, STATUS\n" +
					"FROM CLIENT6 NATURAL JOIN CLIENT1 NATURAL JOIN CLIENT3 NATURAL JOIN CLAIM NATURAL JOIN ACCIDENT\n" +
					"WHERE CLIENTID = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, clientID);

			result = getQueryResult(ps.executeQuery());
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());

		}
		return result;
	}


	/**
	 *	Query for "Find the provider that has the most clients <nested aggregation>"
	 */
	public List<Object> findProvider() {
		List<Object> result = new ArrayList<>();
		try {
			String sql = "SELECT P_NAME AS PROVIDER_NAME, PCOUNT.COUNT AS COUNT\n" +
					"FROM (\n" +
					"         SELECT PLICENSE#, COUNT(*) AS COUNT\n" +
					"         FROM POLICY\n" +
					"         GROUP BY PLICENSE#\n" +
					"     )  PCOUNT, PROVIDER2 P2, PROVIDER1 P1\n" +
					"WHERE PCOUNT.COUNT in (\n" +
					"    SELECT MAX(COUNT)\n" +
					"    FROM (\n" +
					"             SELECT PLICENSE#, COUNT(*) AS COUNT\n" +
					"             FROM POLICY\n" +
					"             GROUP BY PLICENSE#\n" +
					"         ))\n" +
					"    AND P2.PLICENSE# = PCOUNT.PLICENSE#\n" +
					"    AND P1.P_PHONENUMBER = P2.P_PHONENUMBER";
			PreparedStatement ps = connection.prepareStatement(sql);

			result = getQueryResult(ps.executeQuery());
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());

		}
		return result;
	}


	/**
	 *	Query for "Find the number of reported accidents per client <group by>"
	 */
	public List<Object> findAccidentCount() {
		List<Object> result = new ArrayList<>();
		try {
			String sql = "SELECT ACOUNT.CLIENTID, FIRSTNAME, LASTNAME, ACOUNT.COUNT AS ACCIDENT_COUNT\n" +
					"FROM (SELECT CLIENTID, COUNT(*) AS COUNT\n" +
					"      FROM ACCIDENT\n" +
					"      GROUP BY CLIENTID) ACOUNT, CLIENT6 C6, CLIENT1 C1, CLIENT3 C3\n" +
					"WHERE ACOUNT.CLIENTID=C6.CLIENTID\n" +
					"AND C1.C_PHONENUM=C6.C_PHONENUM\n" +
					"AND C3.C_PHONENUM=C6.C_PHONENUM";
			PreparedStatement ps = connection.prepareStatement(sql);

			result = getQueryResult(ps.executeQuery());
			ps.close();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());

		}
		return result;
	}


	/**
	 *	returns meta data of database
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return connection.getMetaData();
	}


	/**
	 *	log into db server
	 */
	public boolean login(String username, String password) throws SQLException {
		try {
			if (connection != null) {
				connection.close();
			}

			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			connection.setAutoCommit(false);

			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
			throw e;
//			return false;
		}
	}


	/**
	 * reset to previous commit of db
	 */
	private void rollbackConnection() {
		try  {
			connection.rollback();
		} catch (SQLException e) {
			System.out.println(EXCEPTION_TAG + " " + e.getMessage());
		}
	}


	/**
	 *	read sql script and setup database
	 */
	public void databaseSetup() throws SQLException, FileNotFoundException {
        String sqlFilePath = "src/sql/scripts/databaseSetup.sql";
        Statement stmt = connection.createStatement();
        Scanner scanner = new Scanner(new File(sqlFilePath));
        scanner.useDelimiter(";");

        while (scanner.hasNext()) {
            String command = scanner.next().trim();
            if (!command.isEmpty()) {
                try {
                    stmt.executeUpdate(command);
                } catch (SQLException e) {
                    System.err.println("Error executing command: " + command);
                }
            }
        }
        System.out.println("SQL script executed successfully.");
    }


	/**
	 *	private function: returns query result as table data after executing query command
	 */
	private List<Object> getQueryResult(ResultSet rs) throws SQLException {
		Vector<String> columnNames = new Vector<>();
		Vector<Vector<Object>> data = new Vector<>();

		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int i = 1; i <= columnCount; i++) {
			columnNames.add(rsmd.getColumnName(i));
		}

		while (rs.next()) {
			Vector<Object> row = new Vector<>();
			for (int i = 1; i <= columnCount; i++) {
				row.add(rs.getObject(i));
			}
			data.add(row);
		}

		rs.close();

		return Arrays.asList(columnNames, data);
	}
}