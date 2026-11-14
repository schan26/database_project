package ca.ubc.cs304.src.delegates;


import ca.ubc.cs304.src.model.PolicyModel;

import java.io.FileNotFoundException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface uses the delegation design pattern where instead of having
 * the TerminalTransactions class try to do everything, it will only
 * focus on handling the UI. The actual logic/operation will be delegated to the 
 * controller class (in this case Bank).
 * 
 * TerminalTransactions calls the methods that we have listed below but 
 * Bank is the actual class that will implement the methods.
 */
public interface homescreenDelegate {
	public void databaseSetup() throws SQLException, FileNotFoundException;
	
//	public void deleteBranch(int branchId);
	public void insertPolicy(PolicyModel model) throws SQLException;
	public void deletePolicy(int policyID) throws SQLException;
	public void updatePolicy(int policyID, String attribute, LocalDate date, String status, Integer license) throws SQLException;

	public DatabaseMetaData getMetaData() throws SQLException;
	public List<Object> selectTable(ArrayList<String> tableNames) throws SQLException;
	public List<Object> project(String select, String table);
	public List<Object> findCount();
	public List<Object> findAgent();
	public List<Object> findClaims(int clientID);
	public List<Object> selectPoliciesByCustomClause(String clause, List<Object> paramValues) throws SQLException;
	public List<Object> findProvider();
	public List<Object> findAccidentCount();
	public void close();





//	public void showBranch();
//	public void updateBranch(int branchId, String name);
	
//	public void terminalTransactionsFinished();
}
