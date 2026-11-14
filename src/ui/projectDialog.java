package ca.ubc.cs304.src.ui;

import ca.ubc.cs304.src.delegates.homescreenDelegate;

import javax.swing.*;
import java.awt.*;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class projectDialog extends JDialog {
    private JComboBox<String> tableSelector;
    private homescreenDelegate delegate = null;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JPanel colPanel = new JPanel(new GridBagLayout());
    private JFrame frame;


    public projectDialog(JFrame parent, homescreenDelegate delegate) {
        super(parent, "Project options", true);
        this.delegate = delegate;
        this.frame = parent;
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        JLabel tblLabel = new JLabel("Select Table: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        add(tblLabel, gbc);

        tableSelector = new JComboBox<>();
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
                tableSelector.addItem(tables.getString("TABLE_NAME"));
            }

            DatabaseMetaData finalDbmd = dbmd;

            tableSelector.addActionListener(e-> {
                colPanel.removeAll();
                String table =  tableSelector.getSelectedItem().toString();



                DefaultListModel<String> model = new DefaultListModel<>();
                JList<String> columnsSelector = new JList<>(model);
                columnsSelector.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                ResultSet columns = null;
                try {
                    columns = finalDbmd.getColumns(null, finalDbmd.getUserName().toUpperCase(), table, "%");
                    while (columns.next()) {
                        model.addElement(columns.getString("COLUMN_NAME"));
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                colPanel.add(columnsSelector, gbc);


                JButton submit = new JButton("Submit");
                colPanel.add(submit, gbc);
                submit.addActionListener(a -> {
                    List<String> selectValues = columnsSelector.getSelectedValuesList();
                    if (selectValues.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No columns selected.");
                    } else {
                        ArrayList<String> columnNames = new ArrayList<>(selectValues);
                        String select = String.join(", ", columnNames);
                        List<Object> tableData = delegate.project(select, table);
                        UIUtils.showQueryResultsInDialog(tableData,frame);
                        dispose();
                    }
                });

                JLabel hint = new JLabel("ctrl + click to select multiple options");
                colPanel.add(hint, gbc);

                add(colPanel, gbc);
                revalidate();
                repaint();
                pack();

            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
