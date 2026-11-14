package ca.ubc.cs304.src.ui;

import ca.ubc.cs304.src.delegates.homescreenDelegate;

import javax.swing.*;
import java.awt.*;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class displayTblDialog extends JDialog {
    private JList<String> tableSelector;
    private DefaultListModel<String> model;
    private homescreenDelegate delegate = null;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JFrame frame;

    public displayTblDialog(JFrame parent, homescreenDelegate delegate) {
        super(parent, "Table options", true);
        this.delegate = delegate;
        this.frame = parent;
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        JLabel tblLabel = new JLabel("Select Table: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        add(tblLabel, gbc);

        model = new DefaultListModel<>();
        tableSelector = new JList<>(model);
        tableSelector.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(tableSelector, gbc);
        populationTableSelector();

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void populationTableSelector() {
        DatabaseMetaData dbmd = null;
        try {
            dbmd = delegate.getMetaData();
            ResultSet tables = dbmd.getTables(null, dbmd.getUserName().toUpperCase(), "%", new String[]{"TABLE"});
            while (tables.next()) {
                model.addElement(tables.getString("TABLE_NAME"));
            }

            DatabaseMetaData finalDbmd = dbmd;

            JButton submit = new JButton("Submit");
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            add(submit, gbc);

            submit.addActionListener(a -> {
                ArrayList<String> tableNames = (ArrayList<String>) tableSelector.getSelectedValuesList();
//                String table = String.join(", ", tableNames);
                List<Object> tableData = null;
                try {
                    tableData = delegate.selectTable(tableNames);
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage());
                }
                UIUtils.showQueryResultsInDialog(tableData,frame);
                dispose();
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

