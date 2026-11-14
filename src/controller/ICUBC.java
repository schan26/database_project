package ca.ubc.cs304.src.controller;

import ca.ubc.cs304.src.database.DatabaseConnectionHandler;
import ca.ubc.cs304.src.delegates.LoginWindowDelegate;
import ca.ubc.cs304.src.delegates.homescreenDelegate;
import ca.ubc.cs304.src.model.PolicyModel;
import ca.ubc.cs304.src.ui.LoginWindow;
import ca.ubc.cs304.src.ui.frame;

import java.io.FileNotFoundException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main controller class that will orchestrate everything.
 */
public class ICUBC implements LoginWindowDelegate, homescreenDelegate {
    private DatabaseConnectionHandler dbHandler = null;
    private LoginWindow loginWindow = null;
    private frame mainFrame;

    public ICUBC() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        loginWindow = new LoginWindow();
        loginWindow.showFrame(this);
    }

    /**
     * LoginWindowDelegate Implementation
     *
     * connects to Oracle database with supplied username and password
     */
    public void login(String username, String password) throws SQLException, FileNotFoundException {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and start text transaction flow
            loginWindow.dispose();
            frame mainframe = new frame(this);
            mainframe.setVisible(true);
            databaseSetup();

        } else {
            loginWindow.handleLoginFailed();

            if (loginWindow.hasReachedMaxLoginAttempts()) {
                loginWindow.dispose();
                System.out.println("You have exceeded your number of allowed attempts");
                System.exit(-1);
            }
        }
    }


    /**
     * homescreenDelegate implementation
     * Insert a policy with given policy info
     */
    @Override
    public void insertPolicy(PolicyModel model) throws SQLException {
        dbHandler.insertPolicy(model);
    }

    /**
     * homescreenDelegate implementation
     * get meta data of db
     */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return dbHandler.getMetaData();
    }

    /**
     * homescreenDelegate implementation
     * query table and return table data
     */
    @Override
    public List<Object> selectTable(ArrayList<String> tableNames) throws SQLException {
        return dbHandler.selectTable(tableNames);
    }

    /**
     * homescreenDelegate implementation
     * query specified columns from specified table and return table data
     */
    @Override
    public List<Object> project(String select, String table) {
        return dbHandler.project(select, table);
    }

    /**
     * homescreenDelegate implementation
     * query and return table data for query "Find the policies that have been held by more than one client <group by + having>"
     */
    @Override
    public List<Object> findCount() {
        return dbHandler.findCount();
    }

    /**
     * homescreenDelegate implementation
     * query and return table data
     */
    @Override
    public List<Object> findAgent() {
        return dbHandler.findAgent();
    }

    @Override
    public List<Object> findClaims(int clientID) {
        return dbHandler.findClaims(clientID);
    }

    /**
     * homescreenDelegate implementation
     * close connection to db
     */
    @Override
    public void close() {
        dbHandler.close();
    }

    /**
     *
     * homescreenDelegate implementation
     * Delete policy
     */
    @Override
    public void deletePolicy(int policyId) throws SQLException {
        dbHandler.deletePolicy(policyId);
    }

    @Override
    public void updatePolicy(int policyID, String attribute, LocalDate date, String status, Integer license) throws SQLException{
        dbHandler.updatePolicy(policyID, attribute, date, status, license);
    }

    @Override
    public List<Object> findProvider() {
        return dbHandler.findProvider();
    }

    @Override
    public List<Object> findAccidentCount() {
        return dbHandler.findAccidentCount();
    }

    @Override
    public List<Object> selectPoliciesByCustomClause(String clause, List<Object> paramList) throws SQLException {
        return dbHandler.selectPoliciesByClause(clause, paramList);
    }


    /**
     * homescreenDelegate Implementation
     *
     * The TerminalTransaction instance tells us that the user is fine with dropping any existing table
     * called branch and creating a new one for this project to use
     */
	public void databaseSetup() throws SQLException, FileNotFoundException {
		dbHandler.databaseSetup();
	}



    /**
     * Main method called at launch time
     */
    public static void main(String args[]) {
        ICUBC app = new ICUBC();
        app.start();
    }
}