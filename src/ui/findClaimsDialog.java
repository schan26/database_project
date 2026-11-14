package ca.ubc.cs304.src.ui;

import ca.ubc.cs304.src.delegates.homescreenDelegate;

import javax.swing.*;
import java.awt.*;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class findClaimsDialog extends JDialog {
    private JComboBox<String> clientSelector;
    private homescreenDelegate delegate = null;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JButton submit;
    private JFrame frame;

    public findClaimsDialog(JFrame parent, homescreenDelegate delegate) {
        super(parent, "Find claims", true);
        this.delegate = delegate;
        this.frame = parent;
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        JLabel tblLabel = new JLabel("Select Client ID: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 10, 5, 0);
        add(tblLabel, gbc);

        clientSelector = new JComboBox<>();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(clientSelector, gbc);
        populateClientSelector();
        clientSelector.addActionListener(e -> {
            int clientId = Integer.parseInt(clientSelector.getSelectedItem().toString());
            dispose();
            UIUtils.showQueryResultsInDialog(delegate.findClaims(clientId), frame);
        });

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void populateClientSelector() {

        try {
            List<Object> clientTable = delegate.selectTable(new ArrayList<>(Arrays.asList("Client6")));

            Vector<String> columnNames = (Vector<String>) clientTable.get(0);
            Vector<Vector<Object>> data = (Vector<Vector<Object>>) clientTable.get(1);
            int index = columnNames.indexOf("CLIENTID");

            for (Vector<Object> row : data) {
                clientSelector.addItem(row.get(index).toString());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
