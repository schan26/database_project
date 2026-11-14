package ca.ubc.cs304.src.ui;

import ca.ubc.cs304.src.delegates.homescreenDelegate;

import javax.swing.*;
import java.awt.*;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class policyAttribute extends JDialog{
    private JComboBox<String> tableSelector;
    private homescreenDelegate delegate = null;
    private GridBagConstraints gbc = new GridBagConstraints();
    private JFrame frame;

    public policyAttribute(JFrame parent, homescreenDelegate delegate, int policyID) {
        super(parent, "Policy Attribute options", true);
        this.frame = parent;
        this.delegate = delegate;
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        JLabel tblLabel = new JLabel("Select attribute: ");
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 20, 10, 0);
        add(tblLabel, gbc);

        tableSelector = new JComboBox<>();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(tableSelector, gbc);
        attributeSelector(policyID);

        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void attributeSelector(int id) {
        DatabaseMetaData dbmd = null;
        try {
            dbmd = delegate.getMetaData();
            ResultSet atrributes = dbmd.getColumns(null, dbmd.getUserName().toUpperCase(), "POLICY", "%");
            atrributes.next();
            while (atrributes.next()) {
                tableSelector.addItem(atrributes.getString("COLUMN_NAME"));
            }

            DatabaseMetaData finalDbmd = dbmd;

//

            tableSelector.addActionListener(e -> {
                String attribute = tableSelector.getSelectedItem().toString();
                dispose();

                JDialog d = new JDialog(frame, "", true);
                GridBagLayout gbl = new GridBagLayout();
                d.setLayout(gbl);
                GridBagConstraints gbc = new GridBagConstraints();

                // place label for client ID
                JLabel attributeLabel= new JLabel(attribute);
                gbc.gridwidth = GridBagConstraints.RELATIVE;
                gbc.insets = new Insets(10, 10, 5, 0);
                gbl.setConstraints(attributeLabel, gbc);
                d.add(attributeLabel);

                // place the text field for the client ID
                JTextField attributeField = new JTextField(15);
                gbc.gridwidth = GridBagConstraints.REMAINDER;
                gbc.insets = new Insets(10, 0, 5, 10);
                gbl.setConstraints(attributeField, gbc);
                d.add(attributeField);

                // place submit and cancel button below
                JPanel buttons = new JPanel();
                buttons.setLayout(new FlowLayout());

                JButton submit = new JButton("Update");
                submit.addActionListener(f ->{
                    String input = attributeField.getText();
                    d.dispose();

                    if(input != null){
                        if(attribute.equals("POLICYNAME") || attribute.equals("POLICYSTATUS") || attribute.equals("PLICENSE#")){
                            try {
                                delegate.updatePolicy(id, attribute, null, input, null);
                                JOptionPane.showMessageDialog(null, "Policy successfully Updated!");
                            } catch (SQLException err) {
                                int code = err.getErrorCode();
                                String msg = "";
                                switch (code) {
                                    case 1:
                                        msg = "Policy already exists. Please enter another policy ID!";
                                        break;
                                    case 2291:
                                        msg = "Provider license does not exist.";
                                        break;
                                    case 12899:
                                        msg = "Policy name/status/provider license input is too long.";
                                        break;
                                    default:
                                        msg = err.getMessage();
                                }
                                JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        else if( attribute.equals("COVERAGELIMIT")){
                            try {
                                delegate.updatePolicy(id, attribute, null, null, Integer.parseInt(input));
                                JOptionPane.showMessageDialog(null, "Policy successfully Updated!");
                            } catch (SQLException err) {
                                int code = err.getErrorCode();
                                String msg = "";
                                switch (code) {
                                    case 2290:
                                        msg = "Please enter a non-negative number.";
                                        break;
                                    default:
                                        msg = err.getMessage();
                                }
                                JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
                            } catch (NumberFormatException i) {
                                JOptionPane.showMessageDialog(null, "Invalid number entered for " + attribute);
                            }
                        }
                        else if(attribute.equals("STARTDATE") || attribute.equals("ENDDATE") ){
                            try {
                                delegate.updatePolicy(id, attribute, LocalDate.parse(input), null, null);
                                JOptionPane.showMessageDialog(null, "Policy successfully Updated!");
                            } catch (SQLException err) {
                                JOptionPane.showMessageDialog(null, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            } catch (DateTimeParseException i) {
                                JOptionPane.showMessageDialog(null, "Invalid date format. Use YYYY-MM-DD.");
                            }
                        }
                    }

                } );

                JButton cancel = new JButton("Cancel");
                cancel.addActionListener(f -> d.dispose());

                buttons.add(submit);
                buttons.add(cancel);
                gbc.anchor = GridBagConstraints.CENTER;
                gbl.setConstraints(buttons, gbc);
                d.add(buttons);


                d.pack();
                d.setLocationRelativeTo(frame);
                d.setVisible(true);
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
